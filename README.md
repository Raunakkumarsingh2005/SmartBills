# 💰 SmartBills

An intelligent billing management system built with Spring Boot that streamlines invoice creation, processing, and management with automated workflows and file upload capabilities.

## 📋 Overview

SmartBills is a comprehensive billing and invoice management application designed to help businesses and individuals efficiently manage their billing operations. The application provides features for creating, tracking, and organizing bills with support for document uploads and automated processing.

## ✨ Features

- **Bill Management**
  - Create, read, update, and delete bills
  - Automated bill numbering and tracking
  - Bill status management (Paid, Pending, Overdue)
  - Search and filter capabilities
  
- **Document Upload**
  - Support for multiple file formats (PDF, images, documents)
  - Secure file storage and retrieval
  - Attachment management for bills
  - File preview and download

- **Customer Management**
  - Customer database with contact information
  - Billing history tracking
  - Customer-specific billing details
  
- **Reporting & Analytics**
  - Financial reports and summaries
  - Payment tracking
  - Outstanding balance calculations
  - Export functionality

- **User Management**
  - Secure authentication and authorization
  - Role-based access control
  - User profile management

## 🛠️ Tech Stack

- **Backend Framework:** Spring Boot 3.x
- **Database:** MySQL / PostgreSQL / H2
- **Security:** Spring Security
- **File Storage:** Local file system / Cloud storage
- **Build Tool:** Maven
- **Java Version:** Java 17+
- **Template Engine:** Thymeleaf (if applicable)

## 📦 Prerequisites

Before running this application, ensure you have:

- Java Development Kit (JDK) 17 or higher
- Maven 3.6+
- MySQL or PostgreSQL (or use H2 for development)
- Git

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/Raunakkumarsingh2005/SmartBills.git
cd SmartBills
```

### 2. Configure Database

Update `src/main/resources/application.properties` with your database credentials:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/smartbills
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
file.upload-dir=./upload
```

### 3. Create Database

```sql
CREATE DATABASE smartbills;
```

### 4. Build the Application

Using Maven wrapper:

```bash
./mvnw clean install
```

Or using Maven directly:

```bash
mvn clean install
```

### 5. Run the Application

Using Maven wrapper:

```bash
./mvnw spring-boot:run
```

Or using Maven directly:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Bill Management Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/bills` | Get all bills | Yes |
| GET | `/api/bills/{id}` | Get specific bill | Yes |
| POST | `/api/bills` | Create new bill | Yes |
| PUT | `/api/bills/{id}` | Update bill | Yes |
| DELETE | `/api/bills/{id}` | Delete bill | Yes |
| GET | `/api/bills/search` | Search bills | Yes |

### Customer Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/customers` | Get all customers | Yes |
| GET | `/api/customers/{id}` | Get customer details | Yes |
| POST | `/api/customers` | Create new customer | Yes |
| PUT | `/api/customers/{id}` | Update customer | Yes |
| DELETE | `/api/customers/{id}` | Delete customer | Yes |

### File Upload Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/upload` | Upload file | Yes |
| GET | `/api/files/{filename}` | Download file | Yes |
| DELETE | `/api/files/{filename}` | Delete file | Yes |

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | User login |
| POST | `/api/auth/logout` | User logout |

## 🏗️ Project Structure

```
SmartBills/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/smartbills/
│   │   │       ├── config/          # Configuration classes
│   │   │       ├── controller/      # REST API controllers
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── entity/          # JPA entities
│   │   │       ├── repository/      # Data access layer
│   │   │       ├── service/         # Business logic
│   │   │       ├── exception/       # Custom exceptions
│   │   │       └── util/            # Utility classes
│   │   └── resources/
│   │       ├── static/              # Static resources
│   │       ├── templates/           # HTML templates
│   │       └── application.properties
│   └── test/                        # Unit and integration tests
├── upload/                          # File upload directory
├── .gitignore
├── pom.xml
└── README.md
```

## 💡 Key Features Explained

### Bill Creation
Create detailed bills with line items, tax calculations, and multiple payment terms. The system automatically generates bill numbers and tracks payment status.

### File Upload System
Securely upload and attach documents (invoices, receipts, contracts) to bills. Files are stored in the `upload/` directory with proper organization and naming conventions.

### Payment Tracking
Monitor payment status for each bill with automatic overdue notifications and payment history tracking.

### Customer Portal
Maintain a comprehensive customer database with billing history, contact information, and outstanding balance tracking.

## 🔒 Security Features

- Password encryption using BCrypt
- JWT-based authentication
- Role-based access control (Admin, User)
- Secure file upload validation
- SQL injection prevention
- XSS protection

## 🧪 Testing

Run the test suite:

```bash
./mvnw test
```

Run with coverage report:

```bash
./mvnw clean test jacoco:report
```

## 📊 Database Schema

### Main Tables

- **bills** - Stores bill information
- **customers** - Customer details
- **bill_items** - Line items for bills
- **payments** - Payment records
- **users** - User authentication data
- **files** - Uploaded file metadata

## 🎨 Frontend (if applicable)

The application includes a web interface built with:
- HTML5
- CSS3 / Bootstrap
- JavaScript
- Thymeleaf templates

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 🐛 Known Issues

- [ ] Large file uploads may take time
- [ ] Date format needs internationalization support

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author

**Raunak Kumar Singh**

- GitHub: [@Raunakkumarsingh2005](https://github.com/Raunakkumarsingh2005)

## 🙏 Acknowledgments

- Spring Boot community for excellent documentation
- All contributors who help improve this project

## 🗺️ Roadmap

- [ ] Email notifications for overdue bills
- [ ] SMS reminders
- [ ] Multi-currency support
- [ ] Recurring bill automation
- [ ] PDF invoice generation
- [ ] Payment gateway integration
- [ ] Mobile application
- [ ] Dashboard analytics with charts
- [ ] Bulk bill operations
- [ ] API rate limiting
- [ ] Multi-tenant support
- [ ] Export to Excel/CSV

## 📞 Support

If you encounter any issues or have questions:
- Open an issue in the GitHub repository
- Contact the maintainer through GitHub

## 🚀 Deployment

### Docker Deployment (Optional)

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/smartbills-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:
```bash
docker build -t smartbills .
docker run -p 8080:8080 smartbills
```

---

⭐ If you find this project useful, please consider giving it a star on GitHub!
