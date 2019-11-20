CREATE TABLE IF NOT EXISTS product (
  id INTEGER IDENTITY PRIMARY KEY,
  product_id UUID UNIQUE,
  name VARCHAR(100) NOT NULL,
  price DECIMAL NOT NULL,
  version INTEGER NOT NULL
);

CREATE SEQUENCE products_seq;