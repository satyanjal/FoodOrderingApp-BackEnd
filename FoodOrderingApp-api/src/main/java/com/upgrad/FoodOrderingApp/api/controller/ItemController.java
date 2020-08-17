/*

package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.DeleteAddressResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.api.model.ItemListResponse;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(method = RequestMethod.GET, path = "/item/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ItemListResponse> getFivePopularItems(@PathVariable("restaurant_id") final String restaurantUuid,
                                                                @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, RestaurantNotFoundException {

        final List<ItemEntity> itemEntities = itemService.getFivePopularItems(restaurantUuid, authorization);

        ItemListResponse itemListResponse = new ItemListResponse();
        for (ItemEntity itemEntity : itemEntities) {
            ItemList itemList = new ItemList();
            itemList.setId(UUID.fromString(itemEntity.getUuid()));
            itemList.setItemName(itemEntity.getItemName());
            if (itemEntity.getType().equals("1")) {
                itemList.setItemType(ItemList.ItemTypeEnum.VEG);
            } else {
                itemList.setItemType(ItemList.ItemTypeEnum.NON_VEG);
            }
            itemList.setPrice(itemEntity.getPrice());
            itemListResponse.add(itemList);
        }

        return new ResponseEntity<ItemListResponse>(itemListResponse, HttpStatus.OK);
    }
}
*/