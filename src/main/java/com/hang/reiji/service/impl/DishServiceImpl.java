package com.hang.reiji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.reiji.domain.*;
import com.hang.reiji.dto.DishDto;
import com.hang.reiji.mapper.*;
import com.hang.reiji.service.DishService;
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
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    DishMapper dishMapper;

    @Autowired
    DishFlavorMapper dishFlavorMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    SetmealDishMapper setmealDishMapper;

    @Value("${filePath.picture}")
    String path;


    /**
     * 保存菜品。菜品里有包含了口味DishFlavor，所以要保存在两张表上。
     * DishDto是一个继承了Dish的实体类，因为Dish类不够封装传来的参数，所以用了Dto
     * 多表操作，需要事务
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean Mysave(DishDto dishDto) {
        boolean ok = true;
        //保存菜品Dish
        int i = dishMapper.insert(dishDto);
        if (i != 1) ok = false;
        //口味DishFlavor中缺少了一些参数,补上
        List<DishFlavor> list = dishDto.getFlavors();
        for (DishFlavor d : list) {
            d.setDishId(dishDto.getId());
            i = dishFlavorMapper.insert(d);
            if (i != 1) ok = false;
        }
        return ok;
    }

    /**
     * 批量删除Dish，可能一个，可能多个
     * 关联了DishFlavor，所以也要根据DishId删除DishFlavor表
     * 如果这个菜品加入了套餐，那么不能删除
     * 开启事务
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean delete(String[] ids) {
        boolean ok = true;
        Dish dish;
        for(String s : ids){
            dish = dishMapper.selectById(s);
            if (dish.getStatus() == 1){
                throw new ExceptionOne("菜品正在售卖中，不能删除");
            }
            //根据id查setmealDish表，如果数量不为1，说明关联了套餐，不能删除
            LambdaQueryWrapper<SetmealDish> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(SetmealDish::getDishId,s);
            Long count = setmealDishMapper.selectCount(wrapper1);
            if (count != 0){
                throw new ExceptionOne("菜品关联了套餐，不能删除");
            }
            //删除两张表中的数据
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId,s);
            int i = dishFlavorMapper.delete(wrapper);
            int j = dishMapper.deleteById(s);
            if (i != 1 || j!= 1) ok = false;
            //查出dish的图片路径，删除本地图片
            File file = new File(path + dish.getImage());
            if (file.exists()) {
                file.delete();
            }
        }
        return ok;
    }

    /**
     * 获取单个菜品,从两张表获取，Dish,DishFlavor
     */
    @Override
    public DishDto getDishAndDishFlavor(Long id) {
        /*
        先查Dish表
        dishMapper的返回值是Dish，而最后需要返回一个DishDto，
        也就是需要把Dish转成DishDto，属于父类转子类。
        自己写方法很麻烦，用现成的工具，BeanUtils
         */
        DishDto dto = new DishDto();
        Dish dish = dishMapper.selectById(id);
        BeanUtils.copyProperties(dish,dto);
        //再查DishFlavor表，把口味属性flavors塞进去
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorMapper.selectList(wrapper);
        dto.setFlavors(list);
        return dto;
    }
    /**
     * 获取Dish的list，但是Dish不能封装，所以必须用dto来封装category的name
     */
    @Override
    public IPage<DishDto> getMyList(String name, int page, int pageSize) {
        //查出Dish的List，这个list在ipage里面
        IPage<Dish> iPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Dish::getName, name);
        dishMapper.selectPage(iPage, wrapper);

        //把Dish的List遍历，塞进新DishDto的List
        List<Dish> list = iPage.getRecords();
        List<DishDto> newList = new ArrayList<>();

        for (Dish d : list){
            DishDto dto = new DishDto();
            BeanUtils.copyProperties(d,dto);
            Category category = categoryMapper.selectById(d.getCategoryId());
            dto.setCategoryName(category.getName());
            newList.add(dto);
        }
        //新建一个ipage
        IPage<DishDto> iPage1 = new Page<>();
        iPage1.setTotal(iPage.getTotal());
        iPage1.setRecords(newList);
        return iPage1;
    }

    @Override
    public List<DishDto> getDishDto(Long categoryId, String name) {
        //查Dish
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getStatus,1);
        wrapper.eq(categoryId != null,Dish::getCategoryId,categoryId);
        wrapper.like(name != null,Dish::getName,name);
        List<Dish> list = dishMapper.selectList(wrapper);
        List<DishDto> newList = new ArrayList<>();
        for (Dish d : list){
            //查dishFlavor
            LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(d.getId() != null,DishFlavor::getDishId,d.getId());
            List<DishFlavor> dishFlavorList = dishFlavorMapper.selectList(wrapper1);
            //查categoryName
            Category category = categoryMapper.selectById(categoryId);
            //把Dish属性转存到DishDto，把DishFlavor放进去,把categoryName放进去
            DishDto dto = new DishDto();
            BeanUtils.copyProperties(d,dto);
            dto.setFlavors(dishFlavorList);
            dto.setCategoryName(category.getName());
            newList.add(dto);
        }
        return newList;
    }

    /**
     * 修改菜品
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean myUpdate(DishDto dishDto) {
        //先把Dish更新
        boolean ok = true;
        if(dishMapper.updateById(dishDto) != 1) ok = false;
        //再把DishFlavor更新
        //删除原有的口味
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorMapper.delete(wrapper);
        //把新口味保存
        List<DishFlavor> list = dishDto.getFlavors();
        for (DishFlavor dishFlavor : list){
            if (dishFlavorMapper.insert(dishFlavor) != 1) ok = false;
        }
        return ok;
    }
}


















