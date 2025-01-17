package ocm.itheima.reggie.dto;

import ocm.itheima.reggie.entity.Setmeal;
import ocm.itheima.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
