package com.indpro.task_manager.wrapper;

import com.indpro.task_manager.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUserEntity() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(user.getAuthority());
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // ✅ Use the actual password from DB
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // ✅ Email is the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // ✅ assuming all accounts are non-expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // ✅ assuming all accounts are non-locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // ✅ assuming credentials are always valid
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled(); // ✅ use your `enabled` flag
    }
}