package com.exception

class RedSkyException extends RuntimeException{

    ErrorDetail errorDetail

    RedSkyException(ErrorDetail errorDetail){
        super(errorDetail?.userMessage)
        this.errorDetail = errorDetail
    }
}
