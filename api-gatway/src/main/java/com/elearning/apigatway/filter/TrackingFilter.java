package com.elearning.apigatway.filter;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Order(1)
@Component
public class TrackingFilter implements GlobalFilter {
    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

    @Autowired
    FilterUtils filterUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if(isCorrelationIdPresent(requestHeaders)){
            logger.debug("tmx-correlation-id found in tracking filter: {}. ",filterUtils.getCorrelationId(requestHeaders));
        }else{
            String correlationID = generateCorrelationId();
            exchange = filterUtils.setCorrelationId(exchange, correlationID);
            logger.debug("new tmx-correlation-id generated in tracking filters: {}.",correlationID);
        }

        System.out.println("The authentication name from the token is : " + getUsername(requestHeaders));

        return chain.filter(exchange);
    }

    private boolean isCorrelationIdPresent(HttpHeaders requestHeader){
        if(filterUtils.getCorrelationId(requestHeader) != null) return true;
        else return false;
    }

    private String generateCorrelationId(){
        return java.util.UUID.randomUUID().toString();
    }


    private String getUsername(HttpHeaders requestHeaders){
        String username="";
        if(filterUtils.getAuthToken(requestHeaders) != null){
            String authToken = filterUtils.getAuthToken(requestHeaders).replace("Bearer ","");
            JSONObject jsonObject = decodeJWT(authToken);
            try {
                username = jsonObject.getString("preferred_username");
            }catch (Exception e){
                logger.debug(e.getMessage());
            }
        }
        return username;
    }

    private JSONObject decodeJWT(String JWTToken) {
        String[] split_string = JWTToken.split("\\.");
        String base64EncodedBody = split_string[1];
        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));
        JSONObject jsonObj = new JSONObject(body);
        return jsonObj;
    }

}
