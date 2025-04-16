package com.jz.modules.download.model;



import java.util.Map;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

/**
 * 内容审核参数模型
 */
@Data
public class ModerationParams {
    /** 应用ID */
    private String appId;

    /** 终端用户传入变量值，key为变量名，value为变量值 */
    @NotEmpty(message = "inputs不能为空")
    private Map<String, String> inputs;

    /** 终端用户当前对话输入内容，对话型应用固定参数 */
    private String query;

    /** LLM 回答内容。当 LLM 输出为流式时，此处为 100 字为一个分段的内容 */
    private String text;
}