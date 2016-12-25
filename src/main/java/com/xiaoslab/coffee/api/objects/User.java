package com.xiaoslab.coffee.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails, Serializable {

    // Markers for Group Validations
    public @interface XipliUser{}
    public @interface ProviderUser{}
    public @interface ShopUser{}

    @Id
    @GeneratedValue
    @Column(unique = true, name = "user_id")
    private Long userId;

    @Column(nullable = false)
    @NotEmpty
    private String firstName;

    @Column
    private String lastName;

    @Transient
    private String name;

    @Column
    @NotEmpty(groups = XipliUser.class)
    @Email
    private String emailAddress;

    @Column
    private String phone;

    @Column
    @NotEmpty(groups = XipliUser.class)
    protected String password;

    @Enumerated(EnumType.ORDINAL)
    @Column
    @NotNull
    private Constants.StatusCodes status;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "provider_type_id")
    @NotNull(groups = ProviderUser.class)
    private Constants.LoginProviderType providerType;

    @Column
    @NotEmpty(groups = ProviderUser.class)
    private String providerUserId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role", referencedColumnName = "role"))
    @NotEmpty(groups = ShopUser.class)
    private Collection<AppAuthority> roles;

    @Column
    @NotNull(groups = ShopUser.class)
    private Long shopId;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(name, user.name) &&
                Objects.equals(emailAddress, user.emailAddress) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(password, user.password) &&
                Objects.equals(status, user.status) &&
                Objects.equals(providerType, user.providerType) &&
                Objects.equals(providerUserId, user.providerUserId) &&
                Objects.equals(shopId, user.shopId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, firstName, lastName, name, emailAddress, phone, password, status, providerType, providerUserId, shopId);
    }

    public static class NewUserRequest extends User {
        @Override
        @JsonProperty(access = JsonProperty.Access.READ_WRITE)
        public String getPassword() {
            return super.password;
        }
        @Override
        @JsonProperty(access = JsonProperty.Access.READ_WRITE)
        public void setPassword(String password) {
            super.password = password;
        }
    }

    @JsonIgnore
    public static User copyFromNewUserRequest(NewUserRequest newUserRequest) {
        User newUser = new User();
        newUser.setPassword(newUserRequest.getPassword());
        newUser.setEmailAddress(newUserRequest.getEmailAddress());
        newUser.setFirstName(newUserRequest.getFirstName());
        newUser.setLastName(newUserRequest.getLastName());
        newUser.setName(newUserRequest.getName());
        newUser.setRoles(newUserRequest.getRoles());
        return newUser;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        if (StringUtils.isBlank(this.name)) {
            if (StringUtils.isBlank(this.firstName) && StringUtils.isBlank(this.lastName)) {
                return null;
            } else {
                return (this.firstName == null ? "" : this.firstName) + (this.lastName == null ? "" : " " + this.lastName);
            }
        } else {
            return this.name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Constants.StatusCodes getStatus() {
        return status;
    }

    public void setStatus(Constants.StatusCodes status) {
        this.status = status;
    }

    public Constants.LoginProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(Constants.LoginProviderType providerType) {
        this.providerType = providerType;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> simpleAuthorities = new HashSet<>();
        getRoles().forEach(authority -> simpleAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority())));
        return simpleAuthorities;
    }

    public Collection<AppAuthority> getRoles() {
        return roles;
    }

    public void setRoles(Collection<AppAuthority> roles) {
        this.roles = roles;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return getUserId() + "_" + getEmailAddress();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return status != Constants.StatusCodes.DELETED;
    }
}
