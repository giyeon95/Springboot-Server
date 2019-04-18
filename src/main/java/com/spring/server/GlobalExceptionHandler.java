package com.spring.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

     public static final Log logger = LogFactory.getLog(GlobalExceptionHandler.class );

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        logger.error(e.toString());
        return e.getMessage();
    }
}
