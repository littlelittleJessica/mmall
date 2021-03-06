package com.mmall.service.Impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Description
 * @Author Jessica
 * @Version v
 * @Date 2021/10/3
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 上传图片
     */
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID() + "." + fileExtensionName;
        LOG.info("开始上传文件,上传文件的文件名:{},上传的路径:{},新文件名:{}", fileName, path, uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);

        try {
            file.transferTo(targetFile);
            //文件上传成功

            //将targetFile上传到FTP服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));

            //上传完之后删除upload下的文件
            targetFile.delete();
        } catch (IOException e) {
            LOG.error("上传文件异常", e);
            return null;
        }
        return targetFile.getName();
    }
}
