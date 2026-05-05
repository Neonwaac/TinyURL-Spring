package com.demo.demo.application.port.out;

import com.demo.demo.domain.model.LinkMetadata;

import java.util.Optional;

public interface LinkMetadataRepositoryPort {
	void save(LinkMetadata metadata);

	Optional<LinkMetadata> findByCode(String code);

	void deleteByCode(String code);
}
