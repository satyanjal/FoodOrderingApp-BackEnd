package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.UUID;
import java.time.ZonedDateTime;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    private boolean ValidEmail(String email) {
        String emailRegex = "^[A-Z0-9_.]+@[A-Z0-9_.]+\\.[A-Z0-9]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private boolean ValidContactNumber(String contactNumber) {
        String contactNUmberRegex = "\\d{10}";

        Pattern pat = Pattern.compile(contactNUmberRegex);
        if (contactNumber == null)
            return false;
        return pat.matcher(contactNumber).matches();
    }

    private boolean WeakPassword(String password) {

        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#@$%&*!^]).{8,}$";

        Pattern pat = Pattern.compile(passwordRegex);
        if (password == null)
            return false;
        return pat.matcher(password).matches();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity signup(CustomerEntity customerEntity) throws SignUpRestrictedException {

        CustomerEntity existingUser1 = customerDao.getCustomerByContactNumber(customerEntity.getContactNumber());

        if (existingUser1 != null) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
        }

        if(!ValidEmail(customerEntity.getEmail())){
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
        }

        if(!ValidContactNumber(customerEntity.getContactNumber())){
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
        }

        if(!WeakPassword(customerEntity.getPassword())){
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        }

        if (customerEntity == null || customerEntity.getFirstName() == null
                || customerEntity.getContactNumber() == null
                || customerEntity.getEmail() == null
                || customerEntity.getPassword() == null
                || customerEntity.getFirstName().isEmpty()
                || customerEntity.getEmail().isEmpty() || customerEntity.getPassword().isEmpty()
                || customerEntity.getContactNumber().isEmpty()
        ) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        }

        String[] encryptedText = passwordCryptographyProvider.encrypt(customerEntity.getPassword());
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);
        return customerDao.createCustomer(customerEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity authenticate(final String contactNumber, final String password)
            throws AuthenticationFailedException {
        CustomerEntity customerEntity = customerDao.getCustomerByContactNumber(contactNumber);
        if (customerEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }

        final String encryptedPassword = PasswordCryptographyProvider.encrypt(password, customerEntity.getSalt());

        if (encryptedPassword.equals(customerEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
            customerAuthEntity.setCustomer(customerEntity);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expires = now.plusHours(8);

            customerAuthEntity.setAccessToken(jwtTokenProvider.generateToken
                    (customerEntity.getUuid(), now, expires));

            customerAuthEntity.setUuid(UUID.randomUUID().toString());
            customerAuthEntity.setLoginAt(now);
            customerAuthEntity.setExpiresAt(expires);
            customerAuthEntity.setLogoutAt(null);

            customerDao.createAuthToken(customerAuthEntity);

            customerDao.updateCustomer(customerEntity);
            return customerAuthEntity;
        } else {
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity validateBearerAuthentication(final String accessToken) throws AuthorizationFailedException {

        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerByToken(accessToken);
        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        } else if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }
        if(ZonedDateTime.now().compareTo(customerAuthEntity.getExpiresAt()) >= 0){
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        return customerAuthEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity logout(final String accessToken) throws AuthorizationFailedException {

        CustomerAuthEntity customerAuthEntity = validateBearerAuthentication(accessToken);
        customerAuthEntity.setExpiresAt(ZonedDateTime.now());
        customerAuthEntity.setLogoutAt(ZonedDateTime.now());
        customerDao.updateCustomerAuth(customerAuthEntity);
        return customerAuthEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity getCustomer(final String accessToken)
            throws AuthorizationFailedException {

        CustomerAuthEntity customerAuthEntity = validateBearerAuthentication(accessToken);
        CustomerEntity customer = customerAuthEntity.getCustomer();
        return customer;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(CustomerEntity customer) {
        customerDao.updateCustomer(customer);
        return customer;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomerPassword(final String oldPassword, final String newPassword, CustomerEntity customer)
            throws UpdateCustomerException {

        if(!WeakPassword(newPassword)){
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }

        final String encryptedOldPassword = PasswordCryptographyProvider
                .encrypt(oldPassword, customer.getSalt());

        if(!encryptedOldPassword.equals(customer.getPassword())) {
            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
        }
        final String encryptedNewPassword = PasswordCryptographyProvider
                .encrypt(newPassword, customer.getSalt());

        customer.setPassword(encryptedNewPassword);
        customerDao.updateCustomer(customer);

        return customer;
    }
}
