package com.example.services.util;

import com.example.services.Constants;
import io.scalecube.services.api.ServiceMessage;

public class ServiceMessageUtil {

    public static ServiceMessage toServiceMessage(String qualifier, Object data) {
        return ServiceMessage.builder()
            .qualifier(qualifier)
            .dataFormat(Constants.CONTENT_TYPE)
            .data(data)
            .build();
    }

    public static ServiceMessage toServiceMessage(Object data) {
        return ServiceMessage.builder()
            .dataFormat(Constants.CONTENT_TYPE)
            .data(data)
            .build();
    }

}
