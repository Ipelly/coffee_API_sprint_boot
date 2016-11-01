package com.xiaoslab.coffee.api.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class AppAuthority {

    @Id
    @Column(name = "role")
    private String authority;

    public AppAuthority() {
        
    }

    public AppAuthority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
