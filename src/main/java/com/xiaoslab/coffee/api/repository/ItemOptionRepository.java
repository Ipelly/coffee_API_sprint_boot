package com.xiaoslab.coffee.api.repository;

import com.xiaoslab.coffee.api.objects.ItemOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ipeli on 10/29/16.
 */


@Repository
@Transactional
public interface ItemOptionRepository extends JpaRepository<ItemOption, Long>, JpaSpecificationExecutor<ItemOption> {

}
