## Key documentation

Refer to [doc/adr](doc/adr) in order to know more about decisions taken in the project.

## Running the project

Just execute the script `buildAndStart.sh`, from anywhere in the machine (make sure your `JAVA_HOME` points to JDK 17). 
It will trigger the execution of Gradle for building, testing, and ultimately building a new Docker image.
Then, it will use `docker-compose` to bootstrap the system, including its database. 

Please notice that the first run might take some time to run, given that it will perform several verifications and
also download the most up-to-date vulnerability database. 

Once it's running, you can refer to http://localhost:8080/swagger-ui.html in order to interact with its API's. 