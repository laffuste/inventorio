
# Inventorio

## Mission Statement

The Inventory Application should have the features below.
1. An Inventory has name, category, sub-category, quantity information
2. The system supported to create Inventory, update the quantity and browse the Inventory
records
3. There are some validation rules when creating an inventory, eg. a sub-category &quot;Shoe&quot;
should not in category &quot;Food&quot;, or sub-category &quot;Cake&quot; should not in category &quot;Clothes&quot;.
4. Additional features supported which not mention in above will be treated as bonus


## Design Choices

- Domain Driven Design: 
    - in a small project like this, usage of DDD or Layered design does not have much impact and is a matter of personal preference. However, DDD is often more scalable as domains multiply.
    - in this project, `CategoryManagementService` and `CategoryServiceImpl` have been artifically separated for demonstration purposes due to the small scale of the project. In real projects, probably these services would be merged and other domain services would carry the real complexity.
- Migrations: database changes should always be versioned and programmatically applied
- All models are immutable (except `@Entity` and `SearchProductRequest` because of tech limitations)
- UI through swagger
- TDD:
    * Unit tests: test son services, make up the majority of tests. Tests are as simple and as isolated as possible.
    * Jpa tests (Integration): only for custom repository methods. Methods provided by `spring-data-jpa` are already tested by them.
    * Component tests: integration tests running the whole component with conditions as close as possible to real servers.  
    * No Controller tests: when using a thin controller pattern, controller tests are superflous when also using Component tests 

## Features Choices

- Categories follow a tree (each category has an optional parent category). Products are attached to a single category. This solves the problem of a product being in inconsistent category and subcategory. It adds the flexibility to have multiple levels of categories as most of shops have.
- Dynamic Query: added search filters to the "browsing" functionality. Once you pay the overhead of using dynamic query, adding new filters is extremely cheap. Added an "isAvailble" filter as example.


## Technology Choices

- java8: industry standard. I tried with 11 as a test but some libraries (querydsl, ) would misbehave when building, and I cound't find the time to dig into it (java, idea, maven plugins?).  
- maven: industry standard. Gradle could have been another acceptable (and slighlty more performant) choice but it would have involved a small learnign curve and some uncertainty. 
- spring boot: industry standard, as of today the most productive and safe way to develop in java.
- h2: simplicity for a POC and to facilitate the audience's execution. Using postgres would have been the next choice, but would also have needed some init script or at least a Dockerfile. 

## Library choices

- `mapstruct` (mapper): needs a processor (in maven). Has good lombok integration (specially for immutables), works out of the box for collections. More features and customisable and friendly than Dozer or ModelMapper.
- `querydsl`: dynamic query, an improvement over Specifications
- `lombok`: avoid lots of boilerplate
- `assertj`: fluent and much more feature rich and collections friendly than `hamcrest` or `junit` assertions
- `junit`: more standard and more broad suport than `ngTest`. This last one uses one instance per class by default, which I believe is an antipattern (carries state).
- `flyway`: simpler to set up than `liquibase`. `flyway` works with raw SQL (more flexible for db specific features) amd it's incredibly rare to change underlying databases.  
- `karate`: a favorite for Component testing at API level, built on top of `cucumber`. Alternatives are `Cucumber` or plain `restTemplate` or `MockMvc`, buy these choices have the following drawbacks:    
    - it needs boilerplate to make requests/responses. Worse in the case of Cucumber, where you need to define your steps, which are usually not obvious to someone reading them (and having to dive into the code to understand exaclty what happens)
    - the input of the API is http and the output of json. The testing framework should test exactly this, so to not have surprises. Using these solutions usually test against Java objects, which might hide some info (f.e. the exact date format in a LocalDateTime/Date object). Tests are better when they are dumb: input this, expect that (no Jackson or other serialisers involved).
    - there is no involvement of Spring. The applications starts embedded and gets hit by real http requests. If the application talsk with other services, "feature" (wiremock) servers are used to simulate dependencies, and real http calls hit those mock servers. Everything is real, nothing is mocked (besides external dependencies). 

## Getting started

:warning: Requirement: java 8 or later.

1. Run:
    ```bash
    $ ./mvnw spring-boot:run
    ```

1. Open: <http://localhost:8080/>


## Limitations

- :warning: updating models is NOT thread safe. Category and Product should have optimistic locking. New endpoints to update product quantities `PUT v1/products/{id}/add` and `PUT v1/products/{id}/remove` should be added with pessimistic locking.
- H2 as a database, not persistent, only for demo purposes
- `querydsl`: need an explicit compilation (or run maven `apt:process`) after a change on entities.
- 400 Bad Requests don't show messages (by default). Needs a `@ControllerAdvice` with better formatted error responses (default bean validation messages are very ugly in spring).
- karate should have these features (but their implementation takes time):
    - parallel runs
    - per controller Runners
    - cucumber reports
    - utilities to interact with database (add fixtures, check state)
    - start in a random port (so to avoid port collision in CI)        


### Next features

- Use of Real database + Docker
- Searching products in a category could include products the category's subcategories (f.e. param `includeSubcategories=true`)
- Page views (count how many times the GET endpoint gets hit) and order by "hot"
- Cache product descriptions and categories (hotspots) instead of fetching from db
- Update quantity with database lock


## Notes

- requires `lombok` plugin and annotation processsing in idea
- recommended Idea plugins:
    - mapstruct support