package com.hang.reiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hang.reiji.domain.Dish;
import com.hang.reiji.domain.R;
import com.hang.reiji.domain.Setmeal;
import com.hang.reiji.dto.SetmealDto;
import com.hang.reiji.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    /**
     * 获取具体的套餐列表，分页，可能包含名字的模糊搜索条件
     */
    @GetMapping("/page")
    public R<IPage<SetmealDto>> getList(String name, Integer page, Integer pageSize) {
        IPage<SetmealDto> dtoIPage = setmealService.getMyList(name, page, pageSize);
        return R.success(dtoIPage);
    }

    /**
     * 保存套餐
     */
    @CacheEvict(value = "setmealCache",allEntries = true)
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        boolean ok = setmealService.MySave(setmealDto);
        return ok ? R.success("保存成功") : R.error("保存失败！！！");
    }

    /**
     * 更新套餐
     */
    @CacheEvict(value = "setmealCache",allEntries = true)
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        boolean ok = setmealService.myUpdate(setmealDto);
        return ok ? R.success("套餐修改成功") : R.error("套餐修改失败");
    }

    /**
     * 修改套餐，获取某个套餐和套餐下关联的菜品
     */
    @GetMapping("/{setmealId}")
    public R<SetmealDto> getSetmealDto(@PathVariable Long setmealId) {
        SetmealDto dto = setmealService.getSetmealDto(setmealId);
        return R.success(dto);
    }

    /**
     * 批量起售和停售
     */
    @CacheEvict(value = "setmealCache",allEntries = true)
    @PostMapping("/status/{flag}")
    public R<String> changeStatus(String[] ids, @PathVariable Integer flag) {
        boolean ok = setmealService.changeStatus(ids, flag);
        return ok ? R.success("更改成功") : R.error("更改失败");
    }

    /**
     * 批量删除
     */
    @CacheEvict(value = "setmealCache",allEntries = true)
    @DeleteMapping
    public R<String> delete(Long[] ids) {
        boolean ok = setmealService.delete(ids);
        return ok ? R.success("批量删除成功") : R.error("删除失败");
    }

    /**
     * 根据categoryID获取Setmeal列表
     */
    @Cacheable(value = "setmealCache",key = "#categoryId + '_' + #name")
    @GetMapping("/list")
    public R<List<Setmeal>> getList(Long categoryId, String name) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(categoryId != null, Setmeal::getCategoryId, categoryId);
        wrapper.eq(Setmeal::getStatus,1);
        wrapper.like(name != null, Setmeal::getName, name);
        List<Setmeal> list = setmealService.list(wrapper);
        return list == null ? R.error("没有找到分类") : R.success(list);
    }
}





















