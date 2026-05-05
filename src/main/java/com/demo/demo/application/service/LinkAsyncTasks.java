package com.demo.demo.application.service;

import com.demo.demo.application.port.out.LinkCachePort;
import com.demo.demo.application.port.out.LinkMetadataRepositoryPort;
import com.demo.demo.application.port.out.LinkRepositoryPort;
import com.demo.demo.domain.model.LinkMetadata;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class LinkAsyncTasks {
	private final LinkMetadataRepositoryPort metadataRepository;
	private final LinkCachePort cachePort;
	private final LinkRepositoryPort linkRepository;

	public LinkAsyncTasks(LinkMetadataRepositoryPort metadataRepository,
						  LinkCachePort cachePort,
						  LinkRepositoryPort linkRepository) {
		this.metadataRepository = metadataRepository;
		this.cachePort = cachePort;
		this.linkRepository = linkRepository;
	}

	@Async("appTaskExecutor")
	public void saveMetadata(LinkMetadata metadata) {
		metadataRepository.save(metadata);
	}

	@Async("appTaskExecutor")
	public void cacheLongUrl(String code, String originalUrl) {
		cachePort.put(code, originalUrl);
	}

	@Async("appTaskExecutor")
	public void incrementClicks(String code) {
		linkRepository.incrementClicks(code);
	}
}
