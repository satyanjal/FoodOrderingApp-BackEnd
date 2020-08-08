package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "order_item")
@NamedQueries(
        {
                //@NamedQuery(name = "answerByUuid", query = "select a from AnswerEntity a where a.uuid = :uuid")
        }
)


public class OrderItemEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @NotNull
    private OrdersEntity order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @NotNull
    private ItemEntity item;

    @Column(name = "quantity")
    @NotNull
    private Integer quantity;

    @Column(name = "price")
    @NotNull
    private Integer price;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public OrdersEntity getOrder() {return order;}

    public void setOrder(OrdersEntity order) {this.order = order;}

    public ItemEntity getItem() {return item;}

    public void setItem(ItemEntity item) {this.item = item;}

    public Integer getQuantity() {return quantity;}

    public void setQuantity(Integer quantity) {this.quantity = quantity;}

    public Integer getPrice() {return price;}

    public void setPrice(Integer price) {this.price = price;}
}
