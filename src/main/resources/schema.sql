CREATE TABLE IF NOT EXISTS category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_title VARCHAR(120) NOT NULL,
    price DECIMAL(10, 2),
    product_info TEXT,
    discount DECIMAL(10, 2),
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES category(id)
);