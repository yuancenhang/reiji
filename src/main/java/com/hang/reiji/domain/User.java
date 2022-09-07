package com.hang.reiji.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    //这个注解的作用是，由于后端返回给前端时，long类型会丢失精度，后几位变成0.用了这个注解后，在返回数据给前端时会把long转成String，不丢失精度
    @JsonSerialize(using = ToStringSerializer.class)
    //这个注解的作用是，由mybatisPlus自动为它赋值，ASSIGN_ID是雪花算法得出的一串数字
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    //姓名
    private String name;


    //手机号
    private String phone;


    //性别 0 女 1 男
    private String sex;


    //身份证号
    private String idNumber;


    //头像
    private String avatar;


    //状态 0:禁用，1:正常
    private Integer status;
}
