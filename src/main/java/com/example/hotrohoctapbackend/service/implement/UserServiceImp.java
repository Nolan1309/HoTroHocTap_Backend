package com.example.hotrohoctapbackend.service.implement;

import com.example.hotrohoctapbackend.dao.RoleUserRepository;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.entity.Account;
//import com.example.hotrohoctapbackend.service.implement.UserService;
import com.example.hotrohoctapbackend.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

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
        User item = new User(user.getEmail(), user.getPassword(), getAuthorities(user));
        return item;
//        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(Account employee) {

        if (employee != null) {
            return Arrays.asList(new SimpleGrantedAuthority(employee.getRole().getRoleName()));
        }
        return Arrays.asList();
    }

    @Override
    public Account findByEmail(String email) {
        return userRepository.findByEmail((email));
    }
}

