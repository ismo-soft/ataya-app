# Microservices E-Commerce Platform

A comprehensive microservices-based e-commerce platform built with Spring Boot, MongoDB, and Apache Kafka for scalable business operations.

## 🏗️ Architecture Overview

This project follows a microservices architecture pattern with the following key components:

- **Company Service** - Manages company, store, product, and worker information
- **Address Service** - Handles address management and location data
- **Inventory Service** - Manages stock levels and inventory operations
- **Contributor Service** - Handles contributor data and related operations
- **Discovery Service** - Eureka service registry for service discovery
- **Gateway Service** - API Gateway for routing and load balancing

## 🛠️ Technologies Used

### Backend Technologies
- **Java 17+** - Programming language
- **Spring Boot** - Framework for building microservices
- **Spring Cloud** - Microservices toolkit
- **Spring Security** - Authentication and authorization
- **Spring Data MongoDB** - Data persistence layer

### Infrastructure & DevOps
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration
- **MongoDB** - NoSQL database
- **Apache Kafka** - Event streaming platform
- **Apache Zookeeper** - Kafka coordination service
- **Eureka Server** - Service discovery
- **Mongo Express** - MongoDB web admin interface

### Service Communication
- **RESTful APIs** - HTTP-based communication
- **Apache Kafka** - Asynchronous event-driven communication
- **Eureka Client** - Service registration and discovery

## 📋 Prerequisites

Before running this project, make sure you have the following installed:

- **Docker** (version 20.0 or higher)
- **Docker Compose** (version 2.0 or higher)
- **Git** (for cloning the repository)
- At least **4GB RAM** available for containers

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd <repository-name>
```

### 2. Run the Application

Start all services using Docker Compose:

```bash
docker-compose up -d
```

This command will:
- Pull all required Docker images
- Start MongoDB with admin credentials
- Launch Kafka and Zookeeper
- Initialize Kafka topics
- Start all microservices
- Set up service discovery and API gateway

### 3. Verify Services

Check if all services are running:

```bash
docker-compose ps
```

## 📡 Service Endpoints

| Service | Port | Health Check | Description |
|---------|------|--------------|-------------|
| **API Gateway** | 8080 | `http://localhost:8080/actuator/health` | Main entry point |
| **Discovery Service** | 8761 | `http://localhost:8761` | Eureka Dashboard |
| **Company Service** | 8091 | `http://localhost:8091/actuator/health` | Company management |
| **Address Service** | 8090 | `http://localhost:8090/actuator/health` | Address management |
| **Inventory Service** | 8092 | `http://localhost:8092/actuator/health` | Inventory management |
| **Contributor Service** | 8093 | `http://localhost:8093/actuator/health` | Contributor management |
| **MongoDB** | 27017 | - | Database |
| **Mongo Express** | 8081 | `http://localhost:8081` | DB Admin Interface |
| **Kafka** | 9092 | - | Message broker |

## 🗃️ Database Configuration

### MongoDB Setup
- **Host**: localhost:27017
- **Username**: admin
- **Password**: admin
- **Database**: user_db

### Mongo Express (Web UI)
- **URL**: http://localhost:8081
- **Username**: admin
- **Password**: admin

## 📨 Kafka Topics

The following Kafka topics are automatically created:

- `suspend-item-from-inventory` - Inventory suspension events
- `cart-item-statistics` - Shopping cart analytics

## 🔧 Development Setup

### Environment Variables

Key environment variables used in the services:

```env
# Eureka Configuration
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://discovery:8761/eureka/

# Kafka Configuration
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092

# MongoDB Configuration
SPRING_DATA_MONGODB_HOST=mongo
SPRING_DATA_MONGODB_PORT=27017
SPRING_DATA_MONGODB_DATABASE=user_db
SPRING_DATA_MONGODB_USERNAME=admin
SPRING_DATA_MONGODB_PASSWORD=admin
```

### Building Services Locally

If you want to build the services from source:

```bash
# Build all services
./mvnw clean package -DskipTests

# Build Docker images
docker build -t company-service ./company-service
docker build -t address-service ./address-service
# ... repeat for other services
```

## 📊 Monitoring and Management

### Service Discovery
Access Eureka Dashboard at: http://localhost:8761

### Database Management
Access Mongo Express at: http://localhost:8081

### Health Checks
Each service provides health check endpoints:
- `http://localhost:<port>/actuator/health`
- `http://localhost:<port>/actuator/info`

## 🐛 Troubleshooting

### Common Issues

1. **Port Conflicts**
   ```bash
   # Check if ports are in use
   netstat -tulpn | grep :8080
   
   # Stop conflicting services
   sudo lsof -ti:8080 | xargs kill -9
   ```

2. **Memory Issues**
   ```bash
   # Check Docker memory usage
   docker stats
   
   # Increase Docker memory limit to at least 4GB
   ```

3. **Service Not Starting**
   ```bash
   # Check service logs
   docker-compose logs <service-name>
   
   # Restart specific service
   docker-compose restart <service-name>
   ```

### Logs

View logs for specific services:

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f company

# Last 100 lines
docker-compose logs --tail=100 inventory
```

## 🧪 Testing

### API Testing

Use the following base URLs for testing:

```bash
# Through API Gateway
curl http://localhost:8080/api/company/health

# Direct service access
curl http://localhost:8091/actuator/health
```

### Load Testing

For load testing, consider using:
- **Apache JMeter**
- **Postman** (for API collections)
- **curl** (for simple requests)

## 🔄 Shutdown

To stop all services:

```bash
# Graceful shutdown
docker-compose down

# Remove volumes (WARNING: This will delete all data)
docker-compose down -v

# Remove images
docker-compose down --rmi all
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions:
- Create an issue in the GitHub repository
- Check the troubleshooting section above
- Review service logs for error details

## 🔮 Roadmap

- [ ] Add authentication service
- [ ] Implement caching layer (Redis)
- [ ] Add monitoring (Prometheus + Grafana)
- [ ] Implement distributed tracing
- [ ] Add CI/CD pipeline
- [ ] API documentation with Swagger
- [ ] Performance optimization
- [ ] Security enhancements