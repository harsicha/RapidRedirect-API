package com.harsicha.urlshortener.controller;

import com.harsicha.urlshortener.abstracts.UrlService;
import com.harsicha.urlshortener.dto.Url;
import com.harsicha.urlshortener.exception.UrlNotValidException;
import com.harsicha.urlshortener.exception.ValidStringNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.NoSuchAlgorithmException;

@RestController
public class UrlController {

    private final UrlService urlService;

    @Autowired
    public UrlController(@Qualifier("v2") UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{random}")
    public ResponseEntity<Object> get(@PathVariable("random") String random) {
        try {
            String url = urlService.getUrl(random);
            return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).location(URI.create(url)).build();
        }
        catch (NullPointerException ex) {
            return new ResponseEntity<>("Invalid short url", HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Invalid request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<String> save(@RequestBody Url url) {
        try {
            var response = urlService.saveUrl(url.getUrl());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (UrlNotValidException ex) {
            return new ResponseEntity<>("Please enter a valid url", HttpStatus.BAD_REQUEST);
        }
        catch (ValidStringNotFoundException ex) {
            return new ResponseEntity<>("Please try a different url", HttpStatus.CONFLICT);
        }
        catch (NoSuchAlgorithmException ex) {
            return new ResponseEntity<>("Service unavailable", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception ex) {
            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        }
    }
}
