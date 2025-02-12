# Database Schema Documentation

## Overview
This document provides a detailed explanation of the database schema used in the project. The schema consists of multiple entities that represent users, products, orders, shopping carts, and related functionalities.

---

## Entity Descriptions

### 1. **Users (Пользователи)**
- **UserID** (INT, Primary Key) - Unique identifier for each user.
- **Name** (VARCHAR) - The user's full name.
- **Email** (VARCHAR, Unique) - The user's email address.
- **PhoneNumber** (VARCHAR) - Contact phone number.
- **PasswordHash** (VARCHAR) - Encrypted password.
- **Role** (ENUM: 'Client', 'Administrator') - Defines user permissions.

#### Relationships:
- One-to-Many: A user can have multiple **Carts**, **Orders**, and **Favorites**.

---

### 2. **Cart (Корзина)**
- **CartID** (INT, Primary Key) - Unique identifier for the cart.
- **UserID** (INT, Foreign Key to Users) - The owner of the cart.

#### Relationships:
- One-to-Many: A **Cart** can contain multiple **CartItems**.
- One-to-One: Each **User** has only one active **Cart**.

---

### 3. **CartItems (Товары в корзине)**
- **CartItemID** (INT, Primary Key) - Unique identifier for cart items.
- **CartID** (INT, Foreign Key to Cart) - The associated cart.
- **ProductID** (INT, Foreign Key to Products) - The product added to the cart.
- **Quantity** (INT) - Number of units of the product.

#### Relationships:
- Many-to-One: Multiple **CartItems** belong to one **Cart**.
- Many-to-One: Each **CartItem** references a **Product**.

---

### 4. **Orders (Заказы)**
- **OrderID** (INT, Primary Key) - Unique identifier for the order.
- **UserID** (INT, Foreign Key to Users) - The user who placed the order.
- **CreatedAt** (TIMESTAMP) - Date and time when the order was created.
- **DeliveryAddress** (VARCHAR) - Address where the order is delivered.
- **ContactPhone** (VARCHAR) - Contact phone for delivery.
- **DeliveryMethod** (VARCHAR) - Delivery method selected.
- **Status** (ENUM) - The status of the order (e.g., Pending, Paid, Shipped, Cancelled).
- **UpdatedAt** (TIMESTAMP) - Last update timestamp.

#### Relationships:
- One-to-Many: An **Order** can contain multiple **OrderItems**.
- Many-to-One: An **Order** is linked to a **User**.

---

### 5. **OrderItems (Товары в заказе)**
- **OrderItemID** (INT, Primary Key) - Unique identifier for order items.
- **OrderID** (INT, Foreign Key to Orders) - The associated order.
- **ProductID** (INT, Foreign Key to Products) - The product included in the order.
- **Quantity** (INT) - Number of units purchased.
- **PriceAtPurchase** (DECIMAL) - Price of the product at the time of purchase.

#### Relationships:
- Many-to-One: Each **OrderItem** belongs to an **Order**.
- Many-to-One: Each **OrderItem** references a **Product**.

---

### 6. **Favorites (Избранные товары)**
- **FavoriteID** (INT, Primary Key) - Unique identifier for favorite items.
- **UserID** (INT, Foreign Key to Users) - The user who favorited the product.
- **ProductID** (INT, Foreign Key to Products) - The product marked as favorite.

#### Relationships:
- Many-to-One: Multiple **Favorites** belong to a **User**.
- Many-to-One: Each **Favorite** references a **Product**.

---

### 7. **Categories (Категории товаров)**
- **CategoryID** (INT, Primary Key) - Unique identifier for product categories.
- **Name** (VARCHAR) - Category name.

#### Relationships:
- One-to-Many: A **Category** can contain multiple **Products**.

---

### 8. **Products (Товары)**
- **ProductID** (INT, Primary Key) - Unique identifier for products.
- **Name** (VARCHAR) - Name of the product.
- **Description** (TEXT) - Detailed product description.
- **Price** (DECIMAL) - Product price.
- **CategoryID** (INT, Foreign Key to Categories) - The category of the product.
- **ImageURL** (VARCHAR) - URL of the product image.
- **DiscountPrice** (DECIMAL, Nullable) - Discounted price if applicable.
- **CreatedAt** (TIMESTAMP) - Date and time when the product was added.
- **UpdatedAt** (TIMESTAMP) - Last update timestamp.

#### Relationships:
- Many-to-One: Each **Product** belongs to a **Category**.
- One-to-Many: A **Product** can be part of multiple **CartItems** and **OrderItems**.

---

## Entity Relationship Diagram (ERD)
Below is the visual representation of the database schema.

![Database Schema](Схема таблиц базы данных.png)

---

## Conclusion
This database schema is designed to efficiently manage e-commerce functionalities including user management, product catalog, shopping cart, orders, and favorites. The relationships ensure data consistency and enable smooth interactions between entities.

For any modifications or improvements, consider adding indexes for frequently queried fields and ensuring proper normalization.

