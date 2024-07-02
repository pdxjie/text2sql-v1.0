package com.pdx.service;

import com.pdx.model.entity.Model;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/18
 * @Description: DO AnyThing...
 */
public interface OpenAiProvider {


    public SseEmitter chatCommonRequest(String prompt, boolean stream, Model activeModel);

}
