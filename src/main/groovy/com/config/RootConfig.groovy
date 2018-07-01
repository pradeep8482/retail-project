package com.config

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.ConsistencyLevel
import com.datastax.driver.core.PoolingOptions
import com.datastax.driver.core.ProtocolOptions
import com.datastax.driver.core.QueryOptions
import com.datastax.driver.core.Session
import com.datastax.driver.core.SocketOptions
import com.datastax.driver.core.policies.RoundRobinPolicy
import groovy.util.logging.Slf4j
import org.apache.http.impl.NoConnectionReuseStrategy
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.glassfish.jersey.servlet.ServletContainer
import org.glassfish.jersey.servlet.ServletProperties
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Configuration
@ComponentScan(basePackages = ['com'])
@Slf4j
class RootConfig {

    @Bean
    RestTemplate redskyClient(){

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE)
                .setMaxConnPerRoute(5)
                .setMaxConnTotal(5)
                .disableAutomaticRetries()
                .useSystemProperties()
                .setDefaultCookieStore(new BasicCookieStore())
                .setSSLContext(null)
                .build()

        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient: httpClient,connectTimeout: 10000,readTimeout: 5000)
        RestTemplate restTemplate = new RestTemplate(requestFactory: httpComponentsClientHttpRequestFactory)

        restTemplate
    }
    @Bean
    CassandraSessionService cassandraSessionService() {
        new CassandraSessionService(cluster: buildCluster())
    }

    Session session

    @Bean
    Session cassandraSession(){
        try{
            session = cassandraSessionService().getSession('retail_product')
        }catch (Exception e){
            log.info('Exception while getting cassandra session ',e.getMessage())
        }
        session
    }
    Cluster buildCluster() {
        String[] contactPoints = ['127.0.0.1']
        Cluster.Builder builder = Cluster.builder()
                .addContactPoints(contactPoints)
                .withPort(ProtocolOptions.DEFAULT_PORT)
                .withLoadBalancingPolicy(new RoundRobinPolicy())
                .withPoolingOptions(new PoolingOptions())
                .withSocketOptions(buildSocketOptions())
                .withQueryOptions(new QueryOptions(consistencyLevel: ConsistencyLevel.LOCAL_ONE))
        builder.build()

    }

    SocketOptions buildSocketOptions() {
        final SocketOptions socketOptions = new SocketOptions()
        socketOptions.setConnectTimeoutMillis(100)
        socketOptions.setReadTimeoutMillis(5000)
        socketOptions.setKeepAlive(true)

        socketOptions
    }
}
