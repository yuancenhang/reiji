package com.hang.reiji.dto;

import com.hang.reiji.domain.OrderDetail;
import com.hang.reiji.domain.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {

    private static final long serialVersionUID = 3998205854700804890L;

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
