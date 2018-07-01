package com.dao

import com.datastax.driver.core.BoundStatement
import com.datastax.driver.core.PreparedStatement
import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Row
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.domain.ProductData
import com.domain.ProductPrice
import com.exception.CassandraDaoException
import com.exception.ErrorDetail
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

import static com.datastax.driver.core.querybuilder.QueryBuilder.bindMarker
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq

@Component
class ProductDao extends BaseCassandraDao {

    static private PreparedStatement selectQuery
static private PreparedStatement insertQuery
    @Autowired
    ObjectMapper objectMapper

    @PostConstruct
    void postConfig(){

        selectQuery = cassandraSession.prepare(QueryBuilder
                .select()
                .from('retail_product','product_by_id_name')
                .where(eq('product_id',bindMarker()))
                .and(eq('product_name',bindMarker()))

        )

        insertQuery = cassandraSession.prepare(QueryBuilder.insertInto("product_by_id_name")
                .value('product_id', bindMarker())
                .value('product_name', bindMarker())
                .value('data', bindMarker())
                .value('date', bindMarker()))
    }

    void insertProductData(ProductData productData) {
        /**PreparedStatement insertQuery = cassandraSession.prepare(QueryBuilder.insertInto("product_by_id_name")
                .value('product_id', bindMarker())
                .value('product_name', bindMarker())
                .value('data', bindMarker())
                .value('date', bindMarker()))**/

        BoundStatement boundStatement = new BoundStatement(insertQuery)
        boundStatement.bind(productData.productId,
                productData.productName,
                objectMapper.writeValueAsString(productData.productPrice),
                new Date())

        execute(boundStatement)

    }

    List<ProductData> execteForSelectData(BoundStatement boundStatement){

       ResultSet resultSet = execute(boundStatement)
        resultSet?.all()?.collect { Row row ->
            ProductData productData = new ProductData()
            productData.productId = row.getString('product_id')
            productData.productName = row.getString('product_name')
            productData.productPrice = objectMapper.readValue(row.getString('data'),ProductPrice)
            productData
        }
    }

    /**
     * This method query cassandra DB for given product id and product name(we get from red sky service)
     * @param ProductData productData
     * @return ProductData
     */
    ProductData selectProductData(ProductData productData){
        List<ProductData> productDataList = null
        try{
            BoundStatement boundStatement = new BoundStatement(selectQuery)
            boundStatement.bind(productData.productId,
                    productData.productName)
            productDataList= execteForSelectData(boundStatement)
        }catch(Exception e){
            throw new CassandraDaoException(new ErrorDetail(errorCode: 'DB_ERROR',userMessage: 'Unable to get details from DB'))
        }

         if(productDataList?.isEmpty()){
             throw new CassandraDaoException(new ErrorDetail(errorCode: 'NO_RESULTS',userMessage: "No data for ${productData.productId} and ${productData.productName}"))
         }
         productDataList?.get(0)
    }
}
