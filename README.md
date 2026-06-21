# Sahara E-Commerce API

### Languages and Tools

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Hibernate](https://img.shields.io/badge/hibernate-%2359666C.svg?style=for-the-badge&logo=hibernate&logoColor=white)
![Apache Maven](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Stripe](https://img.shields.io/badge/Stripe-626CD9?style=for-the-badge&logo=Stripe&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)

A full-stack e-commerce REST API powering **Sahara**, a fictional storefront. Built as a Year Up United LTCA capstone project with JWT auth, role-based access, shopping cart, order management, and Stripe sandbox payments.

> Payments are processed in Stripe sandbox/test mode for obvious reasons lol

<img width="2032" height="1162" alt="Screenshot 2026-06-21 at 4 08 33 AM" src="https://github.com/user-attachments/assets/ea8cd977-f30e-4850-8f6e-8c2ed9e75550" />

**API:** https://sahara-backend-api-capstone.onrender.com
**Check out the frontend repo!** [sahara-frontend](https://github.com/yu26s9-decypted/sahara-capstone3-angular-frontend)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 26 |
| Framework | Spring Boot 4 |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL 8 (AWS RDS) |
| Image Hosting | AWS S3 |
| Payments | Stripe (sandbox) |
| Build Tool | Maven |
| Deployment | Docker + Render |

## AWS Infrastructure

I wanted to get hands-on experience with cloud deployment beyond just the application layer since on my previous project (Andara dealership) I used Aiven for the managed MySQL instance which made things really easy. This time I went with AWS RDS to see how they compared honestly not much difference in the end, but Aiven was way simpler to navigate. AWS had a lot more buttons and options to wade through before getting it running.

For S3 I didn't want to put images into my frontend or store filenames in the database pointing nowhere. So I chose S3 to gives a proper home for the assets. The database stores the full URL, and the frontend just renders it.

**RDS**: MySQL instance on AWS RDS (`us-east-2`).

**S3**: Product images stored in `amzn-s3-generalbucket/SaharaProductItem/` served via public object URLs referenced in the `image_url` field.

## Getting Started

### Prerequisites
- Java 26+, Maven, MySQL 8+

### Setup

```bash
git clone https://github.com/yu26s9-decypted/sahara-backend-api-capstone.git
cd sahara-backend-api-capstone
```

Set environment variables:

| Variable | Description      |
|---|------------------|
| `DB_USERNAME` | MySQL username   |
| `DB_PASSWORD` | MySQL password   |
| `STRIPE_SECRET_KEY` |Stripe secret key |

```bash
./mvnw spring-boot:run
```

API starts on `http://localhost:8080`.

## API Endpoints

### Auth
| Method | Endpoint | Auth |
|---|---|---|
| `POST` | `/register` | None |
| `POST` | `/login` | None |

### Categories
| Method | Endpoint | Auth |
|---|---|---|
| `GET` | `/categories` | None |
| `GET` | `/categories/{id}` | None |
| `GET` | `/categories/{id}/products` | None |
| `POST` | `/categories` | Admin |
| `PUT` | `/categories/{id}` | Admin |
| `DELETE` | `/categories/{id}` | Admin |

### Products
| Method | Endpoint | Auth |
|---|---|---|
| `GET` | `/products` | None |
| `GET` | `/products/{id}` | None |
| `POST` | `/products` | Admin |
| `PUT` | `/products/{id}` | Admin |
| `DELETE` | `/products/{id}` | Admin |

Query params: `cat`, `minPrice`, `maxPrice`, `subCategory`, `name`

### Shopping Cart
| Method | Endpoint | Auth |
|---|---|---|
| `GET` | `/cart` | User |
| `POST` | `/cart/products/{productId}` | User |
| `PUT` | `/cart/products/{productId}` | User |
| `DELETE` | `/cart/products/{productId}` | User |
| `DELETE` | `/cart` | User |

### Orders
| Method | Endpoint | Auth |
|---|---|---|
| `POST` | `/orders` | User |

### Profile
| Method | Endpoint | Auth |
|---|---|---|
| `GET` | `/profile` | User |
| `PUT` | `/profile` | User |

### Payments
| Method | Endpoint | Auth |
|---|---|---|
| `POST` | `/payment/create-intent` | User |

## Authentication

Login to receive a JWT token, then include it in the `Authorization` header:

```
Authorization: Bearer <your_token>
```

---

## Developer

**Andy Tang**
- [andytang.tech](https://andytang.tech)
- [@decypted](https://github.com/yu26s9-decypted)

Do whatever you want with it to learn like I did! Had a ton of fun and learned alot building this.
