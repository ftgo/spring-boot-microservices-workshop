# [Spring Boot Microservices Level 1: Communication and Discovery](https://www.youtube.com/playlist?list=PLqq-6Pq4lTTbXZY_elyGv7IkKrfkSrX5e)
# [Spring Boot Microservices Level 2: Fault Tolerance and Resilience](https://www.youtube.com/playlist?list=PLqq-6Pq4lTTZSKAFG6aCDVDP86Qx4lNas)


## Eureka server
* [`http://localhost:8761`](http://localhost:8761)

## Hystrix dashboard
* [`http://localhost:8081/hystrix`](http://localhost:8081/hystrix)
  * Search: `http://localhost:8081/actuator/hystrix.stream`
  * Make requests to the Catalog (with and without depending on services)

## Http Requests
```
### GET /catalog/foo

GET http://localhost:8081/catalog/foo


### GET /ratingsdata/user/foo

GET http://localhost:8083/ratingsdata/user/foo


### GET /ratingsdata/movies/100
GET http://localhost:8083/ratingsdata/movies/100


### GET /movies/100

GET http://localhost:8082/movies/100
```