package com.orange_yishenggong.wiki.service;

import com.orange_yishenggong.wiki.websocket.WebSocketServer;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;


public class WsService {

    @Resource
    public WebSocketServer webSocketServer;

    @Async
    public void sendInfo(String message, String logId) {
        MDC.put("LOG_ID", logId);
        webSocketServer.sendInfo(message);
    }
}
