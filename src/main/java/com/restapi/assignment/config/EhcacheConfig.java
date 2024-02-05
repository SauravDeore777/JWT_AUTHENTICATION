package com.restapi.assignment.config;

import com.restapi.assignment.entity.Employee;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;

@Configuration
@EnableCaching
@EnableScheduling
public class EhcacheConfig {
    private static Logger log =  LoggerFactory.getLogger(EhcacheConfig.class);
    @Bean
    public CacheManager getcacheManager(){
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager =  provider.getCacheManager();

      CacheConfiguration<Integer, Employee> configuration =  CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class,Employee.class,ResourcePoolsBuilder.heap(100)
                .offheap(10, MemoryUnit.MB))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(10))).build();
      log.info("cache expired");

       javax.cache.configuration.Configuration<Integer, Employee> cacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(configuration);

       cacheManager.createCache("findById",cacheConfiguration);

       return  cacheManager;





    }

}
