This project is a personal web development project built entirely on the Java ecosystem, leveraging modern frameworks and tools to deliver a robust, secure, and user-friendly web application.
Technologies Used

1.Spring Boot
Provides the backbone of the application with embedded Tomcat.
Simplifies configuration and accelerates development with auto-configuration and starter dependencies.
2.Spring Data JPA (Hibernate)
Handles all data persistence operations.
Provides a clean abstraction over database interactions.
Entities are mapped directly to tables in MySQL, ensuring efficient CRUD operations.
3.Spring Security
Implements authentication and authorization for the application.
Includes login/logout functionality, role-based access control (e.g., ROLE_ADMIN, ROLE_USER).
JWT (JSON Web Token) or session-based security can be used depending on the use case.
4.Thymeleaf
Server-side Java template engine for rendering dynamic web pages.
Integrates seamlessly with Spring MVC controllers.
Used to build user-friendly forms, dashboards, and error handling views.
5.Bootstrap
Provides responsive front-end design.
Ensures the application is mobile-friendly and visually appealing.
Combined with Thymeleaf for form validation and layout.
6.MySQL
Relational database for storing user accounts, products, orders, or other business data.
Integrated via Spring Data JPA with connection pooling and schema auto-generation.


