package com.github.dcysteine.nesql.exporter.handler.minecraft;

import com.github.dcysteine.nesql.exporter.handler.AbstractSaver;
import com.github.dcysteine.nesql.sql.data.SqlItem;
import com.github.dcysteine.nesql.sql.repository.ItemRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.persistence.EntityManager;

public class ItemSaver extends AbstractSaver {
    private final ItemRepository repository;

    public ItemSaver(EntityManager entityManager) {
        super(entityManager);
        repository = new JpaRepositoryFactory(entityManager).getRepository(ItemRepository.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void saveImpl() {
        repository.save(new SqlItem("TEST TEST TEST AAAAA"));
        repository.save(new SqlItem("123"));
        repository.save(new SqlItem("456"));
    }
}
