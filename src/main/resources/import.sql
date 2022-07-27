INSERT INTO account (iban, balance, owner) VALUES ('63a0afea-4299-4dd4-8642-ee28d65ef526', 10, 'Alice') ON CONFLICT(iban) DO UPDATE SET balance=10;

INSERT INTO account (iban, balance, owner) VALUES ('c40d2e33-e7c2-4c2a-886f-13d4ed7ff31d', 0, 'Bob') ON CONFLICT(iban) DO UPDATE SET balance=0;
