SET SCHEMA_SEARCH_PATH vending;
INSERT INTO role(role) VALUES ('SELLER');
INSERT INTO role(role) VALUES ('BUYER');

-- Test data
INSERT INTO user (id, deposit, email, password) VALUES (-2, 100, 'steklopod@gmail.com', '$2a$10$/32CV0lMu5pGG9hZP2bp1ujekReChQfGnmUegXRUnB9pSdRuyjolS');
INSERT INTO user (id, deposit, email, password) VALUES (-1, 100, 'd.a.kaltovich@gmail.com', '$2a$10$/32CV0lMu5pGG9hZP2bp1ujekReChQfGnmUegXRUnB9pSdRuyjolS');

INSERT INTO user_roles (user_id, role) VALUES (-1,'SELLER');
INSERT INTO user_roles (user_id, role) VALUES (-2,'BUYER');

INSERT INTO product (id, amount_available, cost, product_name, user_id)VALUES (-1, 100, 125, 'cola', -1);
INSERT INTO product (id, amount_available, cost, product_name, user_id)VALUES (-2, 1, 130, 'pepsi', -1);
INSERT INTO product (id, amount_available, cost, product_name, user_id)VALUES (-3, 0, 100, 'mars', -1);
