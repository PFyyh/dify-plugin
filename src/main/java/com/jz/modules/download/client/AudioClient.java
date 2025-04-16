package com.jz.modules.download.client;


import com.dtflys.forest.annotation.PostRequest;
import com.jz.modules.download.model.TextRequest;
import com.dtflys.forest.annotation.Body;

public interface AudioClient {
    @PostRequest(
        url = "${audioApiUrl}",
        contentType = "application/x-www-form-urlencoded"
    )
    byte[] getAudio(@Body TextRequest request);
}