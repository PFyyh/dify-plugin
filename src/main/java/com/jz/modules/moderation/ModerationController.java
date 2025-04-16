package com.jz.modules.moderation;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jz.framework.config.ModerationProperties;
import com.jz.modules.download.model.ModerationRequest;
import com.jz.modules.download.model.ModerationResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 内容审核控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/moderation")
public class ModerationController {
    @Autowired
    private ModerationProperties moderationProperties;

    /**
     * 内容审核接口
     * 检查用户输入是否包含敏感词，并根据审核结果返回相应处理方案
     * 
     * @param request 审核请求，包含需要检查的文本内容
     * @return 返回审核结果，包含以下情况：
     *         1. 当没有敏感内容时，返回 flagged=false
     *         2. 当有敏感内容且需要替换时，返回 action=overridden 和替换后的内容
     *         3. 当有敏感内容且需要拒绝时，返回 action=direct_output 和预设回复
     */
    @PostMapping("/input")
    public ModerationResponse moderateContent(@Valid @RequestBody ModerationRequest request) {
        log.info("接收到审核请求: {}", request);
        try {
            if ("app.moderation.input".equals(request.getPoint())) {
                ModerationResponse response = new ModerationResponse();
                response.setAction("direct_output");
                response.setPresetResponse("无异常");
                // 检查 inputs 和 query 是否包含敏感词
                Map<String, String> inputs = request.getParams().getInputs();
                String query = request.getParams().getQuery();
                boolean containsSensitiveContent = false;

                // 检查 inputs
                if (inputs != null && !inputs.isEmpty()) {
                    containsSensitiveContent = inputs.values().stream()
                            .anyMatch(value -> moderationProperties.getSensitiveWords().stream()
                                    .anyMatch(word -> value.toLowerCase().contains(word.toLowerCase())));
                }

                // 检查 query
                if (!containsSensitiveContent && query != null && !query.isEmpty()) {
                    containsSensitiveContent = moderationProperties.getSensitiveWords().stream()
                            .anyMatch(word -> query.toLowerCase().contains(word.toLowerCase()));
                }

                response.setFlagged(containsSensitiveContent);

                if (containsSensitiveContent) {
                    if (shouldOverrideContent(inputs)) {
                        // 替换敏感词的情况
                        response.setAction("overridden");

                        // 处理 inputs
                        Map<String, String> sanitizedInputs = new HashMap<>();
                        if (inputs != null) {
                            inputs.forEach((key, value) -> {
                                String sanitizedValue = value;
                                for (String word : moderationProperties.getSensitiveWords()) {
                                    sanitizedValue = sanitizedValue.replaceAll("(?i)" + word, "***");
                                }
                                sanitizedInputs.put(key, sanitizedValue);
                            });
                        }
                        response.setInputs(sanitizedInputs);

                        // 处理 query
                        if (query != null && !query.isEmpty()) {
                            String sanitizedQuery = query;
                            for (String word : moderationProperties.getSensitiveWords()) {
                                sanitizedQuery = sanitizedQuery.replaceAll("(?i)" + word, "***");
                            }
                            response.setQuery(sanitizedQuery);
                        }
                    } else {
                        // 直接拒绝的情况
                        response.setAction("direct_output");
                        response.setPresetResponse(moderationProperties.getMessages().getHarmfulContent());
                    }
                }
                log.info("审核响应: {}", response);
                return response;
            } else if ("app.moderation.output".equals(request.getPoint())) {
                ModerationResponse response = new ModerationResponse();
                String text = request.getParams().getText();

                // 检查输出内容是否包含敏感词
                boolean containsSensitiveContent = false;
                if (text != null && !text.isEmpty()) {
                    containsSensitiveContent = moderationProperties.getSensitiveWords().stream()
                            .anyMatch(word -> text.toLowerCase().contains(word.toLowerCase()));
                }

                response.setFlagged(containsSensitiveContent);

                if (containsSensitiveContent) {
                    if (shouldOverrideContent(null)) { // 可以根据需要修改判断逻辑
                        // 替换敏感词的情况
                        response.setAction("overridden");
                        String sanitizedText = text;
                        for (String word : moderationProperties.getSensitiveWords()) {
                            sanitizedText = sanitizedText.replaceAll("(?i)" + word, "***");
                        }
                        response.setText(sanitizedText);
                    } else {
                        // 直接拒绝的情况
                        response.setAction("direct_output");
                        response.setPresetResponse(moderationProperties.getMessages().getHarmfulContent());
                    }
                }

                return response;
            }

            return new ModerationResponse();
        } catch (Exception e) {
            log.error("审核请求处理失败", e);
            return new ModerationResponse();
        }
    }

    /**
     * 判断是否应该覆写内容而不是直接拒绝
     */
    private boolean shouldOverrideContent(Map<String, String> inputs) {
        // TODO 这里可以添加更复杂的判断逻辑
        // return inputs != null && inputs.size() > 1;
        return false;
    }
}
