package ocm.itheima.reggie.dto;

import lombok.Data;
import ocm.itheima.reggie.entity.OrderDetail;
import ocm.itheima.reggie.entity.Orders;

import java.util.List;
@Data

public class OrderDto extends Orders {
    private List<OrderDetail> orderDetails;

}
