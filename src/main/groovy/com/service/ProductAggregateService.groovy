package com.service

import com.common.TimerApp
import com.domain.ProductData
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@Slf4j
class ProductAggregateService {

    @Autowired
    ProductService productService

    @Autowired
    RedskyProductService redskyProductService

    /**
     * This method makes rest call to redsky gets product name and makes product dao call to get product price
     * @param id
     * @return ProductData
     */
    ProductData getProductDetrails(String id){
        //Make Restcall
       String productName = TimerApp.execute(['productId': id,'methodName':'getProductName'],{redskyProductService.getProductName(id)})
        log.info("ProductId=${id}, productName from RedSky: ${productName}")
       //Make product service call to get product data
        if(productName){
            return TimerApp.execute(['productId': id,'executed':'cassandraMethod'],{productService.getProductPrice(new ProductData(productName: productName , productId: id))})
        }

    }


}
