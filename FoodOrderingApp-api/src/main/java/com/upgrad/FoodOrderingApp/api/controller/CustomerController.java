package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * This method is for user signup. This method receives the object of SignupUserRequest type with
     * its attributes being set.
     *
     * @param signupCustomerRequest - contains customer information for signing up
     * @return customerResponse
     * @throws SignUpRestrictedException - handles invalid entries
     */
    @RequestMapping(method = RequestMethod.POST, path = "/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> customerSignup(@RequestBody final SignupCustomerRequest signupCustomerRequest)
            throws SignUpRestrictedException {

        final CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setLastName(signupCustomerRequest.getLastName());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setPassword(signupCustomerRequest.getPassword());
        customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        customerEntity.setSalt("1234abc");

        final CustomerEntity createdCustomerEntity = customerService.saveCustomer(customerEntity);
        SignupCustomerResponse customerResponse = new SignupCustomerResponse();
        customerResponse.id(createdCustomerEntity.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<>(customerResponse, HttpStatus.CREATED);
    }

    /**
     * This method is for a user to login.
     *
     * @param authorization - Base64 encoded string for contact number and password combination
     * @return loginResponse
     * @throws AuthenticationFailedException - if the contact number exist in the database.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/customer/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader("authorization") final String authorization)
            throws AuthenticationFailedException {

//      System.out.println(authorization);
        String encodedText = authorization.split("Basic ")[1];

        byte[] decode = Base64.getDecoder().decode(encodedText);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");
        String contactNumber, password;
        try{
            contactNumber = decodedArray[0];
            password = decodedArray[1];
        }catch(Exception e){
            throw new AuthenticationFailedException("ATH-003",
                    "Incorrect format of decoded customer name and password");
        }
        CustomerAuthEntity customerAuthEntity = customerService.authenticate(contactNumber, password);
        CustomerEntity customer = customerAuthEntity.getCustomer();
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.id(customer.getUuid()).firstName(customer.getFirstName()).lastName(customer.getLastName())
                .contactNumber(customer.getContactNumber()).emailAddress(customer.getEmail()).message("LOGGED IN SUCCESSFULLY");

        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", customerAuthEntity.getAccessToken());
        return new ResponseEntity<>(loginResponse, headers, HttpStatus.OK);
    }

    /**
     * This method is used to logout user.
     *
     * @param accessToken -Token used for authenticating the user.
     * @return logoutResponse
     * @throws AuthorizationFailedException - if the user is not signed in the application and tries to logout of the application
     */
    @RequestMapping(method = RequestMethod.POST, path = "/customer/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("access-token") final String accessToken)
            throws AuthorizationFailedException {

        String splittedAccessToken = accessToken.split("Bearer ")[1];
        CustomerAuthEntity customerAuthEntity;
        customerAuthEntity = customerService.logout(splittedAccessToken);
        LogoutResponse logoutResponse = new LogoutResponse();
        logoutResponse.id(customerAuthEntity.getUuid()).message("LOGGED OUT SUCCESSFULLY");
        return new ResponseEntity<>(logoutResponse, HttpStatus.OK);
    }

    /**
     * This method is used to update user information.
     *
     * @param  bearerAccessToken - used for authenticating the user
     * @param  updateCustomerRequest - dictionary containing updated information
     * @return updateCustomerResponse
     * @throws UpdateCustomerException - if the first name of user is empty
     * @throws AuthorizationFailedException if system is unable to authenticate the user
     *
     */
    @RequestMapping(method = RequestMethod.PUT, path="/customer",produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<UpdateCustomerResponse> update(@RequestHeader("authorization") final String bearerAccessToken, @RequestBody final UpdateCustomerRequest updateCustomerRequest)
            throws AuthorizationFailedException, UpdateCustomerException {

        if (updateCustomerRequest.getFirstName() == null || updateCustomerRequest.getFirstName().trim()
                .isEmpty()) {
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }

        String accessToken = bearerAccessToken.split("Bearer ")[1];
        customerService.validateAccessToken(accessToken); //For handling authentication related errors
        CustomerEntity customer = customerService.getCustomer(accessToken);
        customer.setFirstName(updateCustomerRequest.getFirstName());
        customer.setLastName(updateCustomerRequest.getLastName());
        customerService.updateCustomer(customer);

        UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse();
        updateCustomerResponse.id(customer.getUuid()).firstName(customer.getFirstName())
                .lastName(customer.getLastName()).status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");

        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", accessToken);
        return new ResponseEntity<>(updateCustomerResponse, headers, HttpStatus.OK);
    }

    /**
     * This method is used to update user password.
     *
     * @param  accessToken - Token used for authenticating the user
     * @param  updatePasswordRequest - dictionary containing new and old password
     * @return response
     * @throws UpdateCustomerException - if the first name of user is empty
     * @throws AuthorizationFailedException - system is unable to authenticate user
     *
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/customer/password", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> updateCustomerPassword
            (@RequestHeader("authorization") final String accessToken, @RequestBody UpdatePasswordRequest updatePasswordRequest)
            throws UpdateCustomerException, AuthorizationFailedException {

        if (updatePasswordRequest.getOldPassword() == null || updatePasswordRequest.getOldPassword().isEmpty()
                || updatePasswordRequest.getNewPassword() == null || updatePasswordRequest.getNewPassword().isEmpty()) {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }

        String splittedAccessToken = accessToken.split("Bearer ")[1];
        CustomerEntity customer = customerService.getCustomer(splittedAccessToken);
        String newPassword = updatePasswordRequest.getNewPassword();
        String oldPassword = updatePasswordRequest.getOldPassword();
        CustomerEntity updatedCustomer = customerService.updateCustomerPassword(oldPassword, newPassword, customer);
        UpdatePasswordResponse response = new UpdatePasswordResponse();
        response.id(updatedCustomer.getUuid()).status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
