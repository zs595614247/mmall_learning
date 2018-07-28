package com.mmall.task;

import com.mmall.common.Cons;
import com.mmall.common.RedissonManage;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.ShardRedisPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CloseOrderTask {

    private final IOrderService iOrderService;

    private final RedissonManage redissonManage;

    @Autowired
    public CloseOrderTask(IOrderService iOrderService,RedissonManage redissonManage) {
        this.iOrderService = iOrderService;
        this.redissonManage = redissonManage;
    }

//    @Scheduled(cron = "0 0/1 * * * ?" )
    public void closeOrderTaskV1() {
        log.info("定时任务启动");
        iOrderService.closeOrder(PropertiesUtil.getIntegerProperty("close.order.task.time.hour","2"));
        log.info("定时任务结束");
    }

//    @Scheduled(cron = "0 0/1 * * * ?" )
    public void closeOrderTaskV2() {
        log.info("定时任务启动");
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setNxResult = ShardRedisPoolUtil.setNx(Cons.RedisLock.CLOSE_ORDER_TASK_LOCK, Long.toString(System.currentTimeMillis() + lockTimeout));
        if (setNxResult != null && setNxResult.intValue() == 1) {
            closeOrder();
        }else{
            log.info("没有获得分布式锁:{}", Cons.RedisLock.CLOSE_ORDER_TASK_LOCK);
        }

        log.info("定时任务结束");
    }

//    @Scheduled(cron = "0 0/1 * * * ?" )
    public void closeOrderTaskV3() {
        log.info("定时任务启动");
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setNxResult = ShardRedisPoolUtil.setNx(Cons.RedisLock.CLOSE_ORDER_TASK_LOCK, Long.toString(System.currentTimeMillis() + lockTimeout));
        if (setNxResult != null && setNxResult.intValue() == 1) {
            closeOrder();
        }else{
            String lockValueStr = ShardRedisPoolUtil.get(Cons.RedisLock.CLOSE_ORDER_TASK_LOCK);
            if (lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)) {
                String getSetResult = ShardRedisPoolUtil.getSet(Cons.RedisLock.CLOSE_ORDER_TASK_LOCK, Long.toString(System.currentTimeMillis() + lockTimeout));
                if (getSetResult == null || lockValueStr.equals(getSetResult)) {
                    closeOrder();
                }else {
                    log.info("没有获得分布式锁:{}", Cons.RedisLock.CLOSE_ORDER_TASK_LOCK);
                }
            }else{
                log.info("没有获得分布式锁:{}", Cons.RedisLock.CLOSE_ORDER_TASK_LOCK);
            }
        }
        log.info("定时任务结束");
    }


    @Scheduled(cron = "0 0/1 * * * ?" )
    public void closeOrderTaskV4() {
        RLock lock = redissonManage.getRedissonClient().getLock(Cons.RedisLock.CLOSE_ORDER_TASK_LOCK);
        boolean tryLock = false;
        try {
            if (tryLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                log.info("Redisson获取分布式锁成功:{},ThreadName:{}", Cons.RedisLock.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
//                iOrderService.closeOrder(PropertiesUtil.getIntegerProperty("close.order.task.time.hour","2"));
            }else {
                log.info("Redisson获取分布式锁失败:{},ThreadName:{}", Cons.RedisLock.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("Redisson分布式锁获取异常",e);
        }finally {
            if (tryLock) {
                lock.unlock();
            }
        }
    }



    private void closeOrder() {
        ShardRedisPoolUtil.expire(Cons.RedisLock.CLOSE_ORDER_TASK_LOCK, 50);
        log.info("获取{},ThreadName:{}", Cons.RedisLock.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        iOrderService.closeOrder(PropertiesUtil.getIntegerProperty("close.order.task.time.hour","2"));
        Long delOrderTaskLock = ShardRedisPoolUtil.del(Cons.RedisLock.CLOSE_ORDER_TASK_LOCK);
        if (delOrderTaskLock != null && delOrderTaskLock.intValue() == 0) {
            log.error("释放锁失败{},ThreadName:{}", Cons.RedisLock.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        }
        log.info("释放{},ThreadName:{}", Cons.RedisLock.CLOSE_ORDER_TASK_LOCK,Thread.currentThread().getName());
        log.info("====================================================");
    }
}
