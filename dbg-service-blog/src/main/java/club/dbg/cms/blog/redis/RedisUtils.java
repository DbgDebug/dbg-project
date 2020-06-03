package club.dbg.cms.blog.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author dbg
 * @date 2019/08/17
 */

@Component
public final class RedisUtils {
    private static final Logger log = LoggerFactory.getLogger(RedisUtils.class);

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void initRedis(Integer indexDb) {
        LettuceConnectionFactory jedisConnectionFactory = (LettuceConnectionFactory) redisTemplate.getConnectionFactory();
        jedisConnectionFactory.setDatabase(indexDb);
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        jedisConnectionFactory.resetConnection();
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public Boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("redis:", e);
            return false;
        }
    }


    /**
     * 获取key的过期时间
     *
     * @param key 不能为null
     * @return 返回0代表永久有效
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public Boolean getKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("redis:", e);
            return false;
        }
    }

    /**
     * 设置单个缓存
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis:", e);
            return false;
        }
    }

    /**
     * 设置单个缓存并设置过期时间
     *
     * @param key
     * @param value
     * @param time  秒
     * @return
     */
    public Boolean set(String key, Object value, long time) {
        try {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("redis:", e);
            return false;
        }
    }

    /**
     * 根据key获取缓存
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存
     *
     * @param key
     * @return
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> listGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("redis:", e);
            return null;
        }
    }

    /**
     * 获取列表大小
     *
     * @param key 键
     * @return
     */
    public long listSizeGet(String key) {
        try {
            Long size = redisTemplate.opsForList().size(key);
            return size == null ? 0 : size;
        } catch (Exception e) {
            log.error("redis:", e);
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
    public Object listGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("redis:", e);
            return null;
        }
    }

    /**
     * 在列表左边添加元素
     *
     * @param key   键
     * @param value 值
     * @return boolean
     */
    public boolean llPush(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis:", e);
            return false;
        }
    }

    public boolean lrPush(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value, time);
            return true;
        } catch (Exception e) {
            log.error("redis:", e);
            return false;
        }
    }

    /**
     * 查询区间范围内的元素
     *
     * @param key   键
     * @param start 区间开始
     * @param end   区间结束
     */
    public List<Object> lRange(String key, int start, int end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("redis:", e);
            return null;
        }
    }

    /**
     * count>0：从表头向表尾搜索，移除count个值为value的元素
     * count<0：从表尾向表头搜索，移除count的绝对值个值为value的元素
     * count=0：移除表中所有的值为value的元素。要遍历列表，从哪边开始结果、速度都是一样的。
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove == null ? 0 : remove;
        } catch (Exception e) {
            log.error("redis:", e);
            return 0;
        }
    }

    /**
     * 保留区间内的元素，区间外的全部删除
     *
     * @param key   键
     * @param start 区间开始
     * @param end   区间结束
     */
    public boolean lTrim(String key, int start, int end) {
        try {
            redisTemplate.opsForList().trim(key, start, end);
            return true;
        } catch (Exception e) {
            log.error("redis:", e);
            return false;
        }
    }

    /**
     * 获取集合
     *
     * @param key 键
     * @return set<object>
     */
    public Set<Object> getSet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("redis:", e);
            return null;
        }
    }

    /**
     * 获取集合大小
     *
     * @param key
     * @return
     */
    public long setCard(String key) {
        try {
            Long size = redisTemplate.opsForSet().size(key);
            return size == null ? 0 : size;
        } catch (Exception e) {
            log.error("redis:", e);
            return 0;
        }
    }

    public boolean setIsMember(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value) != null;
        } catch (Exception e) {
            log.error("redis:", e);
            return false;
        }
    }

    /**
     * 添加元素到集合
     *
     * @param key   键
     * @param value 值
     * @return Boolean
     */
    public boolean setAdd(String key, Object value) {
        try {
            redisTemplate.opsForSet().add(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis:", e);
            return false;
        }
    }

    /**
     * 移除集合内的指定元素
     *
     * @param key   键
     * @param value 值
     * @return Boolean
     */
    public boolean setRem(String key, Object value) {
        try {
            redisTemplate.opsForSet().remove(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis:", e);
            return false;
        }
    }

    public Set<Object> setUnion(String key, Collection<String> otherKey) {
        try {
            return redisTemplate.opsForSet().union(key, otherKey);
        } catch (Exception e) {
            log.error("redis:", e);
            return null;
        }
    }

    public Object hashGet(String hashKey, Object o) {
        try {
            return  redisTemplate.opsForHash().get(hashKey, o);
        } catch (Exception e) {
            log.error("redis:", e);
            return null;
        }
    }

    public boolean hashPut(String hashKey, Object hk, Object hv) {
        try {
            redisTemplate.opsForHash().put(hashKey, hk, hv);
            return  true;
        } catch (Exception e) {
            log.error("redis:", e);
            return false;
        }
    }

    public boolean hashPutAll(String hashKey, Map<?, ?> map){
        try {
            redisTemplate.opsForHash().putAll(hashKey, map);
            return  true;
        } catch (Exception e) {
            log.error("redis:", e);
            return false;
        }
    }

    public boolean hashDelete(String hashKey, Object hk){
        try {
            redisTemplate.opsForHash().delete(hashKey, hk);
            return  true;
        } catch (Exception e) {
            log.error("redis:", e);
            return false;
        }
    }
}
