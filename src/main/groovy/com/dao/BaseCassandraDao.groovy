package com.dao

import com.datastax.driver.core.BoundStatement
import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Session
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PreDestroy

@Component
abstract class BaseCassandraDao {

    @Autowired
    Session cassandraSession

    ResultSet execute(BoundStatement statement){

        ResultSet resultSet = null
        try{
            resultSet = cassandraSession.execute(statement)
        }catch (Exception e){

        }
        resultSet
    }

    @PreDestroy
    void shutdown(){
        cassandraSession?.close()
        cassandraSession?.cluster?.close()
    }
}
