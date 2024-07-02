package com.pdx.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pdx.model.chat.ChatMessage;
import com.pdx.model.chat.ChatParams;
import com.pdx.model.constants.ChatRoleConstant;
import com.pdx.model.entity.Model;
import com.pdx.service.OpenAiProvider;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: IT 派同学
 * @Date: 2024/5/18
 * @Description: OpenAI 接口调用封装
 */
@Slf4j
@Service
public class OpenAiProviderImpl implements OpenAiProvider {

    @Value("${openai.variables.model}")
    private String model;

    @Value("${openai.variables.apiKey}")
    private String apiKey;

    @Value("${openai.remote-url}")
    private String remoteUrl;

    /**
     * 通用聊天请求
     * @param prompt 指令
     * @param stream 是否流式输出
     * @return SseEmitter
     */
    @Override
    public SseEmitter chatCommonRequest(String prompt, boolean stream, Model activeModel) {
        final SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        // 封装聊天信息
        ChatParams chatParams = new ChatParams();
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage message = new ChatMessage();
        message.setContent(prompt);
        message.setRole(ChatRoleConstant.USER);
        messages.add(message);
        chatParams.setMessages(messages);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        // 封装 OpenAi 接口请求参数
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("messages", messages);
        bodyJson.put("model", activeModel.getAssistantId());
        bodyJson.put("temperature", 0.7);
        bodyJson.put("top_p", 1);
        bodyJson.put("stream", true);
        RequestBody requestBody = RequestBody.create(mediaType, JSONUtil.toJsonStr(bodyJson));
        log.info("params --> {}", JSONUtil.toJsonStr(chatParams));
        Request request = new Request.Builder()
                .url(activeModel.getRemoteUrl())
                .post(requestBody) // 请求体
                .addHeader("Authorization", "Bearer " + activeModel.getAuthKey())
                .addHeader("Accept", "text/event-stream")
                .build();

        // 开启 Http 客户端
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.DAYS)   // 建立连接的超时时间
                .readTimeout(1, TimeUnit.DAYS)  // 建立连接后读取数据的超时时间
                .build();

        // 实例化EventSource，注册EventSource监听器 -- 创建一个用于处理服务器发送事件的实例，并定义处理事件的回调逻辑
        RealEventSource realEventSource = new RealEventSource(request, new EventSourceListener() {

            @Override
            public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
                log.info("onOpen");
            }

            @Override
            public void onEvent(@NotNull EventSource eventSource, String id, String type, String data) {
                try {
                    JSONObject resultJson = JSONObject.parseObject(data);
                    Object choices = resultJson.get("choices");
                    String result = "";
                    if (!Objects.isNull(choices)) {
                        JSONArray jsonArray = JSONObject.parseArray(choices.toString());
                        result = jsonArray.stream().findFirst().orElse(null).toString();
                    }
                    log.info("result --> {}", result);
                    sseEmitter.send(result);
                } catch (IOException e) {
                    log.error("onEvent error {}", data);
                    sseEmitter.complete();
                    throw new RuntimeException(e);
                }
                // 消息类型，add 增量，finish 结束，error 错误，interrupted 中断
                if ("finish".equals(type)) {
                    sseEmitter.complete();
                }
            }

            @Override
            public void onClosed(@NotNull EventSource eventSource) {
                sseEmitter.complete();
                log.info("onClosed");
            }

            @Override
            public void onFailure(@NotNull EventSource eventSource, Throwable t, Response response) {
                log.error("onEvent error", response);
                sseEmitter.complete();
            }
        });
        // 与服务器建立连接
        realEventSource.connect(okHttpClient);
        return sseEmitter;
    }
}
