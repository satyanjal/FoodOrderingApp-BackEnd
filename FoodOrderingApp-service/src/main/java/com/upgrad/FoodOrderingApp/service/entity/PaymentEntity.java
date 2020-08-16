package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "payment", schema = "public")
@NamedQueries(
        {
                //@NamedQuery(name = "answerByUuid", query = "select a from AnswerEntity a where a.uuid = :uuid")
                @NamedQuery(name = "allPaymentMethods", query = "select p from PaymentEntity p"),
                @NamedQuery(name = "getMethodbyUUID", query = "select p from PaymentEntity p where p.uuid = :paymentUUID")
        }
)

public class PaymentEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true)
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "payment_name")
    @Size(max = 255)
    private String paymentName;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getUuid() {return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}

    public String getPaymentName() {return paymentName;}

    public void setPaymentName(String paymentName) {this.paymentName = paymentName;}
}
