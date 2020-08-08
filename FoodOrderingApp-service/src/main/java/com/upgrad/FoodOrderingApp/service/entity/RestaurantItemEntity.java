package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "restaurant_item")
@NamedQueries(
        {
                //@NamedQuery(name = "answerByUuid", query = "select a from AnswerEntity a where a.uuid = :uuid")
        }
)


public class RestaurantItemEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @NotNull
    private RestaurantEntity restaurant;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @NotNull
    private ItemEntity item;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public RestaurantEntity getRestaurant() {return restaurant;}

    public void setRestaurant(RestaurantEntity restaurant) {this.restaurant = restaurant;}

    public ItemEntity getItem() {return item;}

    public void setItem(ItemEntity item) {this.item = item;}
}
