package jpabook.jpashop.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderItemQueryDto {

    @JsonIgnore
    private Long oderId;
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemQueryDto(Long oderId, String itemName, int orderPrice, int count) {
        this.oderId = oderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
