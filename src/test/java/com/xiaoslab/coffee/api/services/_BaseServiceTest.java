package com.xiaoslab.coffee.api.services;

import com.xiaoslab.coffee.api.objects.Category;
import com.xiaoslab.coffee.api.objects.Item;
import com.xiaoslab.coffee.api.objects.Shop;
import com.xiaoslab.coffee.api.repository.CategoryRepository;
import com.xiaoslab.coffee.api.repository.ItemRepository;
import com.xiaoslab.coffee.api.utilities.ServiceLoginUtils;
import com.xiaoslab.coffee.api.utilities.TestUtils;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@Transactional
@Rollback
@Sql(scripts = "classpath:database/ClearDatabaseForTest.sql")
@SpringBootTest
public abstract class _BaseServiceTest {

    @Autowired
    protected IService<Item> itemService;

    @Autowired
    protected IService<Category> categoryService;

    @Autowired
    protected IService<Shop> shopService;

    @Autowired
    protected ItemRepository itemRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected ServiceLoginUtils serviceLoginUtils;

    @Autowired
    protected TestUtils testUtils;

    @Autowired
    protected EntityManager entityManager;

    protected Logger getLogger() {
        return Logger.getLogger(this.getClass());
    }

    // This method is needed to refresh the bi-directional entities in JPA repository.
    // Otherwise JPA repository is unable to pickup the changes done from one side.
    protected void refreshEntities() {
        entityManager.flush();
        for (Category category : categoryRepository.findAll()) {
            entityManager.refresh(category);
        }
        for (Item item : itemRepository.findAll()) {
            entityManager.refresh(item);
        }
    }
}
