package com.elearning.apigatway.filter;

import brave.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseFilter {
    final Logger logger  = LoggerFactory.getLogger(ResponseFilter.class);


    @Autowired
    Tracer tracer;

    @Autowired
    FilterUtils filterUtils;

    @Bean
    public GlobalFilter postGlobalFilter(){
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(
                    ()->{
//                        String traceId = tracer.currentSpan()
//                                        .context()
//                                        .traceIdString();

                        HttpHeaders requestHeader = exchange.getRequest().getHeaders();
                        String traceId = filterUtils.getCorrelationId(requestHeader);
                        logger.debug("Adding the correlation id to the outbound headers, {}",traceId);
                        exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, traceId);
                        logger.debug("completing the outgoing request for {}.", exchange.getRequest().getURI());

                    }
            ));
        };
    }
}
