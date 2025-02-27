package com.sky.config;

/**
@author 帅的被人砍
@create 2025-02-27 13:31
*/

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ配置类
 */
@Configuration
public class RabbitMQConfig {

    public static final String ORDER_TIMEOUT_EXCHANGE = "order-timeout-exchange";
    public static final String ORDER_TIMEOUT_QUEUE = "order-timeout-queue";
    public static final String ORDER_TIMEOUT_ROUTING_KEY = "order-timeout-routing-key";

    public static final String DEAD_LETTER_EXCHANGE = "dead-letter-exchange";
    public static final String DEAD_LETTER_QUEUE = "dead-letter-queue";
    public static final String DEAD_LETTER_ROUTING_KEY = "dead-letter-routing-key";

    // 订单超时交换机
    @Bean
    public DirectExchange orderTimeoutExchange() {
        return new DirectExchange(ORDER_TIMEOUT_EXCHANGE);
    }

    // 订单超时队列，配置死信交换机和死信路由键
    @Bean
    public Queue orderTimeoutQueue() {
        Map<String, Object> args = new HashMap<>();
        // 设置死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // 设置死信路由键
        args.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        return new Queue(ORDER_TIMEOUT_QUEUE, true, false, false, args);
    }

    // 订单超时绑定
    @Bean
    public Binding orderTimeoutBinding() {
        return BindingBuilder.bind(orderTimeoutQueue())
                .to(orderTimeoutExchange())
                .with(ORDER_TIMEOUT_ROUTING_KEY);
    }

    // 死信交换机
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    // 死信队列
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    // 死信绑定
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DEAD_LETTER_ROUTING_KEY);
    }
}
