# Menu Service - ByteBites Backend Services

Business microservice managing the food catalog, categories, pricing, and availability. It handles product listings and integrates with Google Cloud Storage to serve menu item images.

## 👨‍🎓 Student Information
* **Student Name:** Anjana Heshan
* **Student ID:** 241722056
* **Module:** ITS 2130 - Enterprise Cloud Architecture (ECA)
* **GCP Project ID:** intense-slice-505613-d3

## 📝 Service Description
The **Menu Service** is responsible for managing all aspects of menu availability and composition:
* **Menu Operations:** Adding, editing, updating, and fetching food menu items and categories.
* **Database Dual Compatibility:** 
  * Local development uses a document-oriented **MongoDB** database.
  * Production cloud deployment uses **Google Cloud Datastore** (NoSQL Datastore Native mode).
* **Cloud Storage Bucket Integration:** Interacts with Google Cloud Storage (`gs://bytebites-media-bucket-intense-slice-505613-d3`) to upload, manage, and distribute high-quality food images.

## 🛠️ Technology Stack
* **Language/Platform:** Java 17
* **Framework:** Spring Boot 3.2.5, Spring Cloud Client (Netflix Eureka client, Config Client)
* **Local Database:** MongoDB (running on port `27017` via Docker Compose)
* **Cloud Database:** Google Cloud Datastore
* **Cloud Storage:** Google Cloud Storage Buckets
* **Default Port:** `8081`

## 🚀 Getting Started & Local Setup

### Prerequisites
* JDK 17 installed
* Maven installed and configured
* Docker and Docker Compose running (for MongoDB container)
* Central platform services (`config-server` and `eureka-server`) must be active first.

### Local Execution Instructions
1. Ensure the local databases are running. From the root directory of backend services, start Docker Compose:
   ```bash
   docker compose up -d
   ```
2. Navigate to the `menu-service` directory:
   ```bash
   cd menu-service
   ```
3. Run the application using the Maven Wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Verify the service is registered with Eureka by checking the dashboard (`http://localhost:8761`) or testing the Menu Service health/REST endpoints:
   ```bash
   curl http://localhost:8081/api/menu
   ```
