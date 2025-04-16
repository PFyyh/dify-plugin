package com.example.demo.client;

import com.dtflys.forest.annotation.PostRequest;
import com.dtflys.forest.annotation.Body;
import com.example.demo.model.TextRequest;

public interface AudioClient {
    @PostRequest(
        url = "${audioApiUrl}",
        contentType = "application/x-www-form-urlencoded"
    )
    byte[] getAudio(@Body TextRequest request);
}