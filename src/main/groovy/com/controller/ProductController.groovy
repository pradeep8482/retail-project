package com.controller

import com.domain.ProductData
import com.exception.ErrorDetail
import com.exception.ProductServiceException
import com.exception.RedSkyException
import com.service.ProductAggregateService
import com.service.ProductService
import groovy.util.logging.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path('/v1')
@Consumes([MediaType.APPLICATION_JSON])
@Produces([MediaType.APPLICATION_JSON])
@Component
@Slf4j
class ProductController {

   private static Logger logger = LoggerFactory.getLogger(ProductController.class)

    @Autowired
    ProductService productService

    @Autowired
    ProductAggregateService productAggregateService

    @Path('/products/{id}')
    @GET
    Response getProduct(@PathParam('id') String id) {

        ProductData productData = null
        try{
            productData = productAggregateService.getProductDetrails(id)
        }catch (ProductServiceException pse){
            return Response.serverError().entity(pse?.errorDetail).type(MediaType.APPLICATION_JSON_TYPE).build()
        }catch (RedSkyException rse){
            return Response.serverError().entity(rse?.errorDetail).type(MediaType.APPLICATION_JSON_TYPE).build()
        }catch (Exception e){
            return Response.serverError(new ErrorDetail(errorCode: Response.Status.INTERNAL_SERVER_ERROR,userMessage: 'Internal Server Error, please try again.')).build()
        }

        return Response.ok(productData).build()
    }


    @Path('/product')
    @PUT
    Response createProduct(ProductData productData) {
        logger.info('product name {}',productData.productName)
        log.info('product name {}',productData.productName)
        productService.populateProductData(productData)
        return Response.ok().build()
    }
}


