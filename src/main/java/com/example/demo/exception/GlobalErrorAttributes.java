package com.example.demo.exception;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Throwable exception = getError(request);
        if (exception instanceof CoreException) {
            Map<String, Object> map = super.getErrorAttributes(request, includeStackTrace);
            map.put("statusCode", ((CoreException) exception).getHttpStatus().value());
            map.put("status", ((CoreException) exception).getHttpStatus());
            map.put("message", exception.getMessage());
            map.put("error", ((CoreException) exception).getHttpStatus().getReasonPhrase());

            return map;
        }

        Map<String, Object> map = super.getErrorAttributes(request, includeStackTrace);
        map.put("statusCode", HttpStatus.BAD_REQUEST.value());
        map.put("status", HttpStatus.BAD_REQUEST);
        map.put("message", exception.getMessage());
        return map;
    }
}
