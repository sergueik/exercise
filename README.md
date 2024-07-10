### info
This is an URL shortening exerfise

### Features

Two endpoints are supported:

endpoint | method | description
 --- | ---  | --- 
 `/encode` | `POST` | Encodes a URL to a shortened URL hosted on a host `short`
 `/decode` | `POST` | Decodes a shortened URL to its original URL

Both endpoints need JSON payload of the following structure:
```JSON
{
 "url": "https://www.google.com/"
}
```
and produce JSON of the following structure:
```JSON
{
  "result":"http://short/0OGWoMJd"
}
```
the short URL `0OGWoMJd` is a result of applying
 + `MessageDigest.getInstance("SHA-256").update()`
 + `Base64.encodeBase64`
 + `String.substring(1,8)` 
to the original url `https://www.google.com/` and the hostname in the short URL `http://short` is configurable property
defined in `application.properties`:
```java
example.host = http://short
```

The early development versions of the exercise supported `GET` requests on `/encode` and `/decode` with the `url` provided through the path variable, 
however formatting a real `url` argument as a path variable or a query string would require encoding the special characters and is inconvenient for a user.
These end points now advise to use `POST`:

![advise](https://github.com/sergueik/exercise/blob/master/screenshots/capture-advise.png)

### Usage

#### Build the Application

* compile
```sh
mvn compile
```
* test
```sh
mvn test
```
* run the application locally, skipping test
```sh
mvn -Dmaven.test.skip=true spring-boot:run
```
### Use the Application

#### Shorten The URL
To convert an URL, construct the POST request in [Postman](https://www.postman.com/downloads/) or [curl](https://curl.se/download.html)
```sh
curl -s -X POST -H 'Content-Type: application/json' -d '{ "url": "https://www.google.com/"}' http://localhost:8085/encode
```
it will respond with
```JSON
{
  "result":"http://short/0OGWoMJd"
}
```
or equivalent Postman command

![encode](https://github.com/sergueik/exercise/blob/master/screenshots/capture-encode.png)



#### Reconstruct the Long URL

use the command

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

![decode](https://github.com/sergueik/exercise/blob/master/screenshots/capture-decode.png)

#### Error Processing

When decode an unknown short URL is attempted, the response code will be 404 status not found.
when there is a problem with parsing the payload JSON or the URL in the JSON, server will also respond with the 501 status non implemented

![error](https://github.com/sergueik/exercise/blob/master/screenshots/capture-nodata.png)

### Notes

* No actual redirect is implemented, just API to encode a long string to a short string and vise versa.

* To produce the short URL, the hashing and base64 encoding are applied. 
The same long URL will always be encoded in the same short url. Adding optional random suffix is a work in progress
Adding the Swagger annotation is a work in progress 

### Author
[Serguei Kouzmine](kouzmine_serguei@yahoo.com)
