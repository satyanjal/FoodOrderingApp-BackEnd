package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "customer_address")
@NamedQueries(
        {
                //@NamedQuery(name = "answerByUuid", query = "select a from AnswerEntity a where a.uuid = :uuid")
        }
)

public class CustomerAddressEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    
    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public CustomerEntity getCustomer() {return customer;}

    public void setCustomer(CustomerEntity customer) {this.customer = customer;}

    public AddressEntity getAddress() {return address;}

    public void setAddress(AddressEntity address) {this.address = address;}
}
