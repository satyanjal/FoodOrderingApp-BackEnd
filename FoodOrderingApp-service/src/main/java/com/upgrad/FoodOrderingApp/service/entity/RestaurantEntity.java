package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "restaurant")
@NamedQueries(
        {
                //@NamedQuery(name = "answerByUuid", query = "select a from AnswerEntity a where a.uuid = :uuid")
        }
)

public class RestaurantEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true)
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "restaurant_name")
    @Size(max = 50)
    @NotNull
    private String restaurantName;

    @Column(name = "photo_url")
    @Size(max = 255)
    private String photoUrl;

    @Column(name = "customer_rating")
    @NotNull
    private Double customerRating;

    @Column(name = "average_price_for_two")
    @NotNull
    private Integer averagePriceForTwo;

    @Column(name = "number_of_customers_rated")
    @NotNull
    private Integer numberOfCustomersRated;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;


    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getUuid() {return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}

    public String getRestaurantName() {return restaurantName;}

    public void setRestaurantName(String restaurantName) {this.restaurantName = restaurantName;}

    public String getPhotoUrl() {return photoUrl;}

    public void setPhotoUrl(String photoUrl) {this.photoUrl = photoUrl;}

    public Double getCustomerRating() {return customerRating;}

    public void setCustomerRating(Double customerRating) {this.customerRating = customerRating;}

    public Integer getAveragePriceForTwo() {return averagePriceForTwo;}

    public void setAveragePriceForTwo(Integer averagePriceForTwo) {this.averagePriceForTwo = averagePriceForTwo;}

    public Integer getNumberOfCustomersRated() {return numberOfCustomersRated;}

    public void setNumberOfCustomersRated(Integer numberOfCustomersRated) {this.numberOfCustomersRated = numberOfCustomersRated;}

    public AddressEntity getAddress() {return address;}

    public void setAddress(AddressEntity address) {this.address = address;}
}
