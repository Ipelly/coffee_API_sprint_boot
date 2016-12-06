package com.xiaoslab.coffee.api.repository;

import com.xiaoslab.coffee.api.objects.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, String>, JpaSpecificationExecutor<PasswordResetCode> {

}
