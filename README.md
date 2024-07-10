## info

### Objective
Two endpoints are required:
* `/encode` - Encodes a URL to a shortened URL
* `/decode` - Decodes a shortened URL to its original URL.

Both endpoints should produce JSON with the short URL

### Example

`https://example.com/library/react` to be converted into something like
`http://short.est/GeAi9K`
and vise versa


### Usage

* compile
```sh
mvn compile
```
* test - uses `RestTemplate`
```sh
mvn test
```
* run skipping test
```sh
mvn -Dmaven.test.skip=true spring-boot:run
```
To convert an URL, construct the POST  request in Postman or curl 
```sh
curl -s -X POST -H 'Content-Type: application/json' -d '{ "url": "https://www.google.com/"}' http://localhost:8085/encode
```
it will respond with
```JSON
{
  "result":"http://short/0OGWoMJd"
}
```

To reconstruct the long url use the 

```sh
curl -s -X POST -H 'Content-Type: application/json' -d '{ "url": "http://short/0OGWoMJd"}' http://localhost:8085/decode
```

this will respond with
```JSON
{
  "result": "https://www.google.com/"
}
```
Alternatively do it in Postman

![encode](https://github.com/sergueik/puppetmaster_vagrant/blob/master/exercise/screenshots/capture-encode.png)

![decode](https://github.com/sergueik/puppetmaster_vagrant/blob/master/exercise/screenshots/capture-decode.png)

The host name `http://short` is configurable via `application.properties`:
```java
example.host = http://short
```

When an unknown short URL is attempted, the server will respond with a 404 status not found.
when there is a problem with parsing the payload JSON or the URL in the JSON, server will also respond with the 501 status non implemented

![error](https://github.com/sergueik/puppetmaster_vagrant/blob/master/exercise/screenshots/capture-nodata.png)
### Notes

* The early development versions of the exercise supported `GET` requests on `/encode` and `/decode` with the `url` in the path variable, however formatting an `url` argument as a path variable or a query string would require encoding the special characters and is inconvenient for a user.
These end points now advise to use `POST`.

* No actual redirect is implemented, just API endpoints to convert a long string to a short string and vise versa.

* To produce the short URL, the following methods  are applied:

 + `MessageDigest.getInstance("SHA-256").update()`
 + `Base64.encodeBase64`
 + `String.substring(1,8)` 

With this approach the same long url will always be encoded in the same short url. Adding optional random suffix is a work in progress


