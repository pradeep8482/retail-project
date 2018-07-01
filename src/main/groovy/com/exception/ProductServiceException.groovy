package com.exception

class ProductServiceException extends RuntimeException {
    ErrorDetail errorDetail

    ProductServiceException(ErrorDetail errorDetail){
        super(errorDetail?.userMessage)
        this.errorDetail = errorDetail
    }
}
