package ocm.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ocm.itheima.reggie.entity.ShoppingCart;
import ocm.itheima.reggie.mapper.ShoppingCartMapper;
import ocm.itheima.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}