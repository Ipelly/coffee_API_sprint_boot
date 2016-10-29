package com.xiaoslab.coffee.api.objects;

import com.xiaoslab.coffee.api.utility.Constants;
import org.apache.commons.lang3.builder.*;

import javax.persistence.*;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long userId;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String emailAddress;

    @Column
    private String phone;

    @Column
    private transient String password;

    @Column
    private transient String passwordSalt;

    @Enumerated(EnumType.ORDINAL)
    @Column
    private Constants.StatusCodes status;

    @Column
    private String providerType;

    @Column
    private String providerUserId;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
                .append(userId)
                .append(firstName)
                .append(lastName)
                .append(emailAddress)
                .append(phone)
                .append(password)
                .append(passwordSalt)
                .append(status)
                .append(providerType)
                .append(providerUserId)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        User user = (User) obj;

        return new EqualsBuilder()
                .append(status, user.status)
                .append(userId, user.userId)
                .append(firstName, user.firstName)
                .append(lastName, user.lastName)
                .append(emailAddress, user.emailAddress)
                .append(phone, user.phone)
                .append(password, user.password)
                .append(passwordSalt, user.passwordSalt)
                .append(providerType, user.providerType)
                .append(providerUserId, user.providerUserId)
                .isEquals();
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
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

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

}
