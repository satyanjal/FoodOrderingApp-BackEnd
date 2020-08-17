package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AddressEntity createAddress(AddressEntity addressEntity) {
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    public List<AddressEntity> getAllAddresses() {
        TypedQuery<AddressEntity> query = entityManager.createQuery("SELECT q from AddressEntity q", AddressEntity.class);
        return query.getResultList();
    }

    public AddressEntity getAddressByUuid(final String addressUuid) {
        try {
            return entityManager.createNamedQuery("addressByUuid", AddressEntity.class).
                    setParameter("uuid", addressUuid).getSingleResult();
        } catch (NoResultException nre) {return null;}
    }

    public AddressEntity deleteAddress(AddressEntity addressEntity) {
        entityManager.remove(addressEntity);
        return addressEntity;
    }
}
