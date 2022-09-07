package com.hang.reiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hang.reiji.domain.Category;
import com.hang.reiji.domain.R;
import com.hang.reiji.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 保存菜品分类
     */
    @PostMapping
    public R<String> add(@RequestBody Category category) {
        boolean ok = categoryService.save(category);
        return ok ? R.success("添加菜品分类成功") : R.error("菜品添加失败");
    }

    /**
     * 显示菜品类型,分页
     */
    @GetMapping("/page")
    public R<IPage<Category>> getList(int page, int pageSize) {
        IPage<Category> iPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        categoryService.page(iPage, wrapper);
        return R.success(iPage);
    }

    /**
     * 更新菜品分类
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category.getId() != null, Category::getId, category.getId());
        boolean ok = categoryService.update(category, wrapper);
        return ok ? R.success("修改菜品分类成功") : R.error("修改菜品分类失败");
    }

    /**
     * 删除分类
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
        categoryService.delete(ids);
        return R.success("删除成功");
    }

    /**
     *获取菜品或套餐的分类列表，填入下拉框
     */
    @GetMapping("/list")
    public R<List<Category>> getList(Category category) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category.getType() != null, Category::getType, category.getType());
        List<Category> list = categoryService.list(wrapper);
        return R.success(list);
    }
}
