package ocm.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ocm.itheima.reggie.entity.OrderDetail;
import ocm.itheima.reggie.mapper.OrderDetailMapper;
import ocm.itheima.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
