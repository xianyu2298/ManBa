package ocm.itheima.reggie.dto;

import lombok.Data;
import ocm.itheima.reggie.entity.Dish;
import ocm.itheima.reggie.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
