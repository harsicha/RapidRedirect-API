package com.harsicha.urlshortener.service;

import com.harsicha.urlshortener.abstracts.UrlService;
import com.harsicha.urlshortener.exception.UrlNotValidException;
import com.harsicha.urlshortener.model.UrlMapping;
import com.harsicha.urlshortener.repository.UrlMappingRepository;
import com.harsicha.urlshortener.utility.Base62;
import com.harsicha.urlshortener.utility.CommonMethods;
import com.harsicha.urlshortener.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
@Qualifier("v1")
public class UrlServiceV1 implements UrlService {

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @Override
    public String saveUrl(String url) throws NoSuchAlgorithmException, UrlNotValidException {
        if (!CommonMethods.isUrlValid(url)) {
            throw new UrlNotValidException(url);
        }

        if (urlMappingRepository.existsByUrl(url)) {
            UrlMapping existMapping =  urlMappingRepository.findByUrl(url);
            return Constants.BASE_URL + existMapping.getRandom();
        }

        String random = getRandom();
        UrlMapping mapping = new UrlMapping();
        mapping.setUrl(url);
        mapping.setRandom(random);

        urlMappingRepository.saveAndFlush(mapping);

        return Constants.BASE_URL + random;
    }

    private String getRandom() {
        String random;
        do {
            random = Base62.Random();
        } while (urlMappingRepository.existsByRandom(random));

        return random;
    }

    public String getUrl(String random) {
        return urlMappingRepository.findByRandom(random).getUrl();
    }
}
