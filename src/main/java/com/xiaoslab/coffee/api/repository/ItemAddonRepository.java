package com.xiaoslab.coffee.api.repository;

import com.xiaoslab.coffee.api.objects.ItemAddon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface ItemAddonRepository extends JpaRepository<ItemAddon, Long>, JpaSpecificationExecutor<ItemAddon> {

}
