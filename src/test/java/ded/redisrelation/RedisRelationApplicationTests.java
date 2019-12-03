package ded.redisrelation;

import ded.redisrelation.model.TestModel;
import ded.redisrelation.redis.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class RedisRelationApplicationTests {

    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate2;

    /**
     * set类型
     * 用于去重
     */
    @Test
    public void test1() {
        Long set1 = redisTemplate2.opsForSet().add("set-prefix:" + "set1", "111");
        Long set2 = redisTemplate2.opsForSet().add("set-prefix:" + "set1", "222");
        Long set5 = redisTemplate2.opsForSet().add("set-prefix:" + "set1", "555");
        Long set6 = redisTemplate2.opsForSet().add("set-prefix:" + "set1", "666");
        Long set3 = redisTemplate2.opsForSet().add("set-prefix:" + "set1", "333");
        Long set33 = redisTemplate2.opsForSet().add("set-prefix:" + "set1", "333");
        Set<Object> set11 = redisTemplate2.opsForSet().members("set-prefix:set1");
        Iterator<Object> it = set11.iterator();
        while (it.hasNext()) {
            String str = (String)it.next();
            System.out.println(str);
        }
        System.out.println("=========");
        //redisTemplate.opsForSet().isMember(key, value);  //判断是否存在
        // redisTemplate.opsForSet().remove(key, value); //删除元素
    }
    /**
     * string类型
     */
    @Test
    public void test2() {
        redisTemplate2.opsForValue().set("string-prefix:" + "string1","1111");
        Object o = redisTemplate2.opsForValue().get("string-prefix:" + "string1");
        System.out.println(o.toString());
    }

    /**
     * hash类型
     * 存储对象类型
     */
    @Test
    public void test3() {
        //对象必须序列话
        TestModel t1 = new TestModel();
        t1.setAge(111);
        t1.setName("222");
        redisTemplate2.opsForHash().put(t1, "age", t1.getAge());
        redisTemplate2.opsForHash().put(t1, "name", t1.getName());
        Object hash = redisTemplate2.opsForHash().get(t1, "age");
        Object hash2 = redisTemplate2.opsForHash().get(t1, "name");
        System.out.println(hash.toString());
        System.out.println(hash2.toString());
    }


    /**
     * zset
     * 每个元素可以设置一个score,可以实现各种排行榜的功能
     * https://blog.csdn.net/liuyueyi25/article/details/84997603
     */
    @Test
    public void test4() {
        redisTemplate2.opsForZSet().add("11", "11", 2);
        redisTemplate2.opsForZSet().add("11", "33", 2);
        redisTemplate2.opsForZSet().add("11", "22", 3);
        redisTemplate2.opsForZSet().add("11", "21", 3);

        Double score = redisTemplate2.opsForZSet().score("11", "11");
        Double score2 = redisTemplate2.opsForZSet().score("11", "22");
        Double score4 = redisTemplate2.opsForZSet().score("11", "21");
        Double score3 = redisTemplate2.opsForZSet().score("11", "33");
        System.out.println("=============" + score);
        System.out.println("=============" + score2);
        System.out.println("=============" + score3);
        System.out.println("=============" + score4);

        Long rank = redisTemplate2.opsForZSet().rank("11", "11");
        Long rank2 = redisTemplate2.opsForZSet().rank("11", "22");
        Long rank4 = redisTemplate2.opsForZSet().rank("11", "21");
        Long rank3 = redisTemplate2.opsForZSet().rank("11", "33");
        System.out.println("------------" + rank);
        System.out.println("------------" + rank2);
        System.out.println("------------" + rank3);
        System.out.println("------------" + rank4);
    }

    /**
     * list
     * 做队列使用，例如 做活动要给一批手机号充话费，则可以将手机号放入list中
     */
    @Test
    public void test5() {
        redisTemplate2.opsForList().leftPush("list2", "list1");
        redisTemplate2.opsForList().leftPush("list2", "list2");
        redisTemplate2.opsForList().leftPush("list2", "list3");

        redisTemplate2.opsForList().leftPop("list2");

        //获取元素，当index = -1 表示返回最后一个，当index大于队列实际长度，则返回null
        Object list1 = redisTemplate2.opsForList().index("list2", 0);
        Object list11 = redisTemplate2.opsForList().index("list2", 1);
        System.out.println(list1);
        System.out.println(list11);

//        redisTemplate2.opsForList().remove("list2", 1, "list1");
//        Long list12 = redisTemplate2.opsForList().size("list2");
//        Object list2 = redisTemplate2.opsForList().index("list2", 0);
//        Object list3 = redisTemplate2.opsForList().index("list2", 1);
//        Object list4 = redisTemplate2.opsForList().index("list2", 4);
//        System.out.println(list2);
//        System.out.println(list3);
//        System.out.println(list4);
//        System.out.println("====" + list12);

    }


    /***
     * 设置过期时间
     */
    @Test
    public void setExpireTime() {
        redisTemplate2.opsForValue().set("hello","hello");
        redisTemplate2.expire("hello", 5000, TimeUnit.MILLISECONDS);
        try {
            Thread.sleep(2000);
            Object hello = redisTemplate2.opsForValue().get("hello");
            System.out.println("========1" + hello);

            Thread.sleep(5000);
            Object hello2 = redisTemplate2.opsForValue().get("hello");
            System.out.println("========2" + hello2);
        } catch (Exception e) {

        }
    }

    /**
     * 分布式锁
     */
    public void setnx(String flag, String flag2) {
        Boolean isLocked = redisTemplate2.opsForValue().setIfAbsent(flag2, flag2, 2000, TimeUnit.MILLISECONDS);
        if (isLocked) {
            System.out.println("=========获得锁成功！" + flag);
        } else {
            System.out.println("=========获得锁失败！" + flag);
        }
    }
    @Test
    public void test6() {
        setnx("1", "1");
        try {
            Thread.sleep(1000);
            setnx("2", "2");
            Thread.sleep(3000);
            setnx("3", "3");
        } catch (Exception e) {

        }
    }

    /**
     *缓存穿透解决：大量请求请求一个不存在的值
     */
    @Test
    public void test7() {
        //1. 先查询Redis

        //2. redis查询不到查询DB，DB也查不到

        //3. 将查询不到的key缓存起来，5min过期
    }

    /***
     * 缓存击穿：热点数据，大量请求，但是key过期了
     * 使用分布式锁机制
     */
    @Test
    public void test8() {

//        String value = redis.get(key);
//        if (value == null) { //代表缓存值过期
//            //设置3min的超时，防止del操作失败的时候，下次缓存过期一直不能load db
//            if (redis.setnx(key_mutex, 1, 3 * 60) == 1) {  //代表设置成功
//                value = db.get(key);
//                redis.set(key, value, expire_secs);
//                redis.del(key_mutex);
//            } else {  //这个时候代表同时候的其他线程已经load db并回设到缓存了，这时候重试获取缓存值即可
//                sleep(50);
//                get(key);  //重试
//            }
//        } else {
//            return value;
//        }
    }

    /***
     * 缓存雪崩：大量key同一时间失效
     */



}
