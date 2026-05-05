package com.demo.demo.adapters.persistence.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LinkMetadataMongoRepository extends MongoRepository<LinkMetadataDocument, String> {
}
