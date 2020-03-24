package com.hqq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Consumer {
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

        //指定交换机名、类型及交换机和队列的绑定关系
        String exchangeName = "test_confirm_exchange";
        String type="topic";
        String bindKey = "confirm.#";
        String queueName = "test_confirm_queue";
        //4 声明交换机
        channel.exchangeDeclare(exchangeName, type, true);
        //5.声明队列
        channel.queueDeclare(queueName, true, false, false, null);
        //6.建立交换机和队列的绑定关系
        channel.queueBind(queueName, exchangeName, bindKey);

        //5 创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        //6 设置Channel
        channel.basicConsume(queueName, true, queueingConsumer);

        //7 这里我使用了一个while循环去轮询消息
        while(true){
            //8 获取消息
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.err.println("消费端: " + msg);
            //Envelope envelope = delivery.getEnvelope();
        }

    }
}
