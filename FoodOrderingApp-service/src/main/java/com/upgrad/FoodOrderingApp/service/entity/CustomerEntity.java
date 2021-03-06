package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "customer", schema = "public")
@NamedQueries(
        {
                //@NamedQuery(name = "answerByUuid", query = "select a from AnswerEntity a where a.uuid = :uuid")
                @NamedQuery(name = "customerById", query = "select c from CustomerEntity c where c.id = :id"),
                @NamedQuery(name = "customerByUUID", query = "select c from CustomerEntity c where c.uuid = :uuid"),
                @NamedQuery(name = "customerByEmail", query = "select c from CustomerEntity c where c.email = :email"),
                @NamedQuery(name = "customerByContactNumber", query = "select c from CustomerEntity c where c.contactNumber = :contact_number")
        }
)

public class CustomerEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true)
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "firstname")
    @Size(max = 30)
    @NotNull
    private String firstName;

    @Column(name = "lastname")
    @Size(max = 30)
    private String lastName;

    @Column(name = "email")
    @Size(max = 50)
    private String email;

    @Column(name = "contact_number", unique = true)
    @Size(max = 30)
    @NotNull
    private String contactNumber;

    @Column(name = "password")
    @Size(max = 255)
    @NotNull
    private String password;

    @Column(name = "salt")
    @NotNull
    @Size(max = 200)
    @ToStringExclude
    private String salt;

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getUuid() {return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getContactNumber() {return contactNumber;}

    public void setContactNumber(String contactNumber) {this.contactNumber = contactNumber;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getSalt() {return salt;}

    public void setSalt(String salt) {this.salt = salt;}

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}


