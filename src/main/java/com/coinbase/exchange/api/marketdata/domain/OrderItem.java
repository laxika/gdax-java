package com.coinbase.exchange.api.marketdata.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@Getter
@ToString
public class OrderItem implements Comparable {

    private final String orderId;
    private final BigDecimal price;
    private final BigDecimal size;
    private final BigDecimal num;

    @JsonCreator
    public OrderItem(List<String> limitOrders) {
        if (CollectionUtils.isEmpty(limitOrders) || limitOrders.size() < 3) {
            throw new IllegalArgumentException("LimitOrders was empty - check connection to the exchange");
        }

        price = new BigDecimal(limitOrders.get(0));
        size = new BigDecimal(limitOrders.get(1));

        if (isString(limitOrders.get(2))) {
            orderId = limitOrders.get(2);
            num = new BigDecimal(1);
        } else {
            orderId = null;
            num = new BigDecimal(1);
        }
    }

    @Override
    public int compareTo(final Object o) {
        return this.getPrice().compareTo(((OrderItem) o).getPrice()) * -1;
    }

    //What is this??? The method name means next to nothing.
    private boolean isString(final String value) {
        boolean isDecimalSeparatorFound = false;

        for (char c : value.substring(1).toCharArray()) {
            if (!Character.isDigit(c)) {
                if (c == '.' && !isDecimalSeparatorFound) {
                    isDecimalSeparatorFound = true;
                    continue;
                }

                return false;
            }
        }

        return true;
    }
}
