package com.joaoneto.todosimple.services;

import com.joaoneto.todosimple.models.User;
import com.joaoneto.todosimple.models.enums.ProfileEnum;
import com.joaoneto.todosimple.repositories.UserRepository;
import com.joaoneto.todosimple.security.UserSpringSecurity;
import com.joaoneto.todosimple.services.exceptions.AuthorizationException;
import com.joaoneto.todosimple.services.exceptions.DataBindingViolationException;
import com.joaoneto.todosimple.services.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    public User findById(Long id) {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (!Objects.nonNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId())) {
            throw new AuthorizationException("Access denied");
        }
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException("User not found: " + id));
    }

    @Transactional
    public User create(User user) {
        user.setId(null);
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        user.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        user = this.userRepository.save(user);
        return user;
    }

    @Transactional
    public User update(User user) {
        User newUser = this.findById(user.getId());
        newUser.setPassword(user.getPassword());
        newUser.setPassword(this.bCryptPasswordEncoder.encode(newUser.getPassword()));
        return this.userRepository.save(newUser);
    }


    public void delete(Long id) {
        this.findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Error deleting user: " + e.getMessage());
        }
    }

    public static UserSpringSecurity authenticated(){
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e){
            return null;
        }
    }
}
