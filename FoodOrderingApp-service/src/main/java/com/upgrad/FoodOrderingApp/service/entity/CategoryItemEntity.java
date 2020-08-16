package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Locale;

@Entity
@Table(name = "category_item")
@NamedQueries(
        {
                @NamedQuery(name = "categoryItemByCategoryId", query = "select c from CategoryItemEntity c where c.category.uuid = :givenUuid")
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
    private CategoryEntity category;


    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public ItemEntity getItem() {return item;}

    public void setItem(ItemEntity item) {this.item = item;}

    public CategoryEntity getCategory() {return category;}

    public void setCategory(CategoryEntity category) {this.category = category;}
}
