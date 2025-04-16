package com.example.demo.model;

import lombok.Data;

@Data
public class TextRequest {

    // 识别文字
    private String text;
    // 音色
    private String role;
    // 输出文件名字
    private String reference;
}