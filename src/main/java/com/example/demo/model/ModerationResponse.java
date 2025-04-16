package com.example.demo.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 内容审核响应模型
 * 
 * 例子
 * 
 * action=direct_output
 * {
 * "flagged": true,
 * "action": "direct_output",
 * "preset_response": "Your content violates our usage policy."
 * }
 * 
 * 
 * action=overridden
 * {
 * "flagged": true,
 * "action": "overridden",
 * "text": "I will *** you."
 * }
 */
@Data
public class ModerationResponse {
    /** 是否违反校验规则 */
    private boolean flagged;

    /** 动作类型：direct_output(直接输出预设回答) 或 overridden(覆写传入变量值) */
    private String action;

    /** 预设回答（仅当 action=direct_output 时返回） */
    @JsonProperty("preset_response")
    private String presetResponse;

    /** 覆写的输出内容（仅当 action=overridden 时返回） */
    private String text;

    /** 终端用户传入变量值，key为变量名，value为变量值（仅当 action=overridden 时返回） */
    private Map<String, String> inputs;

    /** 覆写的终端用户当前对话输入内容，对话型应用固定参数（仅当 action=overridden 时返回） */
    private String query;
}