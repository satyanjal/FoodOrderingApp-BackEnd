package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
@Table(name = "customer_auth")
@NamedQueries(
        {
                //@NamedQuery(name = "answerByUuid", query = "select a from AnswerEntity a where a.uuid = :uuid")
        }
)


public class CustomerAuthEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true)
    @Size(max = 200)
    @NotNull
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @NotNull
    private CustomerEntity customer;

    @Column(name = "access_token")
    @Size(max = 500)
    private String accessToken;

    @Column(name = "login_at")
    @NotNull
    private Timestamp loginAt;

    @Column(name = "logout_at")
    @NotNull
    private Timestamp logoutAt;

    @Column(name = "expires_at")
    @NotNull
    private Timestamp expiresAt;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getUuid() {return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}

    public CustomerEntity getCustomer() {return customer;}

    public void setCustomer(CustomerEntity customer) {this.customer = customer;}

    public String getAccessToken() {return accessToken;}

    public void setAccessToken(String accessToken) {this.accessToken = accessToken;}

    public Timestamp getLoginAt() {return loginAt;}

    public void setLoginAt(Timestamp loginAt) {this.loginAt = loginAt;}

    public Timestamp getLogoutAt() {return logoutAt;}

    public void setLogoutAt(Timestamp logoutAt) {this.logoutAt = logoutAt;}

    public Timestamp getExpiresAt() {return expiresAt;}

    public void setExpiresAt(Timestamp expiresAt) {this.expiresAt = expiresAt;}
}
