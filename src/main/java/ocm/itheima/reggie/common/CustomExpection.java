package ocm.itheima.reggie.common;

/**
 * 自定义业务异常类
 */
public class CustomExpection extends RuntimeException{
    public CustomExpection(String message){
        super(message);
    }
}
