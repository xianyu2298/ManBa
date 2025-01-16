package ocm.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import ocm.itheima.reggie.entity.Dish;
import ocm.itheima.reggie.mapper.DishMapper;
import ocm.itheima.reggie.service.DIshService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DIshService {
}
