package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private StateDao stateDao;

    @Autowired
    private CustomerAuthDao customerAuthDao;

    @Autowired
    private CustomerService customerService;

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity createAddress(final AddressEntity addressEntity, final String stateUuid, final String authorizationToken) throws AuthorizationFailedException, SaveAddressException {
        CustomerEntity customerEntity = customerService.getCustomer(authorizationToken);
        if (addressEntity.getFlatBuilNumber() == null || addressEntity.getLocality() == null ||
                addressEntity.getCity() == null || addressEntity.getPincode() == null || stateUuid == null) {
            throw new SaveAddressException("SAR-001", "No field can be empty");
        }

        boolean numeric = true;
        try {
            Long num = Long.parseLong(addressEntity.getPincode());
        } catch (NumberFormatException e) {
            numeric = false;
        }

        if (addressEntity.getPincode().length() != 6 || !numeric) {
            throw new SaveAddressException("SAR-002", "Invalid pincode");
        }

        StateEntity stateEntity = getStateByUUID(stateUuid);
        if (stateEntity == null) {
            throw new SaveAddressException("ANF-002", "No state by this id");
        } else {
            addressEntity.setState(stateEntity);
        }

        return addressDao.createAddress(addressEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AddressEntity> getAllAddresses(final String authorizationToken) throws AuthorizationFailedException {

        CustomerEntity customerEntity = customerService.getCustomer(authorizationToken);
        return addressDao.getAllAddresses();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeAddress(String addressUuid, final String authorizationToken) throws AuthorizationFailedException, AddressNotFoundException {
        CustomerEntity customerEntity = customerService.getCustomer(authorizationToken);

        if (addressUuid == null) {
            throw new AddressNotFoundException("ANF-005", "Address id can not be empty");
        }

        AddressEntity addressEntity = getAddressByUUID(addressUuid);
        if (addressEntity == null) {
            throw new AddressNotFoundException("ANF-003", "No address by this id");
        }
        // Throw exception for OWNer of address
        deleteAddress(addressEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<StateEntity> getAllStates() {
        return stateDao.getAllStates();
    }

    public StateEntity getStateByUUID(String stateUuid) {
        return stateDao.getStateByUuid(stateUuid);
    }

    public AddressEntity getAddressByUUID(String addressUuid) {
        return addressDao.getAddressByUuid(addressUuid);
    }

    public AddressEntity deleteAddress(AddressEntity addressEntity) {
        return addressDao.deleteAddress(addressEntity);
    }
}
