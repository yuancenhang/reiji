package com.hang.reiji.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.reiji.domain.OrderDetail;
import com.hang.reiji.domain.ShoppingCart;
import com.hang.reiji.mapper.OrderDetailMapper;
import com.hang.reiji.mapper.ShoppingCartMapper;
import com.hang.reiji.service.ShoppingCartService;
import com.hang.reiji.settings.ExceptionOne;
import com.hang.reiji.utils.UtilOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    ShoppingCartMapper shoppingCartMapper;

    /**
     * 往购物车加东西
     * 先用userID，name查询数据库有没有这个菜
     * 如果没有，插进去。
     * 如果有，把数量加1
     * @param shoppingCart
     * @return
     */
    @Override
    public ShoppingCart selectAndSave(ShoppingCart shoppingCart) {
        boolean ok = true;
        shoppingCart.setUserId(UtilOne.getIdInThread());
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,shoppingCart.getUserId());
        wrapper.eq(ShoppingCart::getName,shoppingCart.getName());
        ShoppingCart cart = shoppingCartMapper.selectOne(wrapper);
        if (cart == null){
            //保存
            shoppingCart.setNumber(1);
            int i = shoppingCartMapper.insert(shoppingCart);
            if (i != 1) ok = false;
        }else {
            //更新
            Integer num = cart.getNumber(); //数量
            num += 1;
            shoppingCart.setNumber(num);
            int i = shoppingCartMapper.update(shoppingCart,wrapper);
            if (i != 1) ok = false;
        }
        cart = shoppingCartMapper.selectOne(wrapper);
        if (!ok || cart == null){
            return null;
        }
        return cart;
    }

    /**
     * 往购物车减东西
     * 传来的参数是dishId或者setmealId
     * 先用userID，加上面的其中一个ID查询数据库
     * 肯定有的，设置数量减1.
     * 如果数量为0了，删除表中的数据
     * 最后把数量设置好，返回前端
     * @param shoppingCart 里面有dishId或者setmealId
     * @return
     */
    @Override
    public ShoppingCart selectAndSave2(ShoppingCart shoppingCart) {
        boolean ok = true;
        shoppingCart.setUserId(UtilOne.getIdInThread());
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,shoppingCart.getUserId());
        wrapper.eq(shoppingCart.getDishId() != null,ShoppingCart::getDishId,shoppingCart.getDishId());
        wrapper.eq(shoppingCart.getSetmealId() != null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        ShoppingCart cart = shoppingCartMapper.selectOne(wrapper);
        if (cart != null){
            //更新
            Integer num = cart.getNumber();
            num -= 1;
            if (num != 0){
                shoppingCart.setNumber(num);
                int i = shoppingCartMapper.update(shoppingCart,wrapper);
                if (i != 1) ok = false;
            }else {
                int i = shoppingCartMapper.deleteById(cart.getId());
                if (i != 1) ok = false;
            }
            cart.setNumber(0);
            return ok ? cart : null;
        }
        throw new ExceptionOne("没有数据！");
    }
}
