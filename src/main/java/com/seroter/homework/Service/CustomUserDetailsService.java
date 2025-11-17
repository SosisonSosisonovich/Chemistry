package com.seroter.homework.Service;


import com.seroter.homework.Entity.Users;
import com.seroter.homework.Repository.UsersRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepo customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = customerRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Customer not found with this email: " + username);
        }
        return new CustomerUserDetails(user);
    }
}