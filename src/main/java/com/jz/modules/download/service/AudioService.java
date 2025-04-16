package com.jz.modules.download.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jz.modules.download.client.AudioClient;
import com.jz.modules.download.model.TextRequest;

import lombok.RequiredArgsConstructor;

/**
 * 音频服务
 * 处理音频文件的生成、存储和清理
 */
@Service
@RequiredArgsConstructor
public class AudioService {
    /** 音频文件存储路径 */
    @Value("${audio.storage.path}")
    private String storagePath;

    /** Forest HTTP 客户端 */
    private final AudioClient audioClient;

    /** 文件过期时间映射表，key为文件名，value为过期时间戳 */
    private final Map<String, Long> fileExpiryMap = new ConcurrentHashMap<>();

    /**
     * 处理音频请求
     * 将文本转换为音频文件并临时存储
     *
     * @param request 包含文本内容的请求对象
     * @return 音频文件的下载URL
     * @throws IOException 当文件操作失败时抛出
     */
    public String processAudioRequest(TextRequest request) throws IOException {
        // 调用外部API获取音频数据
        byte[] audioData = audioClient.getAudio(request);

        // 生成唯一的文件名
        String fileName = UUID.randomUUID().toString() + ".mp3";
        Path filePath = Paths.get(storagePath, fileName);

        // 创建存储目录并写入文件
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, audioData);

        // 设置文件5分钟后过期
        fileExpiryMap.put(fileName, System.currentTimeMillis() + 300000);

        // 返回文件下载地址
        return "/api/audio/download/" + fileName;
    }

    /**
     * 清理过期文件
     * 每分钟执行一次，删除已过期的音频文件
     */
    @Scheduled(fixedRate = 60000)
    public void cleanupExpiredFiles() {
        long currentTime = System.currentTimeMillis();
        fileExpiryMap.forEach((fileName, expiryTime) -> {
            if (currentTime > expiryTime) {
                Path filePath = Paths.get(storagePath, fileName);
                try {
                    // 删除过期文件并移除记录
                    Files.deleteIfExists(filePath);
                    fileExpiryMap.remove(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}