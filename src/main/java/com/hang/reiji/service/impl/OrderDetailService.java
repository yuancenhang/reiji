package com.hang.reiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.reiji.domain.OrderDetail;
import com.hang.reiji.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailService extends ServiceImpl<OrderDetailMapper, OrderDetail> implements com.hang.reiji.service.OrderDetailService {
}
