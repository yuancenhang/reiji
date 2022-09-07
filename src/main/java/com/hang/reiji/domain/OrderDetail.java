package com.hang.reiji.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细
 */
@Data
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    //这个注解的作用是，由于后端返回给前端时，long类型会丢失精度，后几位变成0.用了这个注解后，在返回数据给前端时会把long转成String，不丢失精度
    @JsonSerialize(using = ToStringSerializer.class)
    //这个注解的作用是，由mybatisPlus自动为它赋值，ASSIGN_ID是雪花算法得出的一串数字
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    //名称
    private String name;

    //订单id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;


    //菜品id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dishId;


    //套餐id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long setmealId;


    //口味
    private String dishFlavor;


    //数量
    private Integer number;

    //金额
    private BigDecimal amount;

    //图片
    private String image;
}
