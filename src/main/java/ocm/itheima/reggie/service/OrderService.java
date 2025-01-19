package ocm.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import ocm.itheima.reggie.dto.OrderDto;
import ocm.itheima.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);

    // 获取订单分页展示
    Page<OrderDto> getPage(Long page, Long pageSize);

    // 后台管理端获取订单分页展示
    Page<OrderDto> getAllPage(Long page, Long pageSize, String number, String beginTime, String endTime);

    // 修改订单状态
    Boolean update(Orders order);
}
