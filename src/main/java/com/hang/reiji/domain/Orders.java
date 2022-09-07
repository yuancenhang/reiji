package com.hang.reiji.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单
 */
@Data
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    //这个注解的作用是，由于后端返回给前端时，long类型会丢失精度，后几位变成0.用了这个注解后，在返回数据给前端时会把long转成String，不丢失精度
    @JsonSerialize(using = ToStringSerializer.class)
    //这个注解的作用是，由mybatisPlus自动为它赋值，ASSIGN_ID是雪花算法得出的一串数字
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    //订单号
    private String number;

    //订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
    private Integer status;


    //下单用户id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    //地址id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long addressBookId;


    //下单时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime orderTime;


    //结账时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime checkoutTime;


    //支付方式 1微信，2支付宝
    private Integer payMethod;


    //实收金额
    private BigDecimal amount;

    //备注
    private String remark;

    //用户名
    private String userName;

    //手机号
    private String phone;

    //地址
    private String address;

    //收货人
    private String consignee;
}
