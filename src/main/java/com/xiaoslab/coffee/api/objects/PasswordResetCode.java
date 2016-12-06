package com.xiaoslab.coffee.api.objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "password_reset_code")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PasswordResetCode implements Serializable {

    @Id
    @Column(unique = true)
    @NotNull
    @NotEmpty
    private String codeId;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Long userId;

    @Column(nullable = false)
    @NotNull
    @NotEmpty
    private String code;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PasswordResetCode resetCode = (PasswordResetCode) o;

        if (codeId != null ? !codeId.equals(resetCode.codeId) : resetCode.codeId != null) return false;
        if (userId != null ? !userId.equals(resetCode.userId) : resetCode.userId != null) return false;
        return code != null ? code.equals(resetCode.code) : resetCode.code == null;
    }

    @Override
    public int hashCode() {
        int result = codeId != null ? codeId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }

}
