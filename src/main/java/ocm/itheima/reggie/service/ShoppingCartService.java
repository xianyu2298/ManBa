package ocm.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ocm.itheima.reggie.entity.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart> {
    // 购物车商品减一
    Boolean sub(ShoppingCart shoppingCart);
}