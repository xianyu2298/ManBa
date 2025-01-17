package ocm.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ocm.itheima.reggie.dto.DishDto;
import ocm.itheima.reggie.entity.Dish;

public interface DIshService extends IService<Dish> {

    //新增菜品,同时插入口味数据，需要操作两张表dish dish_flavor

    public void  saveWithFlavor (DishDto dishDto);

    //根据ID查询菜品信息以及口味
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，同时更新口味
    public void updateWithFlavor(DishDto dishDto);

    //菜品停售
    boolean stopSell(String ids);
    //菜品起售
    boolean startSell(String ids);

    //删除和批量删除菜品信息

    public void remove(String ids);
}
