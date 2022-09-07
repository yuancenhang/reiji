package com.hang.reiji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.reiji.domain.*;
import com.hang.reiji.dto.OrdersDto;
import com.hang.reiji.mapper.*;
import com.hang.reiji.service.OrdersService;
import com.hang.reiji.settings.ExceptionOne;
import com.hang.reiji.utils.UtilOne;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AddressBookMapper addressBookMapper;

    @Autowired
    ShoppingCartMapper shoppingCartMapper;

    @Autowired
    OrderDetailMapper orderDetailMapper;

    /**
     * 下单
     * 说明：这里的下单相当于直接付款。Status为2.也就是没有待付款的步骤。
     * @param orders
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean mySave(Orders orders) {
        //生成订单号
        String ddh = UtilOne.getDDH();
        orders.setNumber(ddh);
        //新订单状态为代付款
        orders.setStatus(2);
        //userId
        orders.setUserId(UtilOne.getIdInThread());
        //userName
        User user = userMapper.selectById(UtilOne.getIdInThread());
        orders.setUserName(user.getName());
        //实收金额amount
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,UtilOne.getIdInThread());
        List<ShoppingCart> list = shoppingCartMapper.selectList(wrapper);
        //填入总价
        BigDecimal total = new BigDecimal("0.00");
        for (ShoppingCart s : list){
            BigDecimal price = s.getAmount();  //单价
            BigDecimal number = UtilOne.toBigDecimal(s.getNumber()); //数量
            total = total.add(price.multiply(number));  //总价
        }
        orders.setAmount(total);
        //phone
        orders.setPhone(user.getPhone());
        //address
        AddressBook addressBook = addressBookMapper.selectById(orders.getAddressBookId());
        orders.setAddress(UtilOne.getAddress(addressBook));
        //收货人consignee
        orders.setConsignee(addressBook.getConsignee());
        //保存
        boolean ok = ordersMapper.insert(orders) == 1;
        /*
        创建订单详细OrderDetail
        private Long id;
        private String name;
        private Long orderId;
        private Long dishId;
        private Long setmealId;
        private String dishFlavor;
        private Integer number;
        private BigDecimal amount;
        private String image;
         */
        for (ShoppingCart s : list){
            OrderDetail orderDetail = new OrderDetail();
            //填入Name
            orderDetail.setName(s.getName());
            //填入ordersId
            LambdaQueryWrapper<Orders> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(Orders::getNumber,ddh);
            Orders orders1 = ordersMapper.selectOne(wrapper2);
            orderDetail.setOrderId(orders1.getId());
            //填入dishId
            orderDetail.setDishId(s.getDishId());
            //填入setmealId
            orderDetail.setSetmealId(s.getSetmealId());
            //填入DishFlavor
            orderDetail.setDishFlavor(s.getDishFlavor());
            //填入number
            orderDetail.setNumber(s.getNumber());
            //填入amount，总价
            BigDecimal price = s.getAmount();  //单价
            BigDecimal number = UtilOne.toBigDecimal(s.getNumber()); //数量
            orderDetail.setAmount(price.multiply(number));
            //填入image
            orderDetail.setImage(s.getImage());
            //保存
            int i = orderDetailMapper.insert(orderDetail);
            if (i != 1) throw new ExceptionOne("创建订单详细失败！");
        }
        //清空购物车
        shoppingCartMapper.delete(wrapper);
        if (!ok) throw new ExceptionOne("出错了！");
        return true;
    }

    /**
     * 获取订单列表，参数userId 返回OrdersDto
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public IPage<OrdersDto> getPageDto(int page, int pageSize) {
        IPage<Orders> iPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, UtilOne.getIdInThread());
        wrapper.orderByDesc(Orders::getOrderTime);
        ordersMapper.selectPage(iPage,wrapper);

        /*orderDto的属性
            private String userName;
            private String phone;
            private String address;
            private String consignee;
            private List<OrderDetail> orderDetails;
         */
        IPage<OrdersDto> iPage1 = new Page<>();
        BeanUtils.copyProperties(iPage,iPage1);
        List<Orders> list = iPage.getRecords();
        List<OrdersDto> list1 = new ArrayList<>();
        for (Orders o : list){
            OrdersDto dto = new OrdersDto();
            BeanUtils.copyProperties(o,dto);
            //插入用户名字,电话
            User user = userMapper.selectById(UtilOne.getIdInThread());
            dto.setUserName(user.getName());
            dto.setPhone(user.getPhone());
            //插入地址
            LambdaQueryWrapper<AddressBook> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(AddressBook::getUserId,UtilOne.getIdInThread());
            AddressBook addressBook = addressBookMapper.selectOne(wrapper1);
            dto.setAddress(UtilOne.getAddress(addressBook));
            //插入orderDetails
            LambdaQueryWrapper<OrderDetail> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(OrderDetail::getOrderId,o.getId());
            List<OrderDetail> orderDetails = orderDetailMapper.selectList(wrapper2);
            dto.setOrderDetails(orderDetails);
            list1.add(dto);
        }
        iPage1.setRecords(list1);
        return iPage1;
    }
}
