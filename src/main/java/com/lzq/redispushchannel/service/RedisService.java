package com.lzq.redispushchannel.service;

import com.lzq.redispushchannel.config.RedisCacheContants;
import com.lzq.redispushchannel.po.LikedCountDTO;
import com.lzq.redispushchannel.po.UserLike;
import com.lzq.redispushchannel.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    // =============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return stringRedisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                stringRedisTemplate.delete(key[0]);
            } else {
                stringRedisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public String get(final String key) {
        return key == null ? null : (String) stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(final String key, final String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(final String key, final String value, final long time) {
        try {
            if (time > 0) {
                stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(final String key, final long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(final String key, final long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return stringRedisTemplate.opsForValue().increment(key, -delta);
    }

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public String hget(final String key, final String item) {
        return (String) stringRedisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<String, String> hmget(final String key) {
        return (Map) stringRedisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(final String key, final Map<String, String> map) {
        try {
            stringRedisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(final String key, final Map<String, String> map, final long time) {
        try {
            stringRedisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(final String key, final String item, final String value) {
        try {
            stringRedisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(final String key, final String item, final String value, final long time) {
        try {
            stringRedisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(final String key, final String... item) {
        stringRedisTemplate.opsForHash().delete(key, item);
    }


    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(final String key, final String item) {
        return stringRedisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(final String key, final String item, final double by) {
        return stringRedisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(final String key, final String item, final double by) {
        return stringRedisTemplate.opsForHash().increment(key, item, -by);
    }
    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<String> sGet(final String key) {
        try {
            return stringRedisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Set<String> sDiff(final String key,String key1) {
        try {
            return stringRedisTemplate.opsForSet().difference(key,key1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(final String key, final String value) {
        try {
            return stringRedisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(final String key, final String... values) {
        try {
            return stringRedisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(final String key, final long time, final String... values) {
        try {
            Long count = stringRedisTemplate.opsForSet().add(key, values);
            if (time > 0)
                expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(final String key) {
        try {
            return stringRedisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(final String key, final String... values) {
        try {
            Long count = stringRedisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将数据放入zset中
     *
     * @param key    redis key
     * @param values 值
     * @param score  分数
     * @return
     */
    public boolean zSet(final String key, final String values, final double score) {
        try {
            return stringRedisTemplate.opsForZSet().add(key, values, score);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取指定值的排名
     * @param key redis key
     * @param values 值
     * @return 返回指定成员的排名（从小到大）
     */
    public Long getRank(final String key, final String values) {
        try {
            //返回指定成员的排名（从小到大）
            Long rank = stringRedisTemplate.opsForZSet().rank(key, values);
            return rank == null ? 0L : rank;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取所有zset成员
     * @param key redis key
     * @return
     */
    public Set<String> ranges(final String key) {
        //按照排名先后(从小到大)打印指定区间内的元素, -1为打印全部
        try {
            return stringRedisTemplate.opsForZSet().range(key, 0, -1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Set<ZSetOperations.TypedTuple<String>> getRanks(final String key) {
        //返回集合内元素的排名，以及分数（从小到大）
        return stringRedisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
    }

    /**
     * 获取成员个数
     * @param key redis key
     * @return
     */
    public long rangeCount(final String key) {
        //返回集合内的成员个数
        try {
            return stringRedisTemplate.opsForZSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void zDel(final String key,final String value) {
        //从集合中删除指定元素
        stringRedisTemplate.opsForZSet().remove(key, value);
    }

    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List<String> lGet(final String key, final long start, final long end) {
        try {
            return stringRedisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(final String key) {
        try {
            return stringRedisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public String lGetIndex(final String key, final long index) {
        try {
            return (String) stringRedisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取头部数据同时进行删除
     *
     * @param key 键
     * @return
     */
    public String leftPop(final String key) {
        try {
            return (String) stringRedisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(final String key, final String value) {
        try {
            stringRedisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(final String key, final String value, final long time) {
        try {
            stringRedisTemplate.opsForList().rightPush(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(final String key, List<String> value) {
        try {
            stringRedisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(final String key, final List<String> value, final long time) {
        try {
            stringRedisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(final String key, final long index, final String value) {
        try {
            stringRedisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(final String key, final long count, final String value) {
        try {
            Long remove = stringRedisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 模糊匹配key
     *
     * @param key 键
     * @return true成功 false失败
     */
    public Set<String> keys(final String key) {
        try {
            return stringRedisTemplate.keys(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 分布式锁
     *
     * @param key 键
     * @return true成功 false失败
     */
    public Boolean setNx(final String key){
        return stringRedisTemplate.opsForValue().setIfAbsent(key, "1");
    }

    /**
     *
     * 根据key值获取String
     *
     * @param key
     * @return
     */
    public String getOneStringByKey(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     *
     * 缓存一个String
     * @param key
     * @param value
     * @param timeoutSeconds
     */
    public void setOneStringByKey(String key, String value, int timeoutSeconds) {
        stringRedisTemplate.opsForValue().set(key, value, timeoutSeconds, TimeUnit.SECONDS);
    }

    /**
     * 在获取元素下标区间之外的元素会被删除
     * @param key
     * @param beginScore 获取元素的排序开始下标
     * @param endScore 获取元素的排序结束下标
     * @return 指定排序下标范围内的元素
     */
    public Set<String> getSetByKeyAndScore(String key, int beginScore, int endScore) {
        stringRedisTemplate.opsForZSet().removeRangeByScore(key, 1, beginScore - 1);
        return stringRedisTemplate.opsForZSet().rangeByScore(key, beginScore, endScore);
    }

    /**
     *
     * @param key
     * @param newVal 新值
     * @param oldVal 旧值（非空则删除元素）
     * @param score 排序（使用时间基准值来判断是否删除元素）
     */
    public void addOneStringToZSet(String key, String newVal, String oldVal, double score) {
        if (oldVal != null)
            stringRedisTemplate.opsForZSet().remove(key, oldVal);
        stringRedisTemplate.opsForZSet().add(key, newVal, score);
    }

    /**
     * 存入redis GEO缓存
     * @param key 键
     * @param longitude   经度   String类型
     * @param latitude   纬度   String类型
     * @param member  店铺id
     * @return true/false
     */
    public boolean setGeo(String key,double longitude, double latitude,String member){
        boolean result = false;
        try {
            stringRedisTemplate.opsForGeo().add(key,new Point(longitude,latitude),member);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 移除地理位置信息
     * @param key
     * @param members  店铺id
     * @ReturnType：boolean
     */
    public boolean removeGeo(String key, String members) {
        try {
            stringRedisTemplate.opsForGeo().remove(key,members);
        } catch (Throwable t) {
        }
        return true;
    }
    /**
     * 获得某个位置的经纬度
     * @param key 键
     * @param member  店铺id
     *
     */
    public List<Point> getGeo(String key,String member){
        try {
            GeoOperations<String, String> geoOps = stringRedisTemplate.opsForGeo();
            List<Point> position = geoOps.position(key, member);
            return position;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回绑定到给定键的地理空间特定操作接口
     * @param key
     * @return
     */
    public BoundGeoOperations boundGeoOps(String key){

        BoundGeoOperations stringStringBoundGeoOperations = stringRedisTemplate.boundGeoOps(key);
        return stringStringBoundGeoOperations;
    }

    /**
     * 计算两个人之间的距离
     * @param key
     * @param user
     * @param user1
     * @return
     */
    public Double getDistance(String key,String user ,String user1){
        Distance distance = stringRedisTemplate.opsForGeo().distance(key, user, user1, RedisGeoCommands.DistanceUnit.KILOMETERS);
        return distance.getValue();
    }







    public void saveLiked2Redis(String likedUserId, String likedPostId) {
        String key = RedisKeyUtils.getLikedKey(likedUserId, likedPostId);
        redisTemplate.opsForHash().put(RedisCacheContants.MAP_KEY_USER_LIKED, key, RedisCacheContants.LIKE);
        incrementLikedCount(likedUserId);
    }

    public void unlikeFromRedis(String likedUserId, String likedPostId) {
        String key = RedisKeyUtils.getLikedKey(likedUserId, likedPostId);
        redisTemplate.opsForHash().put(RedisCacheContants.MAP_KEY_USER_LIKED, key, RedisCacheContants.UNLIKE);
        decrementLikedCount(likedUserId);
        deleteLikedFromRedis(likedUserId,likedPostId);
    }

    public void deleteLikedFromRedis(String likedUserId, String likedPostId) {
        String key = RedisKeyUtils.getLikedKey(likedUserId, likedPostId);
        redisTemplate.opsForHash().delete(RedisCacheContants.MAP_KEY_USER_LIKED, key);
    }

    public void incrementLikedCount(String likedUserId) {
        redisTemplate.opsForHash().increment(RedisCacheContants.MAP_KEY_USER_LIKED_COUNT, likedUserId, 1);
    }

    public void decrementLikedCount(String likedUserId) {
        redisTemplate.opsForHash().increment(RedisCacheContants.MAP_KEY_USER_LIKED_COUNT, likedUserId, -1);
    }

    public List<UserLike> getLikedDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisCacheContants.MAP_KEY_USER_LIKED, ScanOptions.NONE);
        List<UserLike> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            //分离出 likedUserId，likedPostId
            String[] split = key.split("::");
            String likedUserId = split[0];
            String likedPostId = split[1];
            Integer value = (Integer) entry.getValue();

            //组装成 UserLike 对象
            UserLike userLike = new UserLike(likedUserId, likedPostId, value);
            list.add(userLike);

            //存到 list 后从 Redis 中删除
            redisTemplate.opsForHash().delete(RedisCacheContants.MAP_KEY_USER_LIKED, key);
        }
        return list;
    }
    public List<LikedCountDTO> getLikedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisCacheContants.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
        List<LikedCountDTO> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> map = cursor.next();
            //将点赞数量存储在 LikedCountDT
            String key = (String)map.getKey();
            LikedCountDTO dto = new LikedCountDTO(Long.valueOf(key), (Integer) map.getValue());
            list.add(dto);
            //从Redis中删除这条记录
            redisTemplate.opsForHash().delete(RedisCacheContants.MAP_KEY_USER_LIKED_COUNT, key);
        }
        return list;
    }
}