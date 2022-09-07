package com.hang.reiji.settings;

import com.hang.reiji.domain.R;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.sql.SQLIntegrityConstraintViolationException;

//这个注解的作用是，拦截Controller。当有异常抛出时，可以经由这里，把异常信息返回给前端。
@ControllerAdvice(annotations = {Controller.class, RestController.class})
//这个注解的作用是，这里需要代替Controller返回数据
@ResponseBody
public class ExceptionHandler {

    /*
    这个异常是数据库异常，当抛出这个异常时，就会执行此方法的代码
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    //@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exception1(SQLIntegrityConstraintViolationException e){
        String msg = e.getMessage();
        //当插入数据时，如果被unique约束的字段出现重复，就会出现这两个单词.用空格分割这个字符串，第三个就是重复字段的插入值
        if (msg.contains("Duplicate entry")){
            String[] s = msg.split(" ");
            msg = s[2] + "已存在";
            return R.error(msg);
        }
        return R.error("失败，未知错误！！！");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ExceptionOne.class)
    public R<String> exception2(ExceptionOne e){
        return R.error(e.getMessage());
    }
}
