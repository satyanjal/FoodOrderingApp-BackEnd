package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "coupon")
@NamedQueries(
        {
                @NamedQuery(name = "getCouponByName", query = "select c from CouponEntity c where c.couponName = :couponNameNq"),
                @NamedQuery(name = "getCouponById", query = "select c from CouponEntity c where c.uuid= :couponIdNq")

        }
)


public class CouponEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true)
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "coupon_name")
    @Size(max = 255)
    @NotNull
    private String couponName;

    @Column(name = "percent")
    @NotNull
    private Integer percent;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getUuid() {return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}

    public String getCouponName() {return couponName;}

    public void setCouponName(String couponName) {this.couponName = couponName;}

    public Integer getPercent() {return percent;}

    public void setPercent(Integer percent) {this.percent = percent;}
}
