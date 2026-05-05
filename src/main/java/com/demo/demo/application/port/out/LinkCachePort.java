package com.demo.demo.application.port.out;

import java.util.Optional;

public interface LinkCachePort {
	void put(String code, String originalUrl);

	Optional<String> get(String code);

	boolean isCached(String code);

	void evict(String code);
}
