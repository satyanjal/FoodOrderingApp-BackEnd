package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Locale;

@Entity
@Table(name = "category_item")
@NamedQueries(
        {
                //@NamedQuery(name = "answerByUuid", query = "select a from AnswerEntity a where a.uuid = :uuid")
        }
)


public class CategoryItemEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @NotNull
    private ItemEntity item;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull
    private Locale.Category category;


    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public ItemEntity getItem() {return item;}

    public void setItem(ItemEntity item) {this.item = item;}

    public Locale.Category getCategory() {return category;}

    public void setCategory(Locale.Category category) {this.category = category;}
}
