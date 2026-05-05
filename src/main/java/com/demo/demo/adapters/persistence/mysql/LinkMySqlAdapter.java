package com.demo.demo.adapters.persistence.mysql;

import com.demo.demo.application.port.out.LinkRepositoryPort;
import com.demo.demo.domain.exception.DuplicateLinkException;
import com.demo.demo.domain.model.Link;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LinkMySqlAdapter implements LinkRepositoryPort {
	private final LinkJpaRepository repository;
	private final LinkMapper mapper = new LinkMapper();

	public LinkMySqlAdapter(LinkJpaRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public Link save(Link link) {
		try {
			LinkJpaEntity saved = repository.save(mapper.toEntity(link));
			return mapper.toDomain(saved);
		} catch (DataIntegrityViolationException ex) {
			throw new DuplicateLinkException("Ese enlace ya fue registrado. Puedes usar el existente o probar con otra URL.");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Link> findByCode(String code) {
		return repository.findByCode(code).map(mapper::toDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByUrlOriginal(String originalUrl) {
		return repository.existsByUrlOriginal(originalUrl);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByCode(String code) {
		return repository.existsByCode(code);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Link> findAll() {
		return repository.findAllByOrderByCreatedAtDesc()
				.stream()
				.map(mapper::toDomain)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void incrementClicks(String code) {
		repository.incrementClicks(code);
	}

	@Override
	@Transactional
	public void deleteByCode(String code) {
		repository.deleteByCode(code);
	}
}
