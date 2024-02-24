package com.harsicha.urlshortener.utility;

import org.apache.commons.validator.routines.UrlValidator;

public class CommonMethods {

    public static boolean isUrlValid(String url) {
        UrlValidator validator = new UrlValidator();
        return validator.isValid(url);
    }

}