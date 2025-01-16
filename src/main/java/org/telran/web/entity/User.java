package org.telran.web.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.telran.web.enums.Role;
import org.telran.web.validation.ValidPassword;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shop_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    private String username;

    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 4, max = 128, message = "Password must be between 4 and 128 characters")
    //@ValidPassword
    private String password;

    @NotBlank
    //@Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "The phone number must be valid")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Favorites> favorites = new ArrayList<>();

    public User() {
        //
    }

    public User(String username, String email, String password, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Username is required") @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username is required") @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Password is required") @Size(min = 4, max = 128, message = "Password must be between 4 and 128 characters") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required") @Size(min = 4, max = 128, message = "Password must be between 4 and 128 characters") String password) {
        this.password = password;
    }

    public @NotBlank @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "The phone number must be valid") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "The phone number must be valid") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
