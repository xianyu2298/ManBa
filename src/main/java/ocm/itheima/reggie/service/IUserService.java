package ocm.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ocm.itheima.reggie.entity.User;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Map;

public interface IUserService extends IService<User> {

    // 发送邮箱验证码
    Boolean sendMsg(User user, HttpSession session) throws MessagingException;

    // 移动端用户登录
    User login(Map<String, String> map, HttpSession session);
}
