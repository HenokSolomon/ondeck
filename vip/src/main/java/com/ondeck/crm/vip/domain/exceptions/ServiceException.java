package com.ondeck.crm.vip.domain.exceptions;

public class ServiceException extends RuntimeException {

    public static final String UNKNOWN_SERVICE_ERROR = "vip_01";
    public static final String INVALID_CREATE_VIP_REQUEST = "vip_02";
    public static final String INVALID_EMAIL_FORMAT = "vip_03";
    public static final String DUPLICATE_EMAIL = "vip_04";
    public static final String INVALID_VIP_RECORD = "vip_05";


    private String errorCode;


    public ServiceException(String message) {
        super( message );
        this.errorCode = UNKNOWN_SERVICE_ERROR;
    }


    public ServiceException(String message, String errorCode) {
        super( message );
        this.errorCode = errorCode;
    }


}
