package com.lzq.redispushchannel.service;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author lzq   布隆过滤器
 * @version 1.0
 * @date 2022/1/20 15:14
 */
@Service
public class BloomFilterService {

    private BloomFilter<Long> bf;

    /**
     * 创建布隆过滤器
     *
     * @PostConstruct：程序启动时候加载此方法
     */
    @PostConstruct
    public void initBloomFilter() {
        //创建布隆过滤器(默认3%误差)
        bf = BloomFilter.create(Funnels.longFunnel(), 1000000,0.01);
        for (int i = 0; i < 1000000; i++) {
            bf.put(Long.valueOf(i));
        }
    }

    /**
     * 判断id可能存在于布隆过滤器里面
     *
     * @param id
     * @return
     */
    public boolean testIdExists(Long id) {
        return bf.mightContain(id);
    }
}

