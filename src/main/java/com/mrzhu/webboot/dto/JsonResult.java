package com.mrzhu.webboot.dto;

import java.util.HashMap;

/**
 * 
 * Created by mrzhu on 8/25/15.
 */
public class JsonResult extends HashMap {

    public void setResult(Boolean result){
        this.put("result",result);
    }

    public void setMessage(String message){
        this.put("message",message);
    }

}
