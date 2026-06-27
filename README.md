# GraphQL Spring Boot Template

A production-ready GraphQL API built with Spring Boot featuring complete CRUD operations, performance optimizations, and enterprise-grade security controls.

## ✨ Features

### Core API
- **Complete CRUD** for Authors and Books with GraphQL mutations and queries
- **Advanced Filtering** with dynamic multi-parameter search
- **Cursor-based Pagination** (Relay spec) with bidirectional navigation
- **Relationship Resolution** with optimized batch loading (N+1 problem solved)
- **Error Handling** with structured error codes and custom exceptions

### Performance
- **Caffeine Caching** with TTL eviction (query docs, individual records, batch loads)
- **DataLoader Integration** for efficient batch fetching of author relationships
- **Parsed Query Caching** to avoid re-parsing repeated queries
- **Optimized Database Queries** with Spring Data JPA and custom @Query methods

### Schema & Design
- **GraphQL Interfaces** (Node pattern) for consistent type definitions
- **Union Types** for polymorphic search results
- **Custom Scalars** (DateTime as ISO-8601)
- **Type Safety** via MapStruct for Entity ↔ DTO conversions

### Security
- **Query Complexity Analysis** prevents expensive/malicious queries (depth limit: 7, complexity: 100)
- **Field-level Cost Calculation** with pagination multipliers
- **Input Validation** at schema and business logic levels
- **Business Rules Enforcement** (e.g., cannot delete authors with books)

### Testing
- **20+ Integration Tests** covering queries, mutations, and error scenarios
- **GraphQL-specific Testing** via GraphQlTester
- **Comprehensive Coverage** of edge cases and validation rules

## 🚀 Quick Start

### Prerequisites
- Java 25+
- Maven 3.6+

### Setup & Run
```bash
# Clone repository
git clone <repo-url>
cd graphql-template

# Build
mvn clean compile

# Run tests
mvn test

# Start application
mvn spring-boot:run
```

### Access GraphQL
- **GraphiQL UI**: http://localhost:8080/graphiql
- **H2 Database**: http://localhost:8080/h2-console

## 📊 Example Queries

### Query All Authors with Pagination
```graphql
query {
  authorsWithPagination(first: 10) {
    edges {
      node {
        id
        firstName
        lastName
        createAt
      }
      cursor
    }
    pageInfo {
      hasNextPage
      startCursor
      endCursor
    }
    totalCount
  }
}
```

### Query Books with Author Details
```graphql
query {
  books {
    id
    name
    pageCount
    createAt
    author {
      firstName
      lastName
    }
  }
}
```

### Create Author
```graphql
mutation {
  createAuthor(author: { firstName: "Robert", lastName: "Martin" }) {
    id
    firstName
    lastName
    createAt
  }
}
```

### Create Book with Author
```graphql
mutation {
  createBook(book: { name: "Clean Code", pageCount: 464, authorId: 1 }) {
    id
    name
    author {
      firstName
    }
  }
}
```

## 🏗️ Architecture

### Layers
```
GraphQL Resolvers (Query/Mutation Mapping)
        ↓
Service Layer (Business Logic, Caching, Validation)
        ↓
Repository Layer (Spring Data JPA)
        ↓
Database Entities (H2 in-memory)
```

### Key Components
| Component | Purpose |
|-----------|---------|
| **Resolvers** | Map GraphQL operations to business logic |
| **Services** | Implement CRUD, caching, validation |
| **Repositories** | Custom database queries with filtering |
| **Mappers** | Entity ↔ DTO conversions (MapStruct) |
| **Entities** | JPA models with relationships and timestamps |

## 🔧 Configuration

### Caching Strategy
Three-tier cache configuration in `CacheConfig.java`:
- **author** (5-min TTL, 1000 max): Individual author lookups
- **authorBatch** (1-min TTL, 200 max): Batch-loaded author relationships
- **book** (2-min TTL, 1000 max): Single book queries

### Complexity Limits
`GraphQLComplexityConfig.java` enforces:
- **Max Depth**: 7 levels of nesting
- **Max Complexity**: 100 units per query
- **Field Costs**: lists (10x), single relations (5x), scalars (1x)

### Custom Scalar
DateTime as ISO-8601 strings with automatic LocalDateTime conversion via `ScalarConfig.java`

## 📦 Tech Stack
- **Spring Boot** 4.0.6
- **Spring GraphQL** + GraphQL Java
- **Spring Data JPA** + Hibernate
- **Caffeine** (in-memory cache)
- **MapStruct** (bean mapping)
- **H2** (in-memory database)
- **Lombok** (boilerplate reduction)
- **JUnit 5** + Mockito (testing)

## 📈 Performance Features

### N+1 Query Resolution
- **DataLoader** batches author fetches when loading multiple books
- Reduces 1000 book queries from 1001 to 2 database calls

### Query Caching
- Parsed GraphQL documents cached (1000 max, 1-hour TTL)
- Eliminates parsing overhead for repeated queries

### Database Optimization
- Cursor pagination prevents full table scans
- Filtered queries use optimized JPA @Query methods
- Batch loading extracts unique IDs before fetching

## 🔒 Security Controls

### Implemented
- ✅ Query depth limiting (prevents infinite nesting)
- ✅ Query complexity analysis (prevents expensive queries)
- ✅ Input validation (schema-level + business rules)
- ✅ Error code standardization (no sensitive information leaked)

## 📚 Project Structure
```
src/main/java/com/graphql/template/
├── config/          # GraphQL, Cache, Complexity, Scalar configs
├── constants/       # Error codes
├── dataloader/      # BatchMapping for author resolution
├── dto/             # Transfer objects
├── entity/          # JPA entities
├── exception/       # Custom exceptions
├── mapper/          # MapStruct converters
├── repository/      # Database queries
├── resolver/        # GraphQL resolvers
├── scalar/          # DateTime scalar
├── service/         # Business logic
└── Utils/           # Cursor utilities
```

## 📖 Learning Resources

- [GraphQL Spec](https://spec.graphql.org/)
- [Spring GraphQL Docs](https://docs.spring.io/spring-graphql/docs/current/reference/html/)
- [Relay Cursor Connections](https://relay.dev/graphql-cursor-connections-spec/)

