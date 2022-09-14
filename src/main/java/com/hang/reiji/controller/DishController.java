package com.hang.reiji.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hang.reiji.domain.*;
import com.hang.reiji.dto.DishDto;
import com.hang.reiji.service.CategoryService;
import com.hang.reiji.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    /*@Autowired
    private CacheManager cacheManager;*/

    /*
    先在启动类上加@EnableCaching注解，表示可以使用缓存注解
    然后在具体的地方使用缓存注解
    @Cacheable，在方法执行前先查看缓存中有没有，如果有，直接返回。如果没有，执行方法，并将方法返回值放入缓存中。一般用在查询方法上
    @CachePut，将方法的返回值放入缓存中，一般用在新增方法上
    @CacheEvict，将一条或多条数据从缓存中清除，一般用在删除方法上
    对于本项目而言，缓存主要是前台点餐数据，而这些数据都是属于某个大分类，是根据大分类如categoryId，setmealId等来查询，也是根据这些分类来缓存，
    所以新增和删除等涉及具体DishId的操作，是无法单独放入缓存，无法单独从缓存中删除的，所以当新增和删除的时候，就用@CacheEvict把整个大分类的缓存全部清除
     */

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
    @CacheEvict(value = "dishCache",allEntries = true) //清除dishCache分类下的全部缓存
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        boolean ok = dishService.Mysave(dishDto);
        return ok ? R.success("保存成功") : R.error("保存菜品失败！！！");
    }

    /**
     * 更新菜品
     */
    @CacheEvict(value = "dishCache",allEntries = true)
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        boolean ok = dishService.myUpdate(dishDto);
        return ok ? R.success("菜品修改成功") : R.error("菜品修改失败");
    }

    /**
     * 批量删除，可能一个，可能多个
     */
    @CacheEvict(value = "dishCache",allEntries = true) //清除dishCache分类下的全部缓存
    @DeleteMapping
    public R<String> delete(String[] ids) {
        dishService.delete(ids);
        return R.success("批量删除成功");
    }

    /**
     * 获取单个菜品,从两张表获取，Dish,DishFlavor
     * 这个是后台用查询的，就不缓存了
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {
        DishDto dishDto = dishService.getDishAndDishFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 批量起售和停售
     */
    @CacheEvict(value = "dishCache",allEntries = true) //清除dishCache分类下的全部缓存
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
    @Cacheable(value = "dishCache",key = "#categoryId + '_' + #name")
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
