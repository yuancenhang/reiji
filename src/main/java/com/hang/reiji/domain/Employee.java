package com.hang.reiji.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    //这个注解的作用是，由于后端返回给前端时，long类型会丢失精度，后几位变成0.用了这个注解后，在返回数据给前端时会把long转成String，不丢失精度
    @JsonSerialize(using = ToStringSerializer.class)
    //这个注解的作用是，由mybatisPlus自动为它赋值，ASSIGN_ID是雪花算法得出的一串数字
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    //这个注解的作用是，由mybatisPlus自动为它赋值，INSERT_UPDATE表示使用MybatisPlus提供的方法操作数据库时，当执行insert和update操作时，自动为它赋值
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long createUser;

    @TableField(fill = FieldFill.UPDATE)
    private Long updateUser;

}
