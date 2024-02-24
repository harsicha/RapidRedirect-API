package com.harsicha.urlshortener.service;

import com.harsicha.urlshortener.abstracts.UrlService;
import com.harsicha.urlshortener.exception.UrlNotValidException;
import com.harsicha.urlshortener.exception.ValidStringNotFoundException;
import com.harsicha.urlshortener.model.UrlMapping;
import com.harsicha.urlshortener.repository.UrlMappingRepository;
import com.harsicha.urlshortener.utility.CommonMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.harsicha.urlshortener.utility.Constants.*;

@Service
@Qualifier("v2")
public class UrlServiceV2 implements UrlService {

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @Override
    public String saveUrl(String url) throws NoSuchAlgorithmException, ValidStringNotFoundException, UrlNotValidException {
        if (!CommonMethods.isUrlValid(url)) {
            throw new UrlNotValidException(url);
        }
        String check = checkUrlExists(url);
        if (check != null) {
            return BASE_URL + check;
        }

        String random = getValid7DigitString(url);
        UrlMapping mapping = new UrlMapping();
        mapping.setUrl(url);
        mapping.setRandom(random);

        urlMappingRepository.saveAndFlush(mapping);

        return BASE_URL + random;
    }

    private String checkUrlExists(String url) throws NoSuchAlgorithmException {
        String hash = getMD5Hash(url);
        for (int i = 0; i < hash.length() - SHORT_LENGTH; i++) {
            String sequence = hash.substring(i, i + SHORT_LENGTH - 1);
            UrlMapping mapping = urlMappingRepository.findByRandom(sequence);
            if (mapping != null && mapping.getUrl().equals(url)) {
                return mapping.getRandom();
            }
        }
        return null;
    }

    private String getValid7DigitString(String url) throws NoSuchAlgorithmException, ValidStringNotFoundException {
        String hash = getMD5Hash(url);
        for (int i = 0; i < hash.length() - SHORT_LENGTH; i++) {
            String sequence = hash.substring(i, i + SHORT_LENGTH - 1);
            UrlMapping mapping = urlMappingRepository.findByRandom(sequence);
            if (mapping == null) {
                return sequence;
            }
        }
        throw new ValidStringNotFoundException(url);
    }

    private String getMD5Hash(String input) throws NoSuchAlgorithmException {
        // Generate MD5 bytes
        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
        md.update(input.getBytes());
        byte[] digest = md.digest();

        // Encode MD5 to string
        return byteArrayToBase62(digest);
    }

    // Possible combinations = 16^7 = 268M
    private String hexEncode(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
        }

        return sb.toString();
    }

    // Possible combinations = 62^7 = 3.5T
    private static String byteArrayToBase62(byte[] byteArray) {
        StringBuilder sb = new StringBuilder();

        // Convert each byte to base62
        for (byte b : byteArray) {
            int value = b & 0xFF; // Convert byte to unsigned int
            while (value > 0) {
                sb.insert(0, BASE62_CHARSET.charAt(value % 62));
                value /= 62;
            }
        }

        return sb.toString();
    }

    public String getUrl(String random) {
        return urlMappingRepository.findByRandom(random).getUrl();
    }
}
