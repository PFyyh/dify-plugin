package com.jz.modules.download.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jz.modules.download.model.TextRequest;
import com.jz.modules.download.service.AudioService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 音频处理控制器
 * 处理文本转语音请求和音频文件下载
 */
@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class DownloadController {

    /** 音频服务 */
    private final AudioService audioService;

    /**
     * 处理文本转语音请求
     * 
     * @param request 包含name和text的请求体
     * @return 返回音频文件的下载地址
     */
    @PostMapping("/text")
    public ResponseEntity<?> processText(@RequestBody TextRequest request) {
        try {
            String downloadUrl = audioService.processAudioRequest(request);
            // 将下载URL包装在Map中返回
            Map<String, String> response = new HashMap<>();
            response.put("downloadUrl", downloadUrl);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // 发生异常时返回500错误
            return ResponseEntity.internalServerError().body("Error processing request: " + e.getMessage());
        }
    }

    /**
     * 处理音频文件下载请求s
     * 
     * @param fileName 要下载的文件名
     * @return 音频文件资源
     */
    @GetMapping("/audio/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            // 构建文件路径
            Path filePath = Paths.get(System.getProperty("user.dir"), "audio-files", fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                // 文件存在时，设置响应头并返回文件
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                        .body(resource);
            }
            // 文件不存在时返回404
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // 发生异常时返回500错误
            return ResponseEntity.internalServerError().build();
        }
    }

}