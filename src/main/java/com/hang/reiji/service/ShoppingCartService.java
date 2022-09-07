package com.hang.reiji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.reiji.domain.ShoppingCart;
import org.springframework.stereotype.Service;

@Service
public interface ShoppingCartService extends IService<ShoppingCart> {
    ShoppingCart selectAndSave(ShoppingCart shoppingCart);

    ShoppingCart selectAndSave2(ShoppingCart shoppingCart);
}
