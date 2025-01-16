package ocm.itheima.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* 全局异常处理
* */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /*
    * 异常处理方法
    * */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> expectionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")) {
            String message = ex.getMessage();
            // 使用正则提取 'zhangsan'
            Pattern pattern = Pattern.compile("Duplicate entry '(.*?)'");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                String value = matcher.group(1); // 提取的值，比如 'zhangsan'
                return R.error(value + " 已存在");
            }
        }

        return R.error("未知错误");
    }

    /*
     * 菜品套餐异常处理方法
     * */
    @ExceptionHandler(CustomExpection.class)
    public R<String> expectionHandler(CustomExpection ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
