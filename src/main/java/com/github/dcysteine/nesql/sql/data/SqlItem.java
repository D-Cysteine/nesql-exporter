package com.github.dcysteine.nesql.sql.data;

import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "item")
public class SqlItem implements Persistable<String> {
    @Id
    public String id;

    protected SqlItem() {}

    public SqlItem(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
