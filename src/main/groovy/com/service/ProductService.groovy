package com.service

import com.dao.ProductDao
import com.domain.ProductData
import com.exception.CassandraDaoException
import com.exception.ErrorDetail
import com.exception.ProductServiceException
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@Slf4j
class ProductService {

    @Autowired
    ProductDao productDao

    void populateProductData(ProductData productData) {
        productDao.insertProductData(productData)
    }

    /**
     * This method makes product dao call to get product data with product price
     * @param productData
     * @return
     */
    ProductData getProductPrice(ProductData productData) {
        ProductData resultProductData
        try {
            if((!productData?.productName) || (productData?.productName?.isEmpty())){
                throw new ProductServiceException(new ErrorDetail(errorCode: 'NO_PRODUCT_NAME', userMessage: 'Unable to get product name from redsky'))
            }
            resultProductData = productDao.selectProductData(productData)
        } catch (CassandraDaoException cde) {
            throw new ProductServiceException(new ErrorDetail(errorCode: cde?.errorDetail?.errorCode, userMessage: cde?.errorDetail?.userMessage))
        }
        resultProductData
    }
}
