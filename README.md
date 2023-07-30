## Application Documentation

### How to build
Please replace the private repository url with proper public repository url in `build.gradle` and `settings.gradle` before building the application
    
    ./gradlew build

### How to test
    ./gradlew test

### How to run
    ./gradlew run

### Swagger
When running locally, the Swagger ui can be accessed at http://localhost:8080/swagger/views/swagger-ui/index.html

Please refer to the API documentation on the Swagger page 

### Database
This prototype uses a h2 database for persistence

### Open items
This prototype is built based on the requirement document (see the pdf included in the repository).
This only covers the basic features mentioned in the requirement document.

Further features are likely brought into the scope in future iterations. 

To name a few:
1. Inventory Management
2. More types of Discount
3. Sort and Pagination
4. Payment Management
5. Service Security
