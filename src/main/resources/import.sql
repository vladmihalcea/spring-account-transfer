INSERT INTO account_holder (id, first_name, last_name, country) VALUES (1, 'Alice', 'Smith', 'ROMANIA') ON CONFLICT(id) DO NOTHING;

INSERT INTO account_holder (id, first_name, last_name, country) VALUES (2, 'Bob', 'Johnson', 'US') ON CONFLICT(id) DO NOTHING;

INSERT INTO account (iban, balance, holder_id) VALUES ('63a0afea-4299-4dd4-8642-ee28d65ef526', 20, 1) ON CONFLICT(iban) DO UPDATE SET balance=10;

INSERT INTO account (iban, balance, holder_id) VALUES ('c40d2e33-e7c2-4c2a-886f-13d4ed7ff31d', 0, 2) ON CONFLICT(iban) DO UPDATE SET balance=0;
