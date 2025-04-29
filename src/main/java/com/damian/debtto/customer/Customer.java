package com.damian.debtto.customer;

import com.damian.debtto.common.utils.DTOMapper;
import com.damian.debtto.customer.profile.Profile;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "customers")
public class Customer implements CustomerDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String passwordHash;

    @Column
    @Enumerated(EnumType.STRING)
    private CustomerAccountStatus accountStatus;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL) // FetchType EAGER por defecto
    private Profile profile;

    @Enumerated(EnumType.STRING)
    private CustomerRole role;

    @Column
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    public Customer() {
        this.profile = new Profile(this);
        this.role = CustomerRole.CUSTOMER;
        this.accountStatus = CustomerAccountStatus.ENABLED;
    }

    public Customer(Long id, String email, String password) {
        this();
        this.id = id;
        this.email = email;
        this.setPassword(password);
    }

    public Customer(String email, String password) {
        this(null, email, password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.passwordHash = password;
    }

    public CustomerRole getRole() {
        return role;
    }

    public void setRole(CustomerRole role) {
        this.role = role;
    }

    public CustomerDTO toDTO() {
        return DTOMapper.build(this);
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return this.passwordHash;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return CustomerDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return CustomerDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return CustomerDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return CustomerDetails.super.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + this.role.name());
        return Collections.singletonList(authority);
    }

    public Profile getProfile() {
        return profile;
    }

    public String getFullName() {
        return getProfile().getName() + " " + getProfile().getSurname();
    }

    public void setProfile(Profile profile) {
        if (profile.getCustomer() != this) {
            profile.setCustomer(this);
        }

        this.profile = profile;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public CustomerAccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(CustomerAccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
}
