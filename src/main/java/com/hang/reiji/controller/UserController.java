package com.hang.reiji.controller;

import com.hang.reiji.domain.R;
import com.hang.reiji.domain.User;
import com.hang.reiji.service.UserService;
import com.hang.reiji.utils.UtilOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate<Object,Object> redisTemplate;

    /**
     * 获取验证码
     * @return 验证码
     */
    @GetMapping("/getCode")
    public R<String> getCode(String phone, HttpSession session){
        String code = UtilOne.getCode(4);
        ValueOperations<Object,Object> valueOperations = UtilOne.getValue(redisTemplate,String.class);
        valueOperations.set(phone,code,5L, TimeUnit.MINUTES); //放在redis缓存，5分钟有效
        return R.success(code);
    }

    /**
     * 登陆方法，如果有这个用户，就算了，如果没有，就存一个
     * 手机号：phone，验证码：code
     * @return User
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String,String> map,HttpSession session){
        String code = map.get("code");
        String phone = map.get("phone");
        ValueOperations<Object,Object> valueOperations = UtilOne.getValue(redisTemplate,String.class);
        String code1 = (String) valueOperations.get(phone);
        if (code1 != null){
            System.out.println("传来的验证码是：" + code);
            System.out.println("redis保存的验证码是：" + code1);
            if (code.equals(code1)){
                User user = userService.mySave(phone);
                session.removeAttribute(phone);
                session.setAttribute("user",user.getId());
                redisTemplate.delete(phone);
                return R.success(user);
            }else {
                return R.error("验证码不正确！");
            }
        }else {
            return R.error("验证码已过期！");
        }
    }

    /**
     * 退出登陆
     * @return
     */
    @PostMapping("/loginout")
    public R<String> loginOut(HttpSession session){
        session.removeAttribute("user");
        return R.success("安全退出登陆");
    }
}
