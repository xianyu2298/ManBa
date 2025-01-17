package ocm.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import ocm.itheima.reggie.common.R;
import ocm.itheima.reggie.dto.DishDto;
import ocm.itheima.reggie.entity.Category;
import ocm.itheima.reggie.entity.Dish;
import ocm.itheima.reggie.entity.DishFlavor;
import ocm.itheima.reggie.service.CategoryService;
import ocm.itheima.reggie.service.DIshService;
import ocm.itheima.reggie.service.DishFlavorService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DIshService dIshService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dIshService.saveWithFlavor(dishDto);

        return  R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper <Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dIshService.page(pageInfo,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item)->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();
            //根据ID查
            Category category= categoryService.getById(categoryId);

            if (category !=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }


            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据ID查询菜品信息以及对应口味
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get (@PathVariable Long id){

        DishDto dishDto = dIshService.getByIdWithFlavor(id);

        return R.success(dishDto);

    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dIshService.updateWithFlavor(dishDto);

        return  R.success("修改菜品成功");
    }

    /**
     * 将商品停售
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<Boolean> stopSell(String ids){

        if (StringUtils.isBlank(ids)){
            return R.error("参数传递不正确！");
        }
        boolean result = dIshService.stopSell(ids);
        if (!result){
            R.error("停售失败");
        }
        return R.success(true);
    }

    /**
     * 将商品起售
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<Boolean> startSell(String ids){

        if (StringUtils.isBlank(ids)){
            return R.error("参数传递不正确！");
        }
        boolean result = dIshService.startSell(ids);
        if (!result){
            R.error("起售失败");
        }
        return R.success(true);
    }

    /**
     * 根据id删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(String ids){
        log.info("删除的ids为：{}",ids);
        dIshService.remove(ids);
        return R.success("菜品信息删除成功");
    }


    /**
     * 根据条件查询对应菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        log.info("dish:{}", dish);
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(dish.getName()), Dish::getName, dish.getName());
        queryWrapper.eq(null != dish.getCategoryId(), Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        List<Dish> dishs = dIshService.list(queryWrapper);

        List<DishDto> dishDtos = dishs.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, item.getId());

            dishDto.setFlavors(dishFlavorService.list(wrapper));
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtos);
    }
}
