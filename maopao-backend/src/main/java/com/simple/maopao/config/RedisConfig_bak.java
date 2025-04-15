package com.simple.maopao.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author Simple
 */
//@Configuration
//@EnableRedisHttpSession
public class RedisConfig_bak {

//    // 自定义 ObjectMapper，禁用 @class 字段
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
//        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        objectMapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
//        // 关键：禁用所有类型信息写入
//        objectMapper.deactivateDefaultTyping();
//        return objectMapper;
//    }
//
//
//    // 使用无类型信息的 GenericJackson2JsonRedisSerializer
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory, ObjectMapper objectMapper) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(factory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
//        template.afterPropertiesSet();
//        return template;
//    }
//
//    // 强制 Spring Session 使用无类型信息的序列化器
//    @Bean(name = "springSessionDefaultRedisSerializer")
//    public RedisSerializer<Object> springSessionDefaultRedisSerializer(ObjectMapper objectMapper) {
//        return new GenericJackson2JsonRedisSerializer(objectMapper);
//    }
}