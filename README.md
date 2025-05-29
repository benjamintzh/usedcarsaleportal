# Used Car Sale Portal
This project serves as the Summative Assessment for students in the Advanced Web Development Foundations module. It focuses on evaluating the skills necessary to design, develop, implement, and document a Java-based community portal website using the Spring Framework.


## Project Overview
This project is a Java-based Used Car Sales Portal developed using the Spring MVC Framework for the Advanced Web Development module (DSE205/03). The portal is designed for AutoXpress Pvt Ltd to enable users to register, search for cars, post cars for sale, bid, book test drives, and update profiles. Administrators can manage users, approve sales, and handle appointments and bids. The project integrates Thymeleaf templates, a MySQL database, and Spring Security for role-based access control.

## Purpose
The project serves as a summative assessment to evaluate skills in designing, developing, implementing, and documenting a dynamic web application using Spring MVC, MySQL, Thymeleaf, and Eclipse Spring Tool Suite, while adhering to sustainable software practices.

## Prerequistes
- **Development Environment**:
  - Eclipse Spring Tool Suite v4.30 RELEASE
  - Java SE 17
  - MySQL Server
  - phpMyAdmin or MySQL Client
  - Javadoc
  - Microsoft Word (for documentation)
  - Microsoft PowerPoint (for presentations)

- **Dependencies (managed via Maven)**:
  - Spring Web
  - Spring Data JPA
  - Spring Boot DevTools
  - Spring Security
  - Thymeleaf
  - MySQL Connector
  - JavaMail API (for email functionality)
 
  ## Installation
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/<your-username>/used-car-sales-portal.git

2. **Set Up MySQL Database**:
   - Import the database schema from Module 4 into MySQL using phpMyAdmin or a MySQL client.
   - Update the database connection settings in `src/main/resources/application.properties`:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/<database-name>
     spring.datasource.username=<your-username>
     spring.datasource.password=<your-password>
     ```

3. **Configure Email Service**:
   - The application uses Gmail SMTP for sending registration confirmation and bulk emails.
   - Update the email settings in `src/main/resources/application.properties` with your Gmail SMTP credentials:
     ```properties
     spring.mail.host=smtp.gmail.com
     spring.mail.port=587
     spring.mail.username=<your-gmail-address>
     spring.mail.password=<your-gmail-app-password>
     spring.mail.properties.mail.smtp.auth=true
     spring.mail.properties.mail.smtp.starttls.enable=true
     ```
   - **Note**: You must generate an **App Password** in your Google Account settings if 2-Step Verification is enabled. Replace `<your-gmail-app-password>` with this password.

4. **Install Dependencies**:
   - Open the project in Eclipse STS.
   - Ensure Maven is configured to download dependencies listed in `pom.xml`.

5. **Run the Application**:
   - Right-click the project in Eclipse, select **Run As > Spring Boot App**.
   - Access the application at `http://localhost:8080/home`.

## Project Structure
```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.usedcarsales
│   │   │       ├── controller
│   │   │       ├── model  
│   │   │       ├── repository 
│   │   │       ├── service    
│   │   │       |── config       
|   |   |       |── dto
|   |   |       └── validation
│   │   ├── resources
│   │   │   ├── static
|   |   |   |   |── css
|   |   |   |   |── designmaterial
|   |   |   |   └── images
│   │   │   ├── templates        
│   │   │   └── application.properties  
│   └── test
│       └── java
│           └── com.example.usedcarsales
└── pom.xml
```

## Configuration Notes
- **Email Service**: Ensure the Gmail SMTP settings are correctly configured in `application.properties`. Test the email functionality after setup.
- **Static Files**: Verify that the external directory path for static files is accessible and contains the required files.
- **Database**: Confirm that the MySQL database is running and the connection details in `application.properties` are correct.

## Contributing
This is a student project for academic purposes. Contributions are not expected, but feedback from tutors should be incorporated as per the Project Guidelines.

## License
This project is for educational purposes and not distributed under any open-source license.
