# Spring Data OpenSearch Example

Instructions:
- Start the OpenSearch containers (two nodes and the OpenSearch Dashboard) with `docker-compose up`
- Access the OpenSearch Dashboard at `http://localhost:5601`
- Start the application with `./gradlew bootRun`
- Use the POST "/api/widgets" endpoint to add documents to the OpenSearch "widget" index
```
POST http://localhost:8080/api/widgets
Content-Type: application/json

{
  "name": "Widget Original",
  "brandName": "ADMIN",
  "manufacturerYear": 2018,
  "serialNumber": "573522"
}
```
- Use the GET "/api/widgets/search" endpoint to perform a fuzzy search query on the "widget" index (name and brandName fields)
```
GET http://localhost:8080/api/widgets/search?query=acme
```

Inspired by the following examples:

- https://github.com/anicetkeric/spring-boot-opensearch/
- https://github.com/M-Razavi/Spring-Data-OpenSearch-Example

