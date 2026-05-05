package com.demo.demo.adapters.persistence.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LinkJpaRepository extends JpaRepository<LinkJpaEntity, Long> {
	Optional<LinkJpaEntity> findByCode(String code);

	boolean existsByUrlOriginal(String urlOriginal);

	boolean existsByCode(String code);

	List<LinkJpaEntity> findAllByOrderByCreatedAtDesc();

	@Modifying
	@Query("update LinkJpaEntity l set l.clicks = l.clicks + 1 where l.code = :code")
	void incrementClicks(@Param("code") String code);

	void deleteByCode(String code);
}
