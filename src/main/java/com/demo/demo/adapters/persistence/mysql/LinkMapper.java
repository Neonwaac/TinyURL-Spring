package com.demo.demo.adapters.persistence.mysql;

import com.demo.demo.domain.model.Link;

public class LinkMapper {
	public LinkJpaEntity toEntity(Link link) {
		LinkJpaEntity entity = new LinkJpaEntity();
		entity.setId(link.getId());
		entity.setCode(link.getCode());
		entity.setUrlOriginal(link.getOriginalUrl());
		entity.setClicks(link.getClicks());
		entity.setActive(link.isActive());
		entity.setCreatedAt(link.getCreatedAt());
		return entity;
	}

	public Link toDomain(LinkJpaEntity entity) {
		return new Link(
				entity.getId(),
				entity.getCode(),
				entity.getUrlOriginal(),
				entity.getClicks(),
				entity.isActive(),
				entity.getCreatedAt());
	}
}
