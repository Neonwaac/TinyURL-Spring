package com.demo.demo.application.port.out;

import com.demo.demo.domain.model.Link;

import java.util.List;
import java.util.Optional;

public interface LinkRepositoryPort {
	Link save(Link link);

	Optional<Link> findByCode(String code);

	boolean existsByUrlOriginal(String originalUrl);

	boolean existsByCode(String code);

	List<Link> findAll();

	void incrementClicks(String code);

	void deleteByCode(String code);
}
