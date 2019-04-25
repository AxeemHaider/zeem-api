package org.octabyte.zeem.API.Entity;

public class WrappedString {

    private String response;
    private String errorCode;

    public WrappedString() {
    }

    public WrappedString(String response) {
        this.response = response;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
