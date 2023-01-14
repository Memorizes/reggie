package com.nuist.controller;

import com.nuist.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    String basePath;

    /**
     * 将图片上传至服务器
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        String oriName = file.getOriginalFilename();
        String suffix = oriName.substring(oriName.lastIndexOf("."));
        String newName = UUID.randomUUID().toString() + suffix;

        File filePath = new File(basePath);
        if(!filePath.exists()) {
            filePath.mkdirs();
        }
        File newFile = new File(basePath + newName);
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.success(newName);
    }

    /**
     * 将图片从服务器下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            FileInputStream fs = new FileInputStream(basePath + name);
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while((len = fs.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
