# RapidRedirect-API  
An API to submit a long url and get a shortened url.

## Languages & Tools used
- Java
- Spring Boot
- JUnit
- Gradle
- SQL / PostgreSQL
- IntelliJ
- Postman for testing

## How is the shortened URL created?
- Whenever a long url is submitted, an MD5 hash is generated for this url
- This MD5 hash is represented in Base62 string
- The first 7 characters of this string is used as the short url
- But an MD5 is 32 chars long and if I am taking the first 7 chars everytime there is chance that it might be repeated you ask, absolutely! The first 7 chars can the same for different urls that is why I am checking whether it exists or not in the database, if it does then I take the next sequence of 7 chars from the MD5 hash as the short url
- I then map the long url input to this short url in the datasbase and output the short url in the response

## FAQ
#### Q: Why not assign a randomly generated Base62 string of 7 chars instead of following such a lengthy process?  
1. We will get the same MD5 hash, if we input the same string everytime!
2. If a url already exists in the database I am responsing with the existing mapping present in the database. To avoid searching the urls in database I can get the 7 char string from the long url itself(by following the aforementioned process) and then search this string in database column which is indexed and would provide quick results. I am not indexing the long urls to save space
