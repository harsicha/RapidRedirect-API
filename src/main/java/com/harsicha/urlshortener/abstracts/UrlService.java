package com.harsicha.urlshortener.abstracts;

import com.harsicha.urlshortener.exception.*;

import java.security.NoSuchAlgorithmException;

public interface UrlService {
    String saveUrl(String url) throws NoSuchAlgorithmException, ValidStringNotFoundException, UrlNotValidException;
    String getUrl(String random);
}
