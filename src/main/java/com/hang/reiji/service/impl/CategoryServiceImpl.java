package com.hang.reiji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.reiji.domain.Category;
import com.hang.reiji.domain.Dish;
import com.hang.reiji.domain.Setmeal;
import com.hang.reiji.mapper.CategoryMapper;
import com.hang.reiji.mapper.DishMapper;
import com.hang.reiji.mapper.SetmealMapper;
import com.hang.reiji.service.CategoryService;
import com.hang.reiji.settings.ExceptionOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    @Autowired
    DishMapper dishMapper;

    @Autowired
    SetmealMapper setmealMapper;

    /**
     * 删除分类
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId,id);
        Long i = dishMapper.selectCount(wrapper);
        if (i != 0){
            throw new ExceptionOne("该分类下有菜品，无法删除！！！");
        }
        LambdaQueryWrapper<Setmeal> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Setmeal::getCategoryId,id);
        i = setmealMapper.selectCount(wrapper1);
        if (i != 0){
            throw new ExceptionOne("该分类下有套餐，无法删除！！！");
        }
        super.removeById(id);
    }
}
