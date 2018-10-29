package com.example.annotation.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 接收消息队列消息
 */
@Service
public class MessageService {

    @RabbitListener(queues = "fanout-queue1")
    public void receiveQueue1Message(HashMap<String,Object> map){
        System.out.println("fanout-queue1 begin");
        Object  message = map.getOrDefault("data", "no message");
        System.out.println(message.toString());
        System.out.println("fanout-queue1 end");
    }

    @RabbitListener(queues = "fanout-queue2")
    public void receiveQueue2Message(HashMap<String,Object> map){
        System.out.println("fanout-queue2 begin");
        Object  message = map.getOrDefault("data", "no message");
        System.out.println(message.toString());
        System.out.println("fanout-queue2 end");
    }

    @RabbitListener(queues = "fanout-queue3")
    public void receiveQueue3Message(HashMap<String,Object> map){
        System.out.println("fanout-queue3 begin");
        Object  message = map.getOrDefault("data", "no message");
        System.out.println(message.toString());
        System.out.println("fanout-queue3 end");
    }


}
