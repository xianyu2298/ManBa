package ocm.itheima.reggie.controller;

import lombok.extern.slf4j.Slf4j;
import ocm.itheima.reggie.common.R;
import ocm.itheima.reggie.entity.User;
import ocm.itheima.reggie.service.IUserService;
import ocm.itheima.reggie.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {


    @Autowired
    private IUserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) throws MessagingException {
        if (userService.sendMsg(user, session)) {
            return R.success("验证码发送成功");
        }
        return R.error("验证码发送失败");
    }

    // 移动端用户登录登录
    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String, String> map, HttpSession session) {
        User user = userService.login(map, session);
        return R.success(user);
    }
}
