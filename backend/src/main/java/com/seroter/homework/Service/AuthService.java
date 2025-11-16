package com.seroter.homework.Service;

import com.seroter.homework.Entity.LoginRequest;
import com.seroter.homework.Entity.Users;
import com.seroter.homework.Repository.RolesRepo;
import com.seroter.homework.Repository.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsersRepo userRepository;
    private final RolesRepo roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String authenticate(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "Logged in successfully!";
        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Invalid username or password");
        } catch (Exception ex) {
            throw new RuntimeException("Login failed: " + ex.getMessage());
        }
    }
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    public boolean hasRole(HttpSession session, String roleName) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) return false;

        return user.getRole().getRole_name().equalsIgnoreCase(roleName);
    }

    public Users getCurrentUser(HttpSession session) {
        return (Users) session.getAttribute("user");
    }
}
