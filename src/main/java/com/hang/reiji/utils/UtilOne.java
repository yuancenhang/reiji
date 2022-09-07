package com.hang.reiji.utils;


import com.hang.reiji.domain.AddressBook;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.util.UUID;

public class UtilOne {
    public static ThreadLocal<Long> local = new ThreadLocal<>();

    /**
     * 生成一个UUID
     */
    public static Long getUUID() {
        return UUID.randomUUID().getMostSignificantBits();
    }

    /**
     * 把String加密成MDS
     */
    public static String toMD5(String password){
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    /**
     * 把登陆者的ID保存到当前线程
     */
    public static void setIdInThread(Long id){
        local.set(id);
    }

    /**
     * 把登陆者的ID从当前线程取出
     */
    public static Long getIdInThread(){
        return (Long)local.get();
    }

    /**
     * 生成整数的验证码
     */
    public static String getCode(int o){
        double number = Math.random();
        for (int i = 0; i<o; i++){
            number *= 10;
        }
        Long newNumber = Math.round(number);
        return String.valueOf(newNumber);
    }

    /**
     * 生成一个订单号
     */
    public static String getDDH() {
        return String.valueOf(UUID.randomUUID().getMostSignificantBits());
    }

    /**
     * 封装的把整数转成BigDecimal
     * @param i
     * @return
     */
    public static BigDecimal toBigDecimal(Integer i){
        BigDecimal bigDecimal = new BigDecimal(0);
        return BigDecimal.valueOf(i);
    }

    /**
     * 把地址对象中的省市县等拼成一个地址String
     * @param addressBook
     * @return
     */
    public static String getAddress(AddressBook addressBook){
        StringBuilder buffer = new StringBuilder();
        if (addressBook.getProvinceCode() != null) buffer.append(addressBook.getProvinceCode());
        if (addressBook.getProvinceName() != null) buffer.append(addressBook.getProvinceName());
        if (addressBook.getCityCode() != null) buffer.append(addressBook.getCityCode());
        if (addressBook.getCityName() != null) buffer.append(addressBook.getCityName());
        if (addressBook.getDistrictCode() != null) buffer.append(addressBook.getDistrictCode());
        if (addressBook.getDistrictName() != null) buffer.append(addressBook.getDistrictName());
        if (addressBook.getDetail() != null) buffer.append(addressBook.getDetail());
        return String.valueOf(buffer);
    }

    /**
     * 设置redis的序列化方式
     */
    public static ValueOperations getValue(RedisTemplate template,Class c){
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(c));
        return template.opsForValue();
    }
}
