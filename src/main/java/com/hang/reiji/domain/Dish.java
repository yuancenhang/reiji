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
 菜品
 */
@Data
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    //这个注解的作用是，由于后端返回给前端时，long类型会丢失精度，后几位变成0.用了这个注解后，在返回数据给前端时会把long转成String，不丢失精度
    @JsonSerialize(using = ToStringSerializer.class)
    //这个注解的作用是，由mybatisPlus自动为它赋值，ASSIGN_ID是雪花算法得出的一串数字
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    //菜品名称
    private String name;


    //菜品分类id
    //这个注解的作用是，由于后端返回给前端时，long类型会丢失精度，后几位变成0.用了这个注解后，在返回数据给前端时会把long转成String，不丢失精度
    @JsonSerialize(using = ToStringSerializer.class)
    private Long categoryId;


    //菜品价格
    private BigDecimal price;


    //商品码
    private String code;


    //图片
    private String image;


    //描述信息
    private String description;


    //0 停售 1 起售
    private Integer status;


    //顺序
    private Integer sort;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    //是否删除
    //private Integer isDeleted;

}
