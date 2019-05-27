package com.example.common.response;

import java.util.HashMap;

/**
 * @Author:sxq
 * @Date: 2019/5/26
 * @Description:
 */
public class MetaResponse extends HashMap {
    
    private static final Integer SUCC_CODE = 200;
    private static final String SUCC_MSG = "操作成功！";
    private static final Integer ERROR_CODE = 555;
    private static final String ERROR_MSG = "操作失败！";
    
    public static MetaResponse success(){
        return success(SUCC_MSG);
    }

    public static MetaResponse success(Object data) {
        MetaResponse response = new MetaResponse();
        response.put("code",SUCC_CODE);
        response.put("data",data);
        return response;
    }
    public static MetaResponse error(){
        return success(ERROR_MSG);
    }

    public static MetaResponse error(Object data) {
        MetaResponse response = new MetaResponse();
        response.put("code",ERROR_CODE);
        response.put("data",data);
        return response;
    }
}


