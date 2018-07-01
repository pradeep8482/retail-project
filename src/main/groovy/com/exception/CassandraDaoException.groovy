package com.exception

class CassandraDaoException extends RuntimeException {
    ErrorDetail errorDetail

    CassandraDaoException(ErrorDetail errorDetail) {
        super(errorDetail?.userMessage)
        this.errorDetail = errorDetail
    }
}
