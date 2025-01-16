package ocm.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ocm.itheima.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    public  void remove(Long ids);
}
