package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "address")
@NamedQueries(
        {
                //@NamedQuery(name = "answerByUuid", query = "select a from AnswerEntity a where a.uuid = :uuid")
        }
)

public class AddressEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true)
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "flat_buil_number")
    @Size(max = 255)
    private String flatBuilNumber;

    @Column(name = "locality")
    @Size(max = 255)
    private String locality;

    @Column(name = "city")
    @Size(max = 30)
    private String city;

    @Column(name = "pincode")
    @Size(max = 30)
    private String pincode;

    @Column(name = "state_id")
    @NotNull
    private Integer stateId;

    @Column(name = "active")
    @NotNull
    private Integer active;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private StateEntity state;


    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getUuid() {return uuid;}

    public void setUuid(String uuid) {this.uuid = uuid;}

    public String getFlatBuilNumber() {return flatBuilNumber;}

    public void setFlatBuilNumber(String flatBuilNumber) {this.flatBuilNumber = flatBuilNumber;}

    public String getLocality() {return locality;}

    public void setLocality(String locality) {this.locality = locality;}

    public String getCity() {return city;}

    public void setCity(String city) {this.city = city;}

    public String getPincode() {return pincode;}

    public void setPincode(String pincode) {this.pincode = pincode;}

    public Integer getStateId() {return stateId;}

    public void setStateId(Integer stateId) {this.stateId = stateId;}

    public Integer getActive() {return active;}

    public void setActive(Integer active) {this.active = active;}

    public StateEntity getState() {return state;}

    public void setState(StateEntity state) {this.state = state;}
}
