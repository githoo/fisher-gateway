package com.fisher.base.gateway.exceptions;


/**
 * Created by fisher
 */
public class ProxyApplactionException extends  RuntimeException {

    private  String errCde;
    private  String errMessage;

    public ProxyApplactionException(String message) {
        super(message);
        this.errMessage = message;
    }

    public ProxyApplactionException(String errCde, String errMessage){
        super();
        this.errMessage = errMessage;
        this.errCde = errCde;
    }

}
