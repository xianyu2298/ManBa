package ocm.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ocm.itheima.reggie.common.R;
import ocm.itheima.reggie.dto.OrderDto;
import ocm.itheima.reggie.entity.Orders;
import ocm.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    // 获取订单分页展示
    @GetMapping("/userPage")
    public R<Page<OrderDto>> getPage(Long page, Long pageSize) {
        return R.success(orderService.getPage(page, pageSize));
    }

    // 后台管理端获取订单分页展示
    @GetMapping("/page")
    public R<Page<OrderDto>> page(Long page, Long pageSize, String number, String beginTime, String endTime) {
        return R.success(orderService.getAllPage(page, pageSize, number, beginTime, endTime));
    }

    // 修改订单状态
    @PutMapping
    public R<String> update(@RequestBody Orders order) {
        if (orderService.update(order)) {
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }
}