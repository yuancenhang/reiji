package com.hang.reiji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.reiji.domain.Category;

public interface CategoryService extends IService<Category> {
    void delete(Long id);
}
