package ocm.itheima.reggie.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import ocm.itheima.reggie.dto.SetmealDto;
import ocm.itheima.reggie.entity.Dish;
import ocm.itheima.reggie.entity.Setmeal;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    //禁用套餐
    void updateStatusByIds(int status, List<Long> ids);





    // 根据ID查询套餐信息及对应的菜品
    SetmealDto getByIdWithSetmeal(Long id);

    // 更新套餐信息
    void updateWithSetmeal(SetmealDto setmealDto);
}
