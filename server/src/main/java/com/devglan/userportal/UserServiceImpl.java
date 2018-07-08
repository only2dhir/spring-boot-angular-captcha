package com.devglan.userportal;

import com.devglan.userportal.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private CaptchaService captchaService;

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public User delete(int id) {
        User user = findById(id);
        if(user != null){
            repository.delete(user);
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(int id) {
        return repository.findOne(id);
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        boolean captchaVerified = captchaService.verify(loginRequest.getRecaptchaResponse());
        if(!captchaVerified) {
            loginResponse.setMessage("Invalid captcha");
            loginResponse.setStatus(400);
        }
        if(captchaVerified && loginRequest.getEmail().equals("dhiraj@devglan.com") && loginRequest.getPassword().equals("password")) {
            loginResponse.setMessage("Success");
            loginResponse.setStatus(200);
            loginResponse.setUsername("dhiraj");
            loginResponse.setToken("token");
        }else {
            loginResponse.setMessage("Invalid credentials.");
            loginResponse.setStatus(400);
        }
        return loginResponse;
    }
}
