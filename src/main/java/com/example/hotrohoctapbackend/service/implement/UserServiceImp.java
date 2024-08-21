package com.example.hotrohoctapbackend.service.implement;

import com.example.hotrohoctapbackend.dao.RoleUserRepository;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.entity.Account;
//import com.example.hotrohoctapbackend.service.implement.UserService;
import com.example.hotrohoctapbackend.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {


    private AccountRepository userRepository;

    private RoleUserRepository roleRepository;
    @Autowired
    public UserServiceImp(AccountRepository userRepository, RoleUserRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = userRepository.findByEmail((username));
        if (user == null) {
            throw new UsernameNotFoundException("Tài khoản không tồn tại!");
        }
        return user;
    }

    @Override
    public Account findByEmail(String email) {
        return userRepository.findByEmail((email));
    }
}

