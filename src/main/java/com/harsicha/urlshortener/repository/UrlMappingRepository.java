package com.harsicha.urlshortener.repository;

import com.harsicha.urlshortener.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, UUID> {

    boolean existsByUrl(String url);
    boolean existsByRandom(String random);
    UrlMapping findByUrl(String url);
    UrlMapping findByRandom(String random);

}
