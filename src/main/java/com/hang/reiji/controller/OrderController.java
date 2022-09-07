package com.hang.reiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hang.reiji.domain.OrderDetail;
import com.hang.reiji.domain.Orders;
import com.hang.reiji.domain.R;
import com.hang.reiji.dto.OrdersDto;
import com.hang.reiji.service.OrdersService;
import com.hang.reiji.utils.UtilOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrdersService ordersService;


    /**
     * 后端用的
     * 获取订单列表，返回Dto的封装
     */
    @GetMapping("/page")
    public R<IPage<OrdersDto>> getList(int page, int pageSize, @RequestBody Orders orders){
        return null;
    }

    /**
     * 获取最新的订单，不知道是不是只要一单，但还是使用了分页
     * @return 分页
     */
    @GetMapping("/userPage")
    public R<IPage<OrdersDto>> getLastOrder(int page,int pageSize){
        IPage<OrdersDto> iPage = ordersService.getPageDto(page,pageSize);
        return iPage.getRecords().isEmpty() ? R.error("还没有订单噢") : R.success(iPage);
    }

    /**
     * 下单
     * 保存到数据库，但是有些属性是空的
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        boolean ok = ordersService.mySave(orders);
        return ok ? R.success("下单成功") : R.error("下单失败");
    }


}
















