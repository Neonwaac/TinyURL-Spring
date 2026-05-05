package com.demo.demo.adapters.persistence.redis;

import com.demo.demo.application.port.out.LinkCachePort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LinkCacheRedisAdapter implements LinkCachePort {
	private static final String KEY_PREFIX = "link:";

	private final StringRedisTemplate template;

	public LinkCacheRedisAdapter(StringRedisTemplate template) {
		this.template = template;
	}

	@Override
	public void put(String code, String originalUrl) {
		template.opsForValue().set(KEY_PREFIX + code, originalUrl);
	}

	@Override
	public Optional<String> get(String code) {
		return Optional.ofNullable(template.opsForValue().get(KEY_PREFIX + code));
	}

	@Override
	public boolean isCached(String code) {
		Boolean exists = template.hasKey(KEY_PREFIX + code);
		return exists != null && exists;
	}

	@Override
	public void evict(String code) {
		template.delete(KEY_PREFIX + code);
	}
}
