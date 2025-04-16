package com.jz.framework.interceptor;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * API 请求拦截器
 * 用于处理所有 API 请求的通用逻辑：
 * 1. 验证请求头中的 API Key
 * 2. 处理 ping/pong 心跳请求
 * 3. 缓存请求体供后续使用
 */
@Component
public class ApiInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String contentType = request.getContentType();
        if (contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer ")) {
                String apiKey = authorization.substring(7);
                // TODO: 验证 apiKey
            }

            String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            Map<String, Object> requestBody = objectMapper.readValue(body, Map.class);

            if ("ping".equals(requestBody.get("point"))) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{\"result\":\"pong\"}");
                return false;
            }

            request.setAttribute("requestBody", body);
        }
        return true;
    }
}