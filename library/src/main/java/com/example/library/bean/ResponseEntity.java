package com.example.library.bean;

/**
 * author:  ycl
 * date:  2019/06/04 11:04
 * desc:
 */
public class ResponseEntity {
    private String resultCode;
    private String errorCode;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
