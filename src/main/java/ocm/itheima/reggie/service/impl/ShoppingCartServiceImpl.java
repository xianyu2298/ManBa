package ocm.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ocm.itheima.reggie.common.BaseContext;
import ocm.itheima.reggie.entity.ShoppingCart;
import ocm.itheima.reggie.mapper.ShoppingCartMapper;
import ocm.itheima.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    // 购物车商品减一
    @Override
    public Boolean sub(ShoppingCart shoppingCart) {
        // 1.获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        // 2.创建查询条件封装器
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        // 2.1 添加查询添加：按用户ID查询
        lqw.eq(userId != null, ShoppingCart::getUserId, userId);

        // 3.判断传来的是菜品还是套餐
        if (shoppingCart.getDishId() != null) {
            // 3.1 删除的是菜品
            lqw.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            // 3.2 删除的是套餐
            lqw.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        // 4.查询当前购物车里该菜品/套餐的数量
        ShoppingCart shoppingCartSel = this.getOne(lqw);
        if (1 < shoppingCartSel.getNumber()) {
            // 4.1 数量大于一，直接减一
            shoppingCartSel.setNumber(shoppingCartSel.getNumber() - 1);
            // 4.2更新到数据库
            return this.updateById(shoppingCartSel);
        }

        // 5.其他情况直接从购物车删除此商品
        return this.remove(lqw);
    }
}