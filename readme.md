# Running the application
> ** __NOTE:__ ** This guide and all the commands within assume you're running in a Unix environment.
## Setting up the app
- This application runs on Java 17, so first we need to make sure that we have Java 17 set up.
	- Verify java version by running `java -version` in the terminal.
	- If java isn't installed or you're not on 17, you'll need to [set up Java 17 before proceeding](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/generic-linux-install.html).
- Now that you're running the latest version of Java, it's time to clone the [git repo for this project from Github](https://github.com/jack28drew/jd0823).
	- In the terminal, navigate to the location you'd like to save the project and run `git clone https://github.com/jack28drew/jd0823.git` and then step inside the root directory of the project.
- Run `./gradlew build` to build the project.

  You're all set! You should be able to run the application and execute tests now.

## Running tests
### Unit tests
To run the unit test suite with gradle:
```console
./gradlew clean test
```
### Integration tests
To Run the integration test suite with gradle:
```console
./gradlew clean intTest
```
### Required test cases
There are a couple ways to validate the test cases requested in the requirements:
- jUnit tests can be found at [`src/intTest/java/com/tool_rental/RequiredTestCases.java`](https://github.com/jack28drew/jd0823/blob/main/src/intTest/java/com/tool_rental/RequiredTestCases.java); we can execute them by running.
```console
./gradlew clean intTest --tests RequiredTestCases
```
- We can also validate the inputs in a more interactive way by manually sending requests to the `/checkout` endpoint and then validating the response.
	- Start the application.
	```console
	./gradlew clean bootRun
	```
	- Execute cURL commands for each of the provided test cases.
		- test case 1
		```console
		curl --header "Content-Type: application/json" --request POST http://localhost:8081/api/tool-rental/checkout --data '{ "tool_code": "JAKR", "checkout_date": "09/03/15", "rental_days": 5, "discount_percent": 101 }'
		```
		- test case 2
		```console
		curl --header "Content-Type: application/json" --request POST http://localhost:8081/api/tool-rental/checkout --data '{ "tool_code": "LADW", "checkout_date": "07/02/20", "rental_days": 3, "discount_percent": 10 }'
		```
		- test case 3
		```console
		curl --header "Content-Type: application/json" --request POST http://localhost:8081/api/tool-rental/checkout --data '{ "tool_code": "CHNS", "checkout_date": "07/02/15", "rental_days": 5, "discount_percent": 25 }'
		```
		- test case 4
		```console
		curl --header "Content-Type: application/json" --request POST http://localhost:8081/api/tool-rental/checkout --data '{ "tool_code": "JAKD", "checkout_date": "09/03/15", "rental_days": 6, "discount_percent": 0 }'
		```
		- test case 5
		```console
		curl --header "Content-Type: application/json" --request POST http://localhost:8081/api/tool-rental/checkout --data '{ "tool_code": "JAKR", "checkout_date": "07/02/15", "rental_days": 9, "discount_percent": 0 }'
		```
		- test case 6
		```console
		curl --header "Content-Type: application/json" --request POST http://localhost:8081/api/tool-rental/checkout --data '{ "tool_code": "JAKR", "checkout_date": "07/02/20", "rental_days": 4, "discount_percent": 50 }'
		```

# Some notes about design

## Data
I'm using an H2 database as my data layer for simplicity and portability. If this were an app destined for production we'd need something more robust than an in-memory database, but in this case I'm more interested in reducing the overhead required to get this app up and running.
#### Schema definition
I've chosen Flyway as my database migration tool. There are only a couple tables defined right now (`tools` and `tool_types`) but if this app were slated for further development, having a db migration tool configured from the start would make the database easier to manage.  
I'm also using Flway to pre-populate the tables with the test data provided in the requirements.These test_data migrations are always executed after the regular migrations.  
Flyway migrations can be found in [`src/main/resources/db`](https://github.com/jack28drew/jd0823/tree/main/src/main/resources/db). The [`migration`](https://github.com/jack28drew/jd0823/tree/main/src/main/resources/db/migration) directory holds the schema migrations and the [`test_data`](https://github.com/jack28drew/jd0823/tree/main/src/main/resources/db/test_data) directory contains the test data insertion scripts.
#### Persistence layer
I'm using Spring Data JPA in conjunction with Hibernate to manage data. You'll find the entities and repository defined in [`com.tool_rental.rental.tools.*`](https://github.com/jack28drew/jd0823/tree/main/src/main/java/com/tool_rental/rental/tools). You'll notice I only have a repository for `Tool` defined at the moment. That's because there's no need to access the `tool_types` table directly. `Tool` has a foreign key reference to `ToolType` so Hibernate manages the `ToolType` retrieval, and data is loaded by flyway so a repository isn't needed to save `ToolTypes` either.

## Working with money
I decided to use integer cents when working with money internally. Floating point data types aren't great when working with monetary values since they can introduce rounding errors with floating point precision loss. `BigDecimal` is a solid choice as well, but it seems a bit heavyweight for what we're doing here so I chose against it.

## Formatting
The requirements specify specific formats for dates, percentages and monetary values, so I've implemented a few custom formatters and parsers as well as serializers and deserializers for the API layer. These utilities can be found in [`com.tool_rental.utils.*`](https://github.com/jack28drew/jd0823/tree/main/src/main/java/com/tool_rental/utils)

## Date classification
Each date in the rental request needs to be classified as either a holiday, weekday or weekend since different tool types are charged differently based on that categorization. My approach is fairly straightforward: iterate through each date in the rental request, classifying and then sorting into buckets the dates for each of the classifications. The end result is a [`ClassifiedDates`](https://github.com/jack28drew/jd0823/blob/main/src/main/java/com/tool_rental/time/DateClassificationService.java#L43) record containing the dates sorted into sets for each classification. You'll find the date classification logic in [`com.tool_rental.time.DateClassificationService`](https://github.com/jack28drew/jd0823/blob/main/src/main/java/com/tool_rental/time/DateClassificationService.java).
#### Identifying holidays
To identify holidays I created a [`Holiday`](https://github.com/jack28drew/jd0823/blob/main/src/main/java/com/tool_rental/time/Holiday.java) enumerated type. Holidays are created with a month and day in the constructor for fixed date holidays, or if the holiday is not observed on a fixed date, no constructor params. These holidays will instead override the implementation of `Holiday::isObserved()` to define the holiday.
