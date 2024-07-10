### info
Two endpoints are required
`/encode` - Encodes a URL to a shortened URL
`/decode` - Decodes a shortened URL to its original URL.
Both endpoints should return JSON
o

### Example
`https://example.com/library/react` to be comverted into something like
`http://short.est/GeAi9K`
and vise versa
### Usage
* compile
```sh
mvn compile
```
* run skipping test
```sh
mvn -Dmaven.test.skip=true spring-boot:run
```
```sh
curl -sX GET http://localhost:8085/basic/encode/xxxx | jq
```
```json
{
  "result": "JD+mPIWm"
}

```
```sh
curl -s -vX GET http://localhost:8085/basic/encode/xxxx
```
```text
* Connected to localhost (::1) port 8085 (#0)
> GET /basic/encode/xxxx HTTP/1.1
> Host: localhost:8085
> User-Agent: curl/7.75.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Tue, 09 Jul 2024 20:16:16 GMT
<
{ [27 bytes data]
* Connection #0 to host localhost left intact
```
```sh
curl -sX GET http://localhost:8085/basic/decode/JD+mPIWm
```
```text
{"result":"xxxx"}
```

```sh
curl -s -X POST -d '{"url": "xxxx"}' http://localhost:8085/basic/encode
```
```json
{"timestamp":"2024-07-09T21:41:10.460+00:00","status":415,"error":"Unsupported Media Type","message":"","path":"/basic/encode"}
```
* provide header - still not working
```
curl -s -v -X POST -H 'Content-Type: application/json' -d '{"url": "xxxx", "x": 123, "y": 456}' http://localhost:8085/basic/encode
```
```text
 POST /basic/encode HTTP/1.1
> Host: localhost:8085
> User-Agent: curl/7.75.0
> Accept: */*
> Content-Type: application/json
> Content-Length: 35
>
} [35 bytes data]
* Mark bundle as not supporting multiuse
< HTTP/1.1 501
< Content-Length: 0
< Date: Tue, 09 Jul 2024 21:47:10 GMT
< Connection: close

```

server log show blank payload:
```txt
2024-07-09 17:46:05.906  INFO 3016 --- [nio-8085-exec-1] example.controller.Exam
pleController     : processing {}
2024-07-09 17:46:05.908  INFO 3016 --- [nio-8085-exec-1] example.controller.Exam
pleController     : processing null
```
* test - uses `RestTemplate`
```sh
mvn test
```
### Note

No actual redirect is needed, just a endpoint 

