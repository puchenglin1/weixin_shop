package com.weixin.util;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisUtil {
    private Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private JedisPool jedisPool;

    /**
     * 向redis存入key和value
     * @param key
     * @param value
     * @return
     */
    public String set(String key,String value){
        Jedis jedis = null;
        String s="";
        try{
            jedis=jedisPool.getResource();
            s= jedis.set(key,value);
        }catch (Exception e){
            logger.error("jedis get error:{}",e);
        }finally{
            returnResource(jedis);
            return s;
        }
    }

    /**
     *通过key获取储存在redis中的value
     * @param key
     * @return
     */
    public String get(String key){
        String value="";
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            value=jedis.get(key);
            logger.info("key===========:"+value);
        }catch (Exception e){
            logger.error("jedis set error:{}",e);
        }finally{
            returnResource(jedis);
            return value;
        }
    }

    /**
     * 删除指定key
     * @param key
     * @return
     */
    public Long del(String key){
        Jedis jedis=null;
        Long l=0L;
        try{
            jedis=jedisPool.getResource();
            l= jedis.del(key);
        }catch (Exception e){
            logger.error("jedis del error:{}",e);
        }finally{
            returnResource(jedis);
            return l;
        }
    }

    /**
     * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
     * @param key
     * @param value
     * @return
     */
    public Long expire(String key,int value){
        Jedis jedis=null;
        Long l=0L;
        try{
            jedis=jedisPool.getResource();
            l=jedis.expire(key,value);
        }catch(Exception e){
            logger.error("jedis expire error:{}",e);
        }finally{
            returnResource(jedis);
            return l;
        }
    }

    /**
     * 释放资源
     */
    public void returnResource(Jedis jedis){
        if(jedis!=null){
            jedisPool.returnResource(jedis);
        }
    }
}
