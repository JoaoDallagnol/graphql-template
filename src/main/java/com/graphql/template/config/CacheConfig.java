package com.graphql.template.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {


    // Global approach
//    @Bean
//    public CacheManager cacheManager() {
//        CaffeineCacheManager manager = new CaffeineCacheManager();
//        //maximumSize=500, expire 10 minutes after write
//        manager.setCaffeineSpec(
//                CaffeineSpec.parse("maximumSize=500,expireAfterWrite=10m")
//        );
//        return manager;
//    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.registerCustomCache("author",
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(5, TimeUnit.MINUTES)
                        .recordStats()
                        .build());

        manager.registerCustomCache("authorBatch",
                Caffeine.newBuilder()
                        .maximumSize(200)
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .recordStats()
                        .build());

        manager.registerCustomCache("book",
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(2, TimeUnit.MINUTES)
                        .recordStats()
                        .build());

        return manager;
    }
}
