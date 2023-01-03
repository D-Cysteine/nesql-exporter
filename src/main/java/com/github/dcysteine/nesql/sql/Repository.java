package com.github.dcysteine.nesql.sql;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

// Prevent Spring from creating a Bean for this class, since it's just a convenience interface.
@Conditional(Repository.FalseCondition.class)
public interface Repository<R extends Identifiable<K>, K extends Comparable<K>>
        extends JpaRepository<R, K>, JpaSpecificationExecutor<R> {
    class FalseCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return false;
        }
    }
}
