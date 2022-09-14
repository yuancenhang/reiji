package com.hang.reiji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.reiji.domain.Category;
import com.hang.reiji.domain.Dish;
import com.hang.reiji.domain.Setmeal;
import com.hang.reiji.domain.SetmealDish;
import com.hang.reiji.dto.SetmealDto;
import com.hang.reiji.mapper.CategoryMapper;
import com.hang.reiji.mapper.SetmealDishMapper;
import com.hang.reiji.mapper.SetmealMapper;
import com.hang.reiji.service.SetmealService;
import com.hang.reiji.settings.ExceptionOne;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    SetmealMapper setmealMapper;

    @Autowired
    SetmealDishMapper setmealDishMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Value("${filePath.picture}")
    String path;

    /**
     * 保存某个套餐
     * 两张表，setmeal，setmealDish
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean MySave(SetmealDto setmealDto) {
        boolean ok = true;
        //保存setmeal
        int i = setmealMapper.insert(setmealDto);
        if (i != 1) ok = false;
        //保存setmealDish
        List<SetmealDish> list = setmealDto.getSetmealDishes();
        for (SetmealDish s : list){
            s.setSetmealId(setmealDto.getId());
            s.setSort(1);
            i = setmealDishMapper.insert(s);
            if (i != 1) ok = false;
        }
        return ok;
    }

    /**
     * 获取某个套餐和套餐下关联的菜品
     */
    @Override
    public SetmealDto getSetmealDto(Long id) {
        //获取setmeal父类，然后转成子类SetmealDto
        Setmeal setmeal = setmealMapper.selectById(id);
        SetmealDto dto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,dto);
        //根据setmealId获取setmealDish列表
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishMapper.selectList(wrapper);
        //根据Setmeal的categoryId查出categoryName
        Category category = categoryMapper.selectById(setmeal.getCategoryId());
        //塞进Dto
        dto.setSetmealDishes(list);
        dto.setCategoryName(category.getName());
        return dto;
    }

    /**
     * 获取具体的套餐列表，分页，可能包含名字的模糊搜索条件
     */
    @Override
    public IPage<SetmealDto> getMyList(String name, Integer page, Integer pageSize) {
        IPage<Setmeal> iPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null,Setmeal::getName,name);
        setmealMapper.selectPage(iPage,wrapper);
        List<Setmeal> list = iPage.getRecords();
        List<SetmealDto> newList = new ArrayList<>();
        for (Setmeal s : list){
            SetmealDto dto = new SetmealDto();
            BeanUtils.copyProperties(s,dto);
            Category category = categoryMapper.selectById(s.getCategoryId());
            dto.setCategoryName(category.getName());
            newList.add(dto);
        }
        IPage<SetmealDto> dtoIPage = new Page<>();
        dtoIPage.setTotal(iPage.getTotal());
        dtoIPage.setRecords(newList);
        return dtoIPage;
    }

    /**
     * 批量停售和起售
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean changeStatus(String[] ids, Integer flag) {
        boolean ok = true;
        if (flag == 0) {
            for (String id : ids) {
                Setmeal setmeal = new Setmeal();
                setmeal.setId(Long.parseLong(id));
                setmeal.setStatus(0);
                int i = setmealMapper.updateById(setmeal);
                if (i != 1) ok = false;
            }
        }else if (flag == 1){
            for (String id : ids) {
                Setmeal setmeal = new Setmeal();
                setmeal.setId(Long.parseLong(id));
                setmeal.setStatus(1);
                int i = setmealMapper.updateById(setmeal);
                if (i != 1) ok = false;
            }
        }
        return ok;
    }

    /**
     * 批量删除套餐,顺便删除套餐里的菜，也就是setmealDish表里的数据
     * @param ids Setmeal表的ID
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean delete(Long[] ids) {
        boolean ok = true;
        Setmeal setmeal;
        for (Long id : ids) {
            setmeal = setmealMapper.selectById(id);
            if (setmeal.getStatus() == 1){
                throw new ExceptionOne("套餐正在售卖中，不能删除");
            }
            //删除套餐和关联表数据
            int i = setmealMapper.deleteById(id);
            if (i != 1) ok = false;
            LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SetmealDish::getSetmealId,id);
            setmealDishMapper.delete(wrapper);
            //查出Setmeal的图片路径，删除本地图片
            File file = new File(path + setmeal.getImage());
            if (file.exists()) {
                file.delete();
            }
        }
        return ok;
    }

    /**
     * 更新套餐
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean myUpdate(SetmealDto setmealDto) {
        //更新setmeal
        boolean ok = setmealMapper.updateById(setmealDto) == 1;
        //更新setmealDishes
        //删除原有的套餐菜品
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishMapper.delete(wrapper);
        //保存新的菜品
        List<SetmealDish> list = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : list){
            if (setmealDishMapper.insert(setmealDish) != 1) ok = false;
        }
        return ok;
    }
}
