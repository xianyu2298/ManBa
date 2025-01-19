package ocm.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ocm.itheima.reggie.common.BaseContext;
import ocm.itheima.reggie.common.CustomExpection;
import ocm.itheima.reggie.dto.OrderDto;
import ocm.itheima.reggie.entity.*;
import ocm.itheima.reggie.mapper.OrderMapper;
import ocm.itheima.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private IUserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param orders
     */
    @Transactional
    public void submit(Orders orders) {
        //获得当前用户id
        Long userId = BaseContext.getCurrentId();

        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapper);

        if(shoppingCarts == null || shoppingCarts.size() == 0){
            throw new CustomExpection("购物车为空，不能下单");
        }

        //查询用户数据
        User user = userService.getById(userId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if(addressBook == null){
            throw new CustomExpection("用户地址信息有误，不能下单");
        }

        long orderId = IdWorker.getId();//订单号

        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        //向订单表插入数据，一条数据
        this.save(orders);

        //向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);

        //清空购物车数据
        shoppingCartService.remove(wrapper);
    }

    // 获取订单分页展示
    @Override
    public Page<OrderDto> getPage(Long page, Long pageSize) {
        // 1.创建分页封装器
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        // 2.创建OrderDto的分页封装器
        Page<OrderDto> dtoPage = new Page<>();

        // 3.创建Orders的查询条件封装器
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        // 3.1 添加查询条件：按下单时间降序排列
        lqw.orderByDesc(Orders::getOrderTime);
        // 3.2 条件查询条件：按当前用户ID查询
        Long userId = BaseContext.getCurrentId();
        lqw.eq(userId != null, Orders::getUserId, userId);
        // 4.Orders分页查询
        this.page(ordersPage, lqw);

        // 5.除了Record都复制
        BeanUtils.copyProperties(ordersPage, dtoPage, "records");

        // 6.获取当前用户所有的order对象
        List<Orders> orders = this.list(lqw);
        // 7.通过stream流逐一包装成OrderDto对象
        List<OrderDto> orderDtos = orders.stream().map(order -> {
            // 7.1 创建OrderDto对象
            OrderDto orderDto = new OrderDto();
            // 7.2 拷贝属性
            BeanUtils.copyProperties(order, orderDto);

            // 检查userName字段，如果为空，则使用consignee的值
            if (StringUtils.isEmpty(orderDto.getUserName())) {
                orderDto.setUserName(orderDto.getConsignee());
            }

            // 7.3 调用OrderDetail业务层获取订单明细集合
            LambdaQueryWrapper<OrderDetail> orderDetailLqw = new LambdaQueryWrapper<>();
            orderDetailLqw.eq(OrderDetail::getOrderId, order.getNumber());
            List<OrderDetail> orderDetails = orderDetailService.list(orderDetailLqw);
            // 7.4 设置orderDto的订单明细属性
            orderDto.setOrderDetails(orderDetails);
            // 7.5 返回orderDto
            return orderDto;
        }).collect(Collectors.toList());

        // 8.设置dtoPage的records属性
        dtoPage.setRecords(orderDtos);
        return dtoPage;
    }

    // 后台管理端获取订单分页展示
    @Override
    public Page<OrderDto> getAllPage(Long page, Long pageSize, String number, String beginTime, String endTime) {
        // 1.创建分页封装器
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        // 2.创建OrderDto的分页封装器
        Page<OrderDto> dtoPage = new Page<>();

        // 3.创建Orders的查询条件封装器
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        // 3.1 添加查询条件：按下单时间降序排列
        lqw.orderByDesc(Orders::getOrderTime);
        // 3.2 添加查询条件：按订单号查询
        lqw.like(number != null, Orders::getNumber, number);
        // 3.3 添加查询条件：  动态SQL-字符串使用StringUtils.isNotEmpty这个方法来判断
        lqw.gt(StringUtils.isNotEmpty(beginTime), Orders::getOrderTime, beginTime);
        lqw.lt(StringUtils.isNotEmpty(endTime), Orders::getOrderTime, endTime);
        // 4.Orders分页查询
        this.page(ordersPage, lqw);

        // 5.除了Record都复制
        BeanUtils.copyProperties(ordersPage, dtoPage, "records");

        // 6.获取当前用户所有的order对象
        List<Orders> orders = this.list(lqw);
        // 7.通过stream流逐一包装成OrderDto对象
        List<OrderDto> orderDtos = orders.stream().map(order -> {
            // 7.1 创建OrderDto对象
            OrderDto orderDto = new OrderDto();
            // 7.2 拷贝属性
            BeanUtils.copyProperties(order, orderDto);

            // 检查userName字段，如果为空，则使用consignee的值
            if (StringUtils.isEmpty(orderDto.getUserName())) {
                orderDto.setUserName(orderDto.getConsignee());
            }

            // 7.3 调用OrderDetail业务层获取订单明细集合
            LambdaQueryWrapper<OrderDetail> orderDetailLqw = new LambdaQueryWrapper<>();
            orderDetailLqw.eq(OrderDetail::getOrderId, order.getNumber());
            List<OrderDetail> orderDetails = orderDetailService.list(orderDetailLqw);
            // 7.4 设置orderDto的订单明细属性
            orderDto.setOrderDetails(orderDetails);
            // 7.5 返回orderDto
            return orderDto;
        }).collect(Collectors.toList());

        // 8.设置dtoPage的records属性
        dtoPage.setRecords(orderDtos);
        return dtoPage;
    }

    // 修改订单状态
    @Override
    public Boolean update(Orders order) {
        return this.updateById(order);
    }
}
