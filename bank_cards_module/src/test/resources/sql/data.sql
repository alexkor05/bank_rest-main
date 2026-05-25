-- insert into users(firstname, lastname, birth_date, email, password, role)
-- VALUES ('Alex', 'Kor', '1986-03-03', 'alex@gmail.com', '$2a$10$M53KUV7.zz0GxLW7NtH4c.aGdDb./8/tecIDqRO6xwoGNOiQeZyZm', 'ADMIN'),
--        ('Dima', 'Kor', '1987-09-13', 'dima@gmail.com', '$2a$10$M53KUV7.zz0GxLW7NtH4c.aGdDb./8/tecIDqRO6xwoGNOiQeZyZm', 'USER'),
--        ('Anna', 'Kuznetsova', '2001-04-03', 'anna@gmail.com', '$2a$10$M53KUV7.zz0GxLW7NtH4c.aGdDb./8/tecIDqRO6xwoGNOiQeZyZm', 'USER');

-- insert into card(user_id, card_number, expired_date, status, balance)
-- VALUES (1, '1234567812345678', '2029-03-03', 'ACTIVE', 120000),
--        (2, '1234123412341234', '2030-05-05', 'ACTIVE', 120000),
--        (1, '1111111122222222', '2029-06-06', 'ACTIVE', 120000)


insert into users(firstname, lastname, birth_date, email, password, role)
VALUES ('Jonh', 'Smith', '2001-01-02', 'js1@gmail.com', '$2a$10$Ztg08Gh6cZoKqzmFUJI1DOqp4ny3PPuyuJT2SuWkZo/mwDZXYdpdO', 'USER');

insert into card(user_id, card_number, expiry_date, status, balance)
VALUES (1, '1fc458296ffdb259d75430012720f3c10cbeb4602f461caabf9a2c47b6b67c20c39dac4f3b51d3a93fefc57cf164deaf', '2032-02-01', 'ACTIVE', 100777.00),
       (1, 'ebf9f2a5b2762bea91201da1fa066a507456a7ad02e6f156854babde649070b0a714648d6c7608ddab709b9fb1c72529', '2032-02-05', 'ACTIVE', 5000.00);




