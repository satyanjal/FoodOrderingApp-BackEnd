package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
public class AddressController {

    @Autowired
    private AddressService addressService;

    @RequestMapping(method = RequestMethod.POST, path = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(final SaveAddressRequest saveAddressRequest,
                                                           @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, SaveAddressException {

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setUuid(UUID.randomUUID().toString());
        addressEntity.setFlatBuilNumber(saveAddressRequest.getFlatBuildingName());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setPincode(saveAddressRequest.getPincode());
        addressEntity.setActive(1);

        final AddressEntity createdAddress = addressService.createAddress(addressEntity, saveAddressRequest.getStateUuid(), authorization);
        final SaveAddressResponse saveAddressResponse = new SaveAddressResponse().id(createdAddress.getId().toString()).status("ADDRESS SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/address/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllAddress(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

        final List<AddressEntity> allAddresses = addressService.getAllAddresses(authorization);
        AddressListResponse addressListResponse = new AddressListResponse();
        for (AddressEntity addressEntity: allAddresses){
            AddressList addressList = new AddressList();
            addressList.setCity(addressEntity.getCity());
            addressList.setFlatBuildingName(addressEntity.getFlatBuilNumber());
            addressList.setLocality(addressEntity.getLocality());
            addressList.setPincode(addressEntity.getPincode());
            addressList.setId(UUID.fromString(addressEntity.getStateByUUID()));

            AddressListState addressListState = new AddressListState();
            addressListState.setId(UUID.fromString(addressEntity.getState().getUuid()));
            addressListState.setStateName(addressEntity.getState().getStateName());
            addressList.setState(addressListState);
            addressListResponse.addAddressesItem(addressList);
        }
        return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/address/{address_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteAddress(@PathVariable("address_id") final String addressUuid,
                                                                 @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AddressNotFoundException {

        addressService.removeAddress(addressUuid, authorization);
        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse().id(UUID.fromString(addressUuid)).status("ADDRESS DELETED SUCCESSFULLY");
        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/states", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates()  {

        final List<StateEntity> allStates = addressService.getAllStates();

        StatesListResponse statesListResponse = new StatesListResponse();
        for (StateEntity stateEntity : allStates) {
            StatesList statesList = new StatesList();
            statesList.setId(UUID.fromString(stateEntity.getUuid()));
            statesList.setStateName(stateEntity.getStateName());
            statesListResponse.addStatesItem(statesList);
        }

        return new ResponseEntity<StatesListResponse>(statesListResponse, HttpStatus.OK);
    }

}
