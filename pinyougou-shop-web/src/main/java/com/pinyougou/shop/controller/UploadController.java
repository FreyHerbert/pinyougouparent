package com.pinyougou.shop.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

/**
 * 获取文件控制器
 * @author leiyu
 */
@RestController
public class UploadController {
    @Value("${FILE_SERVER_URL}")
    private String fileServerUrl;

    @RequestMapping("/upload")
    public Result upload(MultipartFile file) {
        Result result = new Result();
        String originalFilename = file.getOriginalFilename();
        String exName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        try {
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
            String fileId = client.uploadFile(file.getBytes(), exName);
            String url = fileServerUrl + fileId;
            result.setSuccess(true);
            result.setMessage(url);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("文件上传失败！！");
            return result;
        }
    }
}
