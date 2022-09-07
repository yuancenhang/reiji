package com.hang.reiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hang.reiji.domain.R;
import com.hang.reiji.domain.ShoppingCart;
import com.hang.reiji.service.ShoppingCartService;
import com.hang.reiji.utils.UtilOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    /**
     * 获取购物车列表，传当前用户的id
     * @return list
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> getList(){
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, UtilOne.getIdInThread());
        wrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(wrapper);
        return R.success(list);
    }

    /**
     * 大概是这样：
     * 点击+号，把明细发来，根据orderId查找数据库，如果没有，就存。如果有，就更新数量number和amount
     * @return 前端需要数量amount
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        ShoppingCart c = shoppingCartService.selectAndSave(shoppingCart);
        return c != null ? R.success(c) : R.error("错误！");
    }

    /**
     * 大概是这样：
     * 点击+号，把明细发来，根据orderId查找数据库，如果没有，就存。如果有，就更新数量number和amount
     * @return 前端需要数量amount
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        ShoppingCart c = shoppingCartService.selectAndSave2(shoppingCart);
        return c != null ? R.success(c) : R.error("错误！");
    }

    /**
     * 清空购物车
     * 根据userId删除表数据
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        Long userId = UtilOne.getIdInThread();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        boolean ok = shoppingCartService.remove(wrapper);
        return ok ? R.success("清空购物车成功") : R.error("清空失败！");
    }
}
