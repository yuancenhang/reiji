package com.hang.reiji.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.reiji.domain.Dish;
import com.hang.reiji.dto.DishDto;

import java.util.List;

public interface DishService extends IService<Dish> {
    boolean Mysave(DishDto dishDto);

    boolean delete(String[] ids);

    DishDto getDishAndDishFlavor(Long id);

    IPage<DishDto> getMyList(String name, int page, int pageSize);

    List<DishDto> getDishDto(Long categoryId, String name);
}
