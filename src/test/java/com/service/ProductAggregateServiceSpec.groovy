package com.service

import com.domain.ProductData
import com.domain.ProductPrice
import com.exception.ErrorDetail
import com.exception.ProductServiceException
import spock.lang.Specification

class ProductAggregateServiceSpec extends Specification {

    ProductService productService
    RedskyProductService redskyProductService
    ProductAggregateService productAggregateService

    def setup() {
        productService = Mock(ProductService.class)
        redskyProductService = Mock(RedskyProductService.class)
        productAggregateService = new ProductAggregateService(redskyProductService:redskyProductService,productService:productService)
    }

    def "Test getProductDetrails"() {
        given:
        ProductData productData = null
        when:
        productData = productAggregateService.getProductDetrails('12345')
        then:

        redskyProductService.getProductDetail(_) >> 'Blu Ray'
        productService.getProductPrice(_ as ProductData) >> new ProductData(productId: '12345', productName: 'Blu Ray', productPrice: new ProductPrice(price: 25, currency: 'USD'))

        productData?.productPrice?.price == 25

        where:
        scenrio | productName | eligbileiy
        'scenario1' | '' | 'eligble'
        'scenario2' | '' | 'ineligble'


    }

    def "Test getProductDetrails no productName"() {
        given:
        ProductData productData = null
        ErrorDetail errorDetail
        when:
        try{
            productData = productAggregateService.getProductDetrails('12345')
        }catch(ProductServiceException pse){
            errorDetail = pse.errorDetail
        }
        then:

        redskyProductService.getProductDetail(_) >> null
        productService.getProductPrice(_ as ProductData) >>  { throw new ProductServiceException(new ErrorDetail(errorCode: 'NO_PRODUCT_NAME', userMessage: 'Unable to get product name from redsky')) }

        errorDetail?.errorCode == 'NO_PRODUCT_NAME'
    }

    def "Test getProductDetrails empty productName"() {
        given:
        ProductData productData = null
        ErrorDetail errorDetail
        when:
        try{
            productData = productAggregateService.getProductDetrails('12345')
        }catch(ProductServiceException pse){
            errorDetail = pse.errorDetail
        }
        then:

        redskyProductService.getProductDetail(_) >> ''
        productService.getProductPrice(_ as ProductData) >>  { throw new ProductServiceException(new ErrorDetail(errorCode: 'NO_PRODUCT_NAME', userMessage: 'Unable to get product name from redsky')) }

        errorDetail?.errorCode == 'NO_PRODUCT_NAME'
    }
}
