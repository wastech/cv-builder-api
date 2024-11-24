package com.wastech.cv_builder_api.repository;

import com.wastech.cv_builder_api.model.Cv;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CvRepository extends JpaRepository<Cv, Long> {
}
