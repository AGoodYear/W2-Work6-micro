package com.ivmiku.w6r1.utils;

import com.alibaba.fastjson2.JSON;
import com.ivmiku.w6r1.entity.Todo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Aurora
 */
@Component
public class RedisUtil {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void listAdd(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    public List<Todo> listGet(String key, int s, int e) {
        List<Object> list = redisTemplate.opsForList().range(key, s, e);
        List<Todo> result = new ArrayList<>();
        if (list != null) {
            for (Object json : list) {
                result.add(JSON.parseObject(JSON.toJSONString(json), Todo.class));
            }
        }
        return result;
    }

    public void listClear(String key) {
        redisTemplate.opsForList().trim(key, 1, 0);
    }

    public Long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public Set<String> getKey() {
        return redisTemplate.keys("history:*");
    }

    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public void setExpireTime(String key) {
        if (redisTemplate.opsForValue().getOperations().getExpire(key) > 0) {
            redisTemplate.expire(key, 3, TimeUnit.DAYS);
        }
    }

    public void refreshExpire(String key) {
        redisTemplate.persist(key);
        redisTemplate.expire(key, 3, TimeUnit.DAYS);
    }

    public boolean ifExist(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public List<String> getStringList(String key, int s, int e) {
        List<Object> list = redisTemplate.opsForList().range(key, s, e);
        List<String> result = new ArrayList<>();
        assert list != null;
        for (Object object : list) {
            result.add((String) object);
        }
        return result;
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
}
