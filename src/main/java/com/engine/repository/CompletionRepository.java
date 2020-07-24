package com.engine.repository;

import com.engine.model.CompletionEntity;
import com.engine.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletionRepository extends JpaRepository<CompletionEntity, Long> {
   Page<CompletionEntity> findByUser(UserEntity userEntity, Pageable pageable);
}
