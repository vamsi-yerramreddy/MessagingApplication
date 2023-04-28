__Messaging Application with Spring Framework, Cassandra and GitHub Authentication__

This is a messaging application built using Spring Framework, Apache Cassandra as the NoSQL database, and GitHub authentication for user login. The application allows users to send messages to other users on the platform using their GitHub account.

__Technologies Used__

    Spring Boot: Application tier
    Apache Cassandra: Database
    Spring Data Cassandra: Data layer
    Spring Security: User authentication and authorization
    Thymeleaf: View layer for handling UI
    REST and Spring MVC: For building controllers

**Getting Started**
    
    Clone the repository: git clone https://github.com/vamsi-yerramreddy/MessagingApplication.git
    Make sure you have Java 8 and DataStax account for accessing Apache Cassandra database
    Configure your GitHub OAuth application with the following settings:
        Homepage URL: http://localhost:8080
        Authorization callback URL: http://localhost:8080/login/oauth2/code/github
    Refer the application.properties file for further details
    

__Features__

    User authentication and authorization using GitHub OAuth
    Users can send messages to other users on the platform
    Messages are stored in Apache Cassandra for scalability and availability
    Thymeleaf templates for handling UI
__SnapShots__

![Screenshot (68)](https://user-images.githubusercontent.com/59535801/235102281-2210514a-af19-4dbc-a56c-7f3f4701ca58.png)
![Screenshot (69)](https://user-images.githubusercontent.com/59535801/235102292-a7d26426-8ea0-41db-96ba-c945a774d5c2.jpg)
![Screenshot (70)](https://user-images.githubusercontent.com/59535801/235102297-a683b17c-c20d-4321-810e-43383b00408e.png)
![Screenshot (71)](https://user-images.githubusercontent.com/59535801/235102304-3af9ef8e-08da-4f22-a634-ae399a66e0a4.png)
![Screenshot (73)](https://user-images.githubusercontent.com/59535801/235102308-14cc0954-bd92-462a-8407-d39b37556eda.png)
