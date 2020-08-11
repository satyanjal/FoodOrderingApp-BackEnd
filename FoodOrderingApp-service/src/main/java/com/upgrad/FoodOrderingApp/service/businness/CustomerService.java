package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
}
