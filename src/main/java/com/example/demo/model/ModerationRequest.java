package com.example.demo.model;

import lombok.Data;

/**
 * 内容审核请求模型
 * 
 * 例子
 * {
 * "point": "app.moderation.input",
 * "params": {
 * "app_id": "61248ab4-1125-45be-ae32-0ce91334d021",
 * "inputs": {
 * "var_1": "I will kill you.",
 * "var_2": "I will fuck you."
 * },
 * "query": "Happy everydays."
 * }
 * }
 */
@Data
public class ModerationRequest {
    /** 扩展点类型，固定为 app.moderation.input */
    private String point;
    /** 请求参数 */
    private ModerationParams params;
}

