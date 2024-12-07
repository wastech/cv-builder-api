package com.wastech.cv_builder_api.util;

import com.wastech.cv_builder_api.model.CV;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CVSpecification {
    public static Specification<CV> filterCVs(
        String title,
        String languageCode,
        String status,
        LocalDateTime createdAfter,
        LocalDateTime createdBefore,
        Boolean isDeleted
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + title.toLowerCase() + "%"
                ));
            }

            if (languageCode != null) {
                predicates.add(criteriaBuilder.equal(root.get("languageCode"), languageCode));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (createdAfter != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdAfter));
            }

            if (createdBefore != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdBefore));
            }

            if (isDeleted != null) {
                predicates.add(criteriaBuilder.equal(root.get("isDeleted"), isDeleted));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}