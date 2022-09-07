package com.hang.reiji.settings;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hang.reiji.utils.UtilOne;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * 这个类是自动为实体类赋值用的。
 * 实体类和数据库表中有些字段是所有表都有的，如createTime,createUser,updateTime,updateUser等
 * 可以把这些字段交给mybatisPlus来维护，每次操作的时候为它们自动赋值，但需要一些配置。
 * 1，在实体类中的公共字段上加注解，指明是什么时机赋值，如insert代表新增时赋值，update表示更新时赋值
 * 2，写一个类继承MetaObjectHandler，重写两个方法。
 */
@Component
public class MetaHandler implements MetaObjectHandler {

    /**
     * 这个方法会在insert操作时为变量赋值
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        /*
        createUser属于公共字段，是登陆者的ID，存在session里的。但是这里无法获取session。
        可以通过线程获取。每次访问服务器，都会创建一条单独的线程。这条线程先经过Filter过滤器，再经过Controller，再经过这个类。
        线程里有一个变量，相当于一个作用域，可以存东西。在过滤器登陆验证的时候，从session里获取ID存进线程，然后在这里取出来用。
         */
        String[] strings = metaObject.getGetterNames();
        for (String s : strings){
            if (s.equals("createUser")) metaObject.setValue("createUser", UtilOne.getIdInThread());
            if (s.equals("updateUser")) metaObject.setValue("updateUser", UtilOne.getIdInThread());
            if (s.equals("createTime")) metaObject.setValue("createTime", LocalDateTime.now());
            if (s.equals("updateTime")) metaObject.setValue("updateTime", LocalDateTime.now());
            if (s.equals("orderTime")) metaObject.setValue("orderTime", LocalDateTime.now());
            if (s.equals("checkoutTime")) metaObject.setValue("checkoutTime", LocalDateTime.now());
        }
    }

    /**
     * 这个方法会在update操作时为变量赋值
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        String[] strings = metaObject.getGetterNames();
        for (String s : strings) {
            if (s.equals("updateUser")) metaObject.setValue("updateUser", UtilOne.getIdInThread());
            if (s.equals("updateTime")) metaObject.setValue("updateTime", LocalDateTime.now());
            if (s.equals("checkoutTime")) metaObject.setValue("checkoutTime", LocalDateTime.now());
        }
    }
}
