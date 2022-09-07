package com.hang.reiji.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.reiji.domain.Orders;
import com.hang.reiji.dto.OrdersDto;

public interface OrdersService extends IService<Orders> {
    boolean mySave(Orders orders);

    IPage<OrdersDto> getPageDto(int page, int pageSize);
}
