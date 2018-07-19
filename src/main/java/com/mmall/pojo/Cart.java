package com.mmall.pojo;

import lombok.*;

import java.util.Date;

/**
 * @author zs595
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private Integer checked;

    private Date createTime;

    private Date updateTime;

}