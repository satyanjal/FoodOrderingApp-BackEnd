package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "restaurant_category")
@NamedQueries(
        {
                @NamedQuery(name = "categoryByRestaurantId", query = "select a from RestaurantCategoryEntity a where a.restaurant = :restaurant"),
                @NamedQuery(name = "restaurantByCategoryId", query = "select a from RestaurantCategoryEntity a where a.category = :category"),
        }
)

public class RestaurantCategoryEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @NotNull
    private RestaurantEntity restaurant;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull
    private CategoryEntity category;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public RestaurantEntity getRestaurant() {return restaurant;}

    public void setRestaurant(RestaurantEntity restaurant) {this.restaurant = restaurant;}

    public CategoryEntity getCategory() {return category;}

    public void setCategory(CategoryEntity category) {this.category = category;}
}
