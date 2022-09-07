package com.hang.reiji.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.reiji.domain.Setmeal;
import com.hang.reiji.dto.SetmealDto;

public interface SetmealService extends IService<Setmeal> {
    boolean MySave(SetmealDto setmealDto);

    SetmealDto getSetmealDto(Long id);

    IPage<SetmealDto> getMyList(String name, Integer page, Integer pageSize);

    boolean changeStatus(String[] ids, Integer flag);

    boolean delete(Long[] ids);
}
