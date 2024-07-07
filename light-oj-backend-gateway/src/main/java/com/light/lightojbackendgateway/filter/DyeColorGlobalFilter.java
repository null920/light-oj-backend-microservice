package com.light.lightojbackendgateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 流量染色过滤器
 *
 * @author null&&
 * @Date 2024/7/7 19:52
 */
//@Component
public class DyeColorGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 创建新的请求对象，包含染色标识
        ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-Dye-Color", "OJ-Ture-Flag") // 这里可以替换成你喜欢的颜色或其他标识
                .build();

        // 将修改后的请求传递给过滤器链
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}