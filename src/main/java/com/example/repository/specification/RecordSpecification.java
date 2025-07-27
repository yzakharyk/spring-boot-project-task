package com.example.repository.specification;

import com.example.model.dto.RecordFilter;
import com.example.model.entity.Record;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class RecordSpecification {

    public static Specification<Record> withFilters(RecordFilter filter) {
        return (root, query, criteriaBuilder) -> {
            var predicates = new ArrayList<Predicate>();

            if (filter.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
            }
            if (filter.getName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("name"),  filter.getName() ));
            }
            if (filter.getCreatedBy() != null) {
                predicates.add(criteriaBuilder.equal(root.get("createdBy"), filter.getCreatedBy()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
