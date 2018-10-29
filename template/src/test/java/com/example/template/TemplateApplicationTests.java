package com.example.template;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TemplateApplicationTests {

    /**
     * rabbitmq 给rabbitmq 收发消息
     */
    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * amqpTemplate 给amqp中间件 收发消息， 推荐使用
     */
    @Autowired
    AmqpTemplate amqpTemplate;

    /**
     * 交换器、队列管理
     * amqp系统管理功能组件
     */
    @Autowired
    AmqpAdmin amqpAdmin;

    /**
     * 第一步：
     * 使用amqpAdmin初始化。创建交换器，队列，以及交换器绑定队列
     * 执行完成。访问  http://192.168.9.219:15672/#/exchanges/%2F/exchange.direct 可以查看具体信息
     */
    @Test
    public void init() {
        /**
         * exchange 类型主要分DirectExchange FanoutExchange TopicExchange HeadersExchange
         * 具体区别更多详情请查看 https://blog.csdn.net/belonghuang157405/article/details/83184388 关于amqp相关内容
         */

        //创建交换器 点对点模式
        amqpAdmin.declareExchange(new DirectExchange("exchange.direct"));
        //创建队列 queue第二个参数：是否持久化
        amqpAdmin.declareQueue(new Queue("direct-queue",true));
        //交换器绑定队列
        amqpAdmin.declareBinding(new Binding("direct-queue", Binding.DestinationType.QUEUE,"exchange.direct","fruit.apple",null));

    }


    /**
     * 第二步：
     * 发送消息
     */
    @Test
    public void sendMessage(){

        //第一种.使用send 需要自己构造一个Message，定义消息内容以及消息头
        //rabbitTemplate.send("exchange.direct","fruit.apple",new Message("apple-message-made".getBytes(),null));

        //第二种.使用convertAndSend  第三个参数object默认当初消息体，自动序列化发送给rabbitmq
        HashMap<Object, Object> map = new HashMap<>();
        map.put("type","red apple");
        map.put("data", Arrays.asList(1,2,3));
        // convertAndSend 使用的转换器是SimpleMessageConverter 会采用jdk序列方式，
        // 但往往大多数会想使用json方式，故多编写一个配置类:AmqpConfig
        // 这时候再去http://192.168.9.219:15672/#/queues/%2F/direct-queue界面上Get messages时候查看消息就是json格式
        rabbitTemplate.convertAndSend("exchange.direct","fruit.apple",map);

    }

    /**
     * 第三步：
     * 接收消息
     */
    @Test
    public void receiveMessage(){

        Object message = rabbitTemplate.receiveAndConvert("direct-queue");
        System.out.println(message.getClass());
        System.out.println(message.toString());

    }

}
