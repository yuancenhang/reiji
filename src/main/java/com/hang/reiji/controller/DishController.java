package com.hang.reiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hang.reiji.domain.*;
import com.hang.reiji.dto.DishDto;
import com.hang.reiji.service.CategoryService;
import com.hang.reiji.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    DishService dishService;

    @Autowired
    CategoryService categoryService;

    /**
     * 获取菜品列表，可能会有name条件
     */
    @GetMapping("/page")
    public R<IPage<DishDto>> getList(String name, int page, int pageSize) {
        IPage<DishDto> iPage = dishService.getMyList(name,page,pageSize);
        return R.success(iPage);
    }

    /**
     * 保存菜品。菜品里有包含了口味DishFlavor，所以要保存在两张表上。
     * DishDto是一个继承了Dish的实体类，因为Dish类不够封装传来的参数，所以用了Dto
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        System.out.println("............................" + dishDto.getName());
        boolean ok = dishService.Mysave(dishDto);
        return ok ? R.success("保存成功") : R.error("保存菜品失败！！！");
    }

    /**
     * 批量删除，可能一个，可能多个
     */
    @DeleteMapping
    public R<String> delete(String[] ids) {
        dishService.delete(ids);
        return R.success("批量删除成功");
    }

    /**
     * 获取单个菜品,从两张表获取，Dish,DishFlavor
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {
        DishDto dishDto = dishService.getDishAndDishFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 批量起售和停售
     */
    @PostMapping("/status/{flag}")
    public R<String> changeStatus(Long[] ids, @PathVariable Integer flag) {
        List<Dish> list = new ArrayList<>();
        if (flag == 0) {
            for (Long id : ids) {
                Dish dish = new Dish();
                dish.setId(id);
                dish.setStatus(0);
                list.add(dish);
            }
        }else if (flag == 1){
            for (Long id : ids) {
                Dish dish = new Dish();
                dish.setId(id);
                dish.setStatus(1);
                list.add(dish);
            }
        }
        boolean ok = dishService.updateBatchById(list);
        return ok ? R.success("更改成功") : R.error("更改失败");
    }

    /**
     * 获取某个套餐分类下的所有菜品，可能包含categoryId,name条件
     * 应该只能包含一个，不会两个同时有
     */
    @GetMapping("/list")
    public R<List<DishDto>> getCategoryList(Long categoryId, String name){
        List<DishDto> list = dishService.getDishDto(categoryId,name);
        return list.isEmpty() ? R.error("该分类下还没有菜品噢！") : R.success(list);
    }
    /*@GetMapping("/list")
    public R<List<Dish>> getCategoryList(Long categoryId, String name){
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq(Dish::getCategoryId,categoryId);
        } else {
            wrapper.like(name != null,Dish::getName,name);
        }
        List<Dish> list = dishService.list(wrapper);
        return list == null ? R.error("没有找到分类") : R.success(list);
    }*/
}
