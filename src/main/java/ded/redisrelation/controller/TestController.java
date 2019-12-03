package ded.redisrelation.controller;


import ded.redisrelation.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisService redisService;

    /**
     * 基本使用 String 类型使用
     * 一般使用时，都需要加上有意义的前缀，用于区分
     * @return
     */
    @RequestMapping("/hello")
    @ResponseBody
    public String test() {
        redisService.set2("hello","i am deamon!");
        return redisService.get("hello");
    }

    /**
     * String 类型使用
     */






}
