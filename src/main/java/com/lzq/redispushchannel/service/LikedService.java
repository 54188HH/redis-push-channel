package com.lzq.redispushchannel.service;

import com.lzq.redispushchannel.mapper.UserLikeMapper;
import com.lzq.redispushchannel.mapper.UserMapper;
import com.lzq.redispushchannel.po.LikedCountDTO;
import com.lzq.redispushchannel.po.User;
import com.lzq.redispushchannel.po.UserLike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/11/5 10:09
 */
@Service
public class LikedService {
    @Autowired
    UserLikeMapper likeMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 保存点赞记录
     *
     * @param userLike
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int save(UserLike userLike) {
        int i = 0;
        if (null == userLike.getId()) {
            i = likeMapper.insertUseGeneratedKeys(userLike);
        } else {
            userLike.setUpdateTime(new Date());
            i = likeMapper.updateByPrimaryKeySelective(userLike);
        }
        return i;
    }

    /**
     * 通过被点赞人和点赞人id查询是否存在点赞记录
     *
     * @param likedUserId
     * @param likedPostId
     * @return
     */
    public UserLike getByLikedUserIdAndLikedPostId(String likedUserId, String likedPostId) {
  /*      Example example = new Example(UserLike.class);
        example.createCriteria().andEqualTo("likedUserId", likedUserId)
                .andEqualTo("likedPostId", likedPostId);
        return likeMapper.selectOneByExample(example);*/
        return null;
    }

    /**
     * 将Redis里的点赞数据存入数据库中
     */
    @Transactional(rollbackFor = Exception.class)
    public void transLikedFromRedis2DB() {
        List<UserLike> list = redisService.getLikedDataFromRedis();
        for (UserLike like : list) {
            UserLike ul = getByLikedUserIdAndLikedPostId(like.getLikedUserId(), like.getLikedPostId());
            if (ul == null) {
                //没有记录，直接存入
                save(like);
            } else {
                //有记录，需要更新
                ul.setStatus(like.getStatus());
                save(ul);
            }
        }
    }

    /**
     * 将Redis中的点赞数量数据存入数据库
     */
    @Transactional(rollbackFor = Exception.class)
    public void transLikedCountFromRedis2DB() {
        List<LikedCountDTO> list = redisService.getLikedCountFromRedis();
        for (LikedCountDTO dto : list) {
            User user = userMapper.selectByPrimaryKey(dto.getId());
            //点赞数量属于无关紧要的操作，出错无需抛异常
            if (user != null) {
                Integer likeNum = user.getLikeNum() + dto.getCount();
                user.setLikeNum(likeNum);
                //更新点赞数量
                userMapper.updateByPrimaryKeySelective(user);
            }
        }
    }
}
