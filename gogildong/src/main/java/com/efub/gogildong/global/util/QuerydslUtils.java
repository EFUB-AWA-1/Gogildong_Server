package com.efub.gogildong.global.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Sort;

// Sort → OrderSpecifier 변환
public class QuerydslUtils {

    public static OrderSpecifier<?>[] toOrderSpecifiers(Sort sort, PathBuilder<?> entityPath) {
        return sort.stream()
                .map(order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    return new OrderSpecifier(direction, entityPath.get(order.getProperty()));
                })
                .toArray(OrderSpecifier[]::new);
    }
}
