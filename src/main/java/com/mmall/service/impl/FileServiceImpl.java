package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Demo class
 *
 * @author keriezhang
 * @date 2016/10/31
 */
@Service("iFileService")
@Slf4j
public class FileServiceImpl implements IFileService {

    public static final String OS_LINUX = "linux";

    @Override
    public String upload(MultipartFile file, String path) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            String fileExtensionName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //避免用户上传的filename重复
            String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
            log.info("开始上传文件,上传文件的文件名:{},上传文件的路径:{},新文件名:{}",originalFilename,path,uploadFileName);
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                String osName = System.getProperty("os.name");
                if (osName.contains(OS_LINUX)) {
                    boolean isWritable = fileDir.setWritable(true);
                    if (!isWritable) {
                        log.error("linux下创建文件失败");
                    }
                }
                boolean isCreateSuccess = fileDir.mkdirs();
                if (!isCreateSuccess) {
                    log.error("文件或文件夹创建失败");
                }
            }
            File targetFile = new File(path, uploadFileName);
            try {
                file.transferTo(targetFile);
                //上传至ftp服务器
                boolean isUploadFileSuccess = FTPUtil.uploadFile(Lists.newArrayList(targetFile));
                if (!isUploadFileSuccess) {
                    return null;
                }
                boolean isDeleteSuccess = targetFile.delete();
                if (!isDeleteSuccess) {
                    log.error("删除上传文件失败");
                }
            } catch (IOException e) {
                log.error("上传文件异常",e);
                return null;
            }
            return targetFile.getName();
        }
        return null;
    }
}
