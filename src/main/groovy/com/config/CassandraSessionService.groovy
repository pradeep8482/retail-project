package com.config

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session
import groovy.util.logging.Slf4j

import java.util.concurrent.ConcurrentHashMap

@Slf4j
class CassandraSessionService {

    Cluster cluster

    Map<String,Session> sessionMap = new ConcurrentHashMap()
    Session getSession(final String keyspace){
        Session session = sessionMap.get(keyspace)
        if(!session){
            session = cluster.connect(keyspace)
            sessionMap.put(keyspace,session)
        }else if(session.isClosed()){
            log.info("session for keyspace ${keyspace} is closed, creating a new session")
            session = cluster.connect(keyspace)
            sessionMap.put(keyspace,session)
        }
        return session
    }
}
