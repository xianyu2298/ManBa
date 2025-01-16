package ocm.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ocm.itheima.reggie.common.CustomExpection;
import ocm.itheima.reggie.entity.Category;
import ocm.itheima.reggie.entity.Dish;
import ocm.itheima.reggie.entity.Setmeal;
import ocm.itheima.reggie.mapper.CategoryMapper;
import ocm.itheima.reggie.service.CategoryService;
import ocm.itheima.reggie.service.DIshService;
import ocm.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public  class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>implements CategoryService {

    @Autowired
    private DIshService dIshService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param ids
     */
    @Override
    public void remove(Long ids){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        int count1 = dIshService.count(dishLambdaQueryWrapper);

        //查询分类是否关联菜品，如果已关联，抛出业务异常
        if (count1>0){
            //已经关联菜品，抛出异常
            throw new CustomExpection("当前分类下关联菜品，不可删除");
        }

        //查询分类是否关联套餐，如果已关联，抛出业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        int count2 = dIshService.count(dishLambdaQueryWrapper);
        if (count2>0){
            //已经关联套餐，抛出异常
            throw new CustomExpection("当前分类下关联套餐，不可删除");
        }

        //正常删除分类
        super.removeById(ids);
    }
}
