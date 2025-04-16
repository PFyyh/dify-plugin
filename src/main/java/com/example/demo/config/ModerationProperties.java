package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "moderation")
public class ModerationProperties {
    private Messages messages;
    private List<String> sensitiveWords;

    @Data
    public static class Messages {
        private String harmfulContent;
    }
}