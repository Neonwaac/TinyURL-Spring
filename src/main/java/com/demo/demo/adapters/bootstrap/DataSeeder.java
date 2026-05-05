package com.demo.demo.adapters.bootstrap;

import com.demo.demo.application.port.out.LinkCachePort;
import com.demo.demo.application.port.out.LinkMetadataRepositoryPort;
import com.demo.demo.application.port.out.LinkRepositoryPort;
import com.demo.demo.domain.model.Link;
import com.demo.demo.domain.model.LinkMetadata;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {
	private static final int CACHE_URL_MIN_LENGTH = 50;

	private final LinkRepositoryPort linkRepository;
	private final LinkMetadataRepositoryPort metadataRepository;
	private final LinkCachePort cachePort;

	public DataSeeder(LinkRepositoryPort linkRepository,
					  LinkMetadataRepositoryPort metadataRepository,
					  LinkCachePort cachePort) {
		this.linkRepository = linkRepository;
		this.metadataRepository = metadataRepository;
		this.cachePort = cachePort;
	}

	@Override
	public void run(String... args) {
		Instant now = Instant.now();
		List<SeedLink> seeds = List.of(
				new SeedLink(
						"Ab12Xy",
						"https://www.google.com",
						5,
						true,
						"https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Google_%22G%22_logo.svg/3840px-Google_%22G%22_logo.svg.png",
						"Buscador Google"),
				new SeedLink(
						"Zx98Qr",
						"https://www.youtube.com/watch?v=WPnZJE3-SSM&list=RDWPnZJE3-SSM&start_radio=1",
						2,
						true,
						"https://upload.wikimedia.org/wikipedia/commons/e/ef/Youtube_logo.png",
						"Plataforma de videos"),
				new SeedLink(
						"Lm45No",
						"https://www.github.com",
						0,
						true,
						"https://images.icon-icons.com/3685/PNG/512/github_logo_icon_229278.png",
						"Repositorio de codigo")
		);

		for (SeedLink seed : seeds) {
			boolean exists = linkRepository.existsByCode(seed.code)
					|| linkRepository.existsByUrlOriginal(seed.originalUrl);
			if (!exists) {
				linkRepository.save(new Link(
						null,
						seed.code,
						seed.originalUrl,
						seed.clicks,
						seed.active,
						now));
			}

			if (metadataRepository.findByCode(seed.code).isEmpty()) {
				metadataRepository.save(new LinkMetadata(
						seed.code,
						seed.image,
						seed.description,
						now));
			}

			if (seed.originalUrl.length() > CACHE_URL_MIN_LENGTH) {
				cachePort.put(seed.code, seed.originalUrl);
			} else {
				cachePort.evict(seed.code);
			}
		}
	}

	private static class SeedLink {
		private final String code;
		private final String originalUrl;
		private final long clicks;
		private final boolean active;
		private final String image;
		private final String description;

		private SeedLink(String code,
						 String originalUrl,
						 long clicks,
						 boolean active,
						 String image,
						 String description) {
			this.code = code;
			this.originalUrl = originalUrl;
			this.clicks = clicks;
			this.active = active;
			this.image = image;
			this.description = description;
		}
	}
}
