package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders") //정렬할때 order 키워드가 있기때문에 orders라고 지정
@Getter @Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //한명의 회원은 여러번의 주문을 할수있으므로 주문엔티티 기준에서 다대일 단방향 매핑을함.

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문 상태

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>(); //하나의 주문이 여러개의 주문상품을 갖으므로 List 자료형을 사용해서 매핑함

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    private void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member,List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member);
        for (OrderItem orderItem : orderItemList){
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    public int getTotalPrice(){
        int totalPrice =0;
        for (OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;

        for (OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

}
