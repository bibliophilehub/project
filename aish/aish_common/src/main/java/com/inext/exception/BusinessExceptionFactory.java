package com.inext.exception;


import com.inext.enums.FinanceTransferErrorEnum;

/**
 * 异常工厂类
 * @author cjh
 */
public class BusinessExceptionFactory {

    public static BusinessException create(FinanceTransferErrorEnum errorEnum, String... args){
        String message = errorEnum.getMessage();
        if(args.length > 0) {
            message = String.format(message, args);
        }
        return new BusinessException(errorEnum.getCode(), message);
    }


    public static BusinessException create(String errorCode, String... args){
        FinanceTransferErrorEnum errorEnum = FinanceTransferErrorEnum.getErrorEnum(errorCode);
        String message = errorEnum.getMessage();
        if(args.length > 0) {
            message = String.format(message, args);
        }
        return new BusinessException(errorEnum.getCode(), message);
    }




}
