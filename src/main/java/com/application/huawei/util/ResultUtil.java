package com.application.huawei.util;

import lombok.Data;

/**
 * @Auther: 10199
 * @Date: 2019/10/30 18:41
 * @Description: 统一的Rest相应对象，包含成功 错误等信息，还有数据
 */

@Data
public class ResultUtil {
    public static int SUCCESS_CODE = 1;
    public static int FAILE_CODE = 0;

    private int code;
    private String message;
    private Object data;

    private ResultUtil(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResultUtil success(){
        return new ResultUtil(SUCCESS_CODE, null, null);
    }

    public static ResultUtil success(Object data) {
        return new ResultUtil(SUCCESS_CODE, "", data);
    }

    public static ResultUtil fail(String message){
        return new ResultUtil(FAILE_CODE, message, null);
    }
}
