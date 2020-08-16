package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "orders")
@NamedQueries(
        {
                @NamedQuery(name = "ordersByCustomerId", query = "select  o from OrdersEntity o where o.customer.uuid = :customerUuidNq order by o.date desc"),
                @NamedQuery(name = "ordersByRestaurantId", query = "select o from OrdersEntity o where o.restaurant_id = :restaurant_id")

        }
)

public class OrdersEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true)
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "bill")
    @NotNull
    private Double bill;

    @Column(name = "discount")
    @NotNull
    private Double discount;

    @Column(name = "date")
    @NotNull
    private Date date;

    @ManyToOne
    @JoinColumn(name = "address_id")
    @NotNull
    private AddressEntity address;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @NotNull
    private RestaurantEntity restaurant;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @NotNull
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private CouponEntity coupon;


    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getUuid() {return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}

    public Double getBill() {return bill;}

    public void setBill(Double bill) {this.bill = bill;}

    public Double getDiscount() {return discount;}

    public void setDiscount(Double discount) {this.discount = discount;}

    public Date getDate() {return date;}

    public void setDate(Date date) {this.date = date;}

    public AddressEntity getAddress() {return address;}

    public void setAddress(AddressEntity address) {this.address = address;}

    public RestaurantEntity getRestaurant() {return restaurant;}

    public void setRestaurant(RestaurantEntity restaurant) {this.restaurant = restaurant;}

    public PaymentEntity getPayment() {return payment;}

    public void setPayment(PaymentEntity payment) {this.payment = payment;}

    public CustomerEntity getCustomer() {return customer;}

    public void setCustomer(CustomerEntity customer) {this.customer = customer;}

    public CouponEntity getCoupon() {return coupon;}

    public void setCoupon(CouponEntity coupon) {this.coupon = coupon;}
}
