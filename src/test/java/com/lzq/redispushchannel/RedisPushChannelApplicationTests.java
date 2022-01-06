package com.lzq.redispushchannel;

import com.alibaba.fastjson.JSONObject;
import com.lzq.redispushchannel.mapper.GameDescMapper;
import com.lzq.redispushchannel.po.GameDesc;
import com.lzq.redispushchannel.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisPushChannelApplicationTests {
    @Autowired
    private RedisService redisService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GameDescMapper descMapper;

    @Test
    public void game(){
        List<GameDesc> list =new ArrayList<>();
        for (int i = 2020001; i <= 2020150; i++) {
            String forEntity = restTemplate.getForObject("https://match.lottery.sina.com.cn/client/index/clientProxy/?format=json&__caller__=wap&__version__=1&__verno__=1&cat1=gameCurrentInfo&gameCode=101&issueNo="+i+"&t=1641451696962", String.class);
            JSONObject json = JSONObject.parseObject(forEntity);
            JSONObject result = JSONObject.parseObject(json.getString("result"));
            JSONObject status = JSONObject.parseObject( result.getString("status"));

            if (Integer.valueOf(status.get("code").toString()) == 0){
                GameDesc desc = new GameDesc();
                JSONObject data = JSONObject.parseObject(result.getString("data"));
                desc.setIssueNo(data.get("issue_no").toString());
                desc.setBlueResult(data.get("blue_result").toString());
                desc.setRedResult(data.get("red_result").toString());
                list.add(desc);
            }

        }
        System.out.println(list.toString());
        if (!list.isEmpty()){
            descMapper.insertList(list);
        }
    }
    @Test
    public void setHashData(){
        redisService.hset("test","lzq","15903031938");
        redisService.hset("test","lzq1","15903031938");
        System.out.println(redisService.hmget("test"));
    }
}
