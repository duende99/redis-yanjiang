package ded.redisrelation.redis;

import ded.redisrelation.config.Slf4jLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service("redisService")
public class RedisService {
    private static final String KEY_SPLIT = ":"; // 用于隔开项目前缀与缓存键值

//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<Object, Object> stringRedisTemplate;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate2;

    /**
     * 设置缓存
     *
     * @param prefix 项目前缀（用于区分缓存，防止缓存键值重复）
     * @param key    缓存key
     * @param value  缓存value
     */
    public void set(String prefix, String key, String value) {
        stringRedisTemplate.opsForValue().set(prefix + KEY_SPLIT + key, value);
        Slf4jLogUtil.SimpleLogUtil.debug("RedisService:set cache key={},value={}", prefix + KEY_SPLIT + key, value);
    }

    //设置缓存 无项目前缀
    public void set( String key, String value) {
        stringRedisTemplate.opsForValue().set(KEY_SPLIT + key, value);
        Slf4jLogUtil.SimpleLogUtil.debug("RedisService:set cache key={},value={}",  KEY_SPLIT + key, value);
    }

    public void setkey( String key, String value, Long expireTime, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value);
        stringRedisTemplate.expire(key, expireTime, timeUnit);
        Slf4jLogUtil.SimpleLogUtil.debug("RedisService:setWithExpireTime cache key={},value={},expireTime={},timeUnit={}", key, value, expireTime, timeUnit);
    }
    public void setStringToSet(String prefix, String key, String value) {
//		stringRedisTemplate.opsForValue().set(prefix + KEY_SPLIT + key, value);
        redisTemplate2.opsForSet().add(prefix + KEY_SPLIT + key, value);
        Slf4jLogUtil.SimpleLogUtil.debug("RedisService:set cache key={},value={}", prefix + KEY_SPLIT + key, value);
    }

    /**
     * 设置缓存，并且指定过期时间
     *
     * @param prefix     项目前缀（用于区分缓存，防止缓存键值重复）
     * @param key        缓存key
     * @param value      缓存value
     * @param expireTime 过期时间
     * @param timeUnit   过期时间单位
     */
    public void setWithExpireTime(String prefix, String key, String value, Long expireTime, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(prefix + KEY_SPLIT + key, value, expireTime, timeUnit);
        Slf4jLogUtil.SimpleLogUtil.info("RedisService:setWithExpireTime cache key={},value={},expireTime={},timeUnit={}",
                prefix + KEY_SPLIT + key, value, expireTime, timeUnit);
    }

    /**
     * 获取指定key的缓存
     *
     * @param prefix 项目前缀（用于区分缓存，防止缓存键值重复）
     * @param key    缓存key
     * @return 缓存value
     */
    public String get(String prefix, String key) {
        String value = (String)stringRedisTemplate.opsForValue().get(prefix + KEY_SPLIT + key);
        Slf4jLogUtil.SimpleLogUtil.debug("RedisService:get cache key={},value={}", prefix + KEY_SPLIT + key, value);
        return value;
    }

    /**
     * 获取指定key的缓存
     *
     * @param key 缓存key
     * @return
     */
    public String get(String key) {
        String value = (String)stringRedisTemplate.opsForValue().get(key);
//        String value = stringRedisTemplate.opsForValue().get(key);
        Slf4jLogUtil.SimpleLogUtil.debug("RedisService:get cache key={},value={}", key, value);
        return value;
    }

    public long getSetCount(String prefix, String key) {
        return redisTemplate2.opsForSet().size(prefix + KEY_SPLIT + key);
    }

    public long getSetCount(String key) {
        return redisTemplate2.opsForSet().size(key);
    }

    public Set getSet(String prefix, String key) {
        return redisTemplate2.opsForSet().members(prefix + KEY_SPLIT + key);
    }

    public Set getSet(String key) {
        return redisTemplate2.opsForSet().members(key);
    }

    /**
     * 删除指定key的缓存
     *
     * @param prefix 项目前缀（用于区分缓存，防止缓存键值重复）
     * @param key    缓存key
     */
    public void deleteWithPrefix(String prefix, String key) {
        stringRedisTemplate.delete(prefix + KEY_SPLIT + key);
        Slf4jLogUtil.SimpleLogUtil.debug("RedisService:deleteWithPrefix cache key={},value={}", prefix + KEY_SPLIT + key);
    }

    public void delete(String prefix, String key) {
        redisTemplate2.delete(prefix + KEY_SPLIT + key);
        Slf4jLogUtil.SimpleLogUtil.debug("RedisService:deleteWithPrefix cache key={},value={}", prefix + KEY_SPLIT + key);
    }

    /**
     * 删除指定key的缓存
     *
     * @param key 缓存key
     */
    public void delete(String key) {
        stringRedisTemplate.delete(key);
        Slf4jLogUtil.SimpleLogUtil.debug("RedisService:delete cache key={}", key);
    }

    /**
     * 删除key
     * @param key
     */
    public void deleteSetKey(String key){
        redisTemplate2.delete(key);
        Slf4jLogUtil.SimpleLogUtil.debug("RedisService:delete cache key={}", key);
    }

    /**
     * 判断是否存在key
     *
     * @param key
     * @return
     */
    public Boolean hasKey(Object key) {
        return redisTemplate2.hasKey(key);
    }

    public void deleteByKey(Object key) {
        redisTemplate2.delete(key);
        Slf4jLogUtil.SimpleLogUtil.debug("RedisService:delete cache key={}", key);
    }

    /**
     * 自增
     *
     * @param prefix 项目前缀（用于区分缓存，防止缓存键值重复）
     * @param key    缓存key
     * @param span   自增跨度 当span大于零时，自增；当span小于零时，自减
     * @return
     */
    public Long incr(String prefix, String key, Long span) {
        Long value = stringRedisTemplate.opsForValue().increment(prefix + KEY_SPLIT + key, span);
        Slf4jLogUtil.SimpleLogUtil.debug("RedisService:incr cache key={}", prefix + KEY_SPLIT + key, value);
        return value;
    }

    /**
     * 没有前缀的自增方法
     * @param key
     * @return
     */
    public Long incr(String key, Long span) {
        Long value = stringRedisTemplate.opsForValue().increment(key, span);
        Slf4jLogUtil.SimpleLogUtil.debug("RedisService:incr cache key={}", key, value);
        return value;
    }
    
    public Long getCountWithKey(String key) {
    	String s = (String)stringRedisTemplate.opsForValue().get(key);
//    	if(StringUtils.isBlank(s)) {
//    		return null;
//    	}
    	Long count = Long.valueOf(s);
    	return count;
    }
    
    public void setCountWithKey(String key,Long value) {
    	stringRedisTemplate.opsForValue().set(key, String.valueOf(value));
    }
    public void setTime(String key,Long time,TimeUnit unit) {
    	stringRedisTemplate.expire(key, time, unit);
    }

    public void setWithExpireTimeWithoutPrefix(String key, String value, Long expireTime, TimeUnit timeUnit) {
        redisTemplate2.opsForSet().add(key, value);
        redisTemplate2.expire(key, expireTime, timeUnit);
        Slf4jLogUtil.SimpleLogUtil.debug("RedisService:setWithExpireTime cache key={},value={},expireTime={},timeUnit={}", key, value, expireTime, timeUnit);
    }

    /**
     * 指定的值插入存储在键的列表
     *
     * @param key
     * @param value
     * @return 插入后列表的长度
     */
    public Long leftPushToList(String key, Object value) {
        return redisTemplate2.opsForList().leftPush(key, value);
    }


    /**
     * 批量把一个集合插入到列表
     *
     * @param key
     * @param values
     * @return 插入后列表的长度
     */
    public Long leftPushAllToList(String key, Collection<Object> values) {
        return redisTemplate2.opsForList().leftPushAll(key, values);
    }

    /**
     * 取列表中最右边的元素，（该值在列表中将不复存在）
     *
     * @param key
     * @return 列表中最右边的元素
     */
    public Object leftPopFromList(String key) {
        return redisTemplate2.opsForList().leftPop(key);
    }

    /**
     * 取列表中最右边的元素  (如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止)
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Object leftPopFromList(String key, Long timeout, TimeUnit unit) {
        return redisTemplate2.opsForList().leftPop(key, timeout, unit);
    }

    /**
     * 获取列表中的长度 （如果键不存在，则将其解释为空列表，并返回0。当key存储的值不是列表时返回错误。）
     *
     * @param key
     * @return
     */
    public Long sizeFromList(String key) {
        return redisTemplate2.opsForList().size(key);
    }
    
    /**
     * 向set中添加值并返回
     *
     * @param key
     * @param value
     * @return
     */
    public Long setWithReturn(String key, String value) {
        return redisTemplate2.opsForSet().add(key, value);
    }

    /**
     * 向set添加值，设置过期时间并返回
     * @param key
     * @param value
     * @param expireTime
     * @param timeUnit
     * @return
     */
    public Long setWithExpireTimeWithReturn(String key, String value, Long expireTime, TimeUnit timeUnit) {
    	Long count = redisTemplate2.opsForSet().add(key, value);
        redisTemplate2.expire(key, expireTime, timeUnit);
        return count;
    }

    //设置缓存 无项目前缀
    public void set2( String key, String value) {
        redisTemplate2.opsForValue().set(key, value);
        System.out.println("===========" + key + "---" + value);
//        stringRedisTemplate.opsForValue().set(KEY_SPLIT + key, value);
//        Slf4jLogUtil.SimpleLogUtil.debug("RedisService:set cache key={},value={}",  KEY_SPLIT + key, value);
    }
}
