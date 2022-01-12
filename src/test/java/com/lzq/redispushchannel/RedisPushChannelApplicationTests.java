package com.lzq.redispushchannel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzq.redispushchannel.mapper.GameDescMapper;
import com.lzq.redispushchannel.po.GameDesc;
import com.lzq.redispushchannel.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    /*****************************************    排行榜操作    *****************************************/
    public static final String SCORE_RANK = "score_rank";
    /**
     * 批量新增
     */
    @Test
    public void batchAdd() {
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            DefaultTypedTuple<String> tuple = new DefaultTypedTuple<>("张三" + i, 1D + i);
            tuples.add(tuple);
        }
        System.out.println("循环时间:" +( System.currentTimeMillis() - start));
        Long num = redisService.zSet(SCORE_RANK, tuples);
        System.out.println("批量新增时间:" +(System.currentTimeMillis() - start));
        System.out.println("受影响行数：" + num);
    }
    /**
     * 获取排行列表
     */
    @Test
    public void list() {
        Set<String> range = redisService.ranges(SCORE_RANK, 0L, 9L);
        System.out.println("获取到的排行列表:" + JSON.toJSONString(range));
        Set<ZSetOperations.TypedTuple<String>> rangeWithScores = redisService.getRanks(SCORE_RANK, 0L, 9L);
        System.out.println("获取到的排行和分数列表:" + JSON.toJSONString(rangeWithScores));
    }
    /**
     * 单个新增
     */
    @Test
    public void add() {
        redisService.zSet(SCORE_RANK, "lzq", 999999D);
    }

    /**
     * 给用户加分
     */
    @Test
    public void incrementScore(){
        redisService.incrementScore(SCORE_RANK,"lzq",1D);
    }
    /**
     * 获取单个的排行
     */
    @Test
    public void find(){
        Long rankNum = redisService.getOneRank(SCORE_RANK, "lzq");
        System.out.println("lzq的个人排名：" + (rankNum+1));

        Double score = redisService.getOneScore(SCORE_RANK, "lzq");
        System.out.println("lzq的分数:" + score);
    }
}
