package com.demo.demo.adapters.persistence.mongo;

import com.demo.demo.application.port.out.LinkMetadataRepositoryPort;
import com.demo.demo.domain.model.LinkMetadata;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LinkMetadataMongoAdapter implements LinkMetadataRepositoryPort {
	private final LinkMetadataMongoRepository repository;

	public LinkMetadataMongoAdapter(LinkMetadataMongoRepository repository) {
		this.repository = repository;
	}

	@Override
	public void save(LinkMetadata metadata) {
		LinkMetadataDocument document = new LinkMetadataDocument();
		document.setCode(metadata.getCode());
		document.setImage(metadata.getImage());
		document.setDescription(metadata.getDescription());
		document.setCreatedAt(metadata.getCreatedAt());
		repository.save(document);
	}

	@Override
	public Optional<LinkMetadata> findByCode(String code) {
		return repository.findById(code)
				.map(document -> new LinkMetadata(
					document.getCode(),
					document.getImage(),
					document.getDescription(),
					document.getCreatedAt()));
	}

	@Override
	public void deleteByCode(String code) {
		repository.deleteById(code);
	}
}
