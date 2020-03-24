package com.hqq;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Producer {
    public static void main(String[] args) throws Exception {

        //1 创建一个ConnectionFactory, 并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // host就是你的RabbitMQ所在的虚拟机地址，我这里是192.168.11.76；默认端口是5672
        connectionFactory.setHost("192.168.1.150");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3 通过connection创建一个Channel
        Channel channel = connection.createChannel();

        //4.开启确认模式的消息投递
        channel.confirmSelect();

        //5.指定交换机名及路由
        String exchangeName = "test_confirm_exchange";
        String routingKey = "return.confirm.save";
        String routingKeyError = "abc.save";

        //6. 通过Channel发送消息，这里我发送了5条消息哦
        for(int i=0; i < 5; i++){
            String msg = "Hello RabbitMQ!第"+i+"次了";

            //添加return监听器  用于处理消息路由不到队列的情况
            channel.addReturnListener(new ReturnListener() {
                @Override
                public void handleReturn(int replyCode, String replyText, String exchange,
                                         String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    // 这是broker把消息原路返回后提供的一些响应参数，供生产端去做后续的处理
                    System.out.println("---------handle  return----------");
                    System.out.println("replyCode: " + replyCode);
                    System.out.println("replyText: " + replyText);
                    System.out.println("exchange: " + exchange);
                    System.out.println("routingKey: " + routingKey);
                    System.out.println("properties: " + properties);
                    System.out.println("body: " + new String(body));
                }
            });
            //1 exchange   2 routingKey
            //channel.basicPublish(exchangeName,routingKey,null,msg.getBytes());
            channel.basicPublish(exchangeName, routingKeyError, true, null, msg.getBytes());
            //添加确认发送模式下的监听,每发一次，监听一下
            /*channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long l, boolean b) throws IOException {
                    System.out.println("收到了broker发来的ack");
                }

                @Override
                public void handleNack(long l, boolean b) throws IOException {
                    System.out.println("没收到broker发来的ack");
                }
            });*/
        }
        //7. 记得要关闭相关的连接
        channel.close();
        connection.close();
    }
}
