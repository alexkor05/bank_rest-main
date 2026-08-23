

insert into users (firstname, lastname, birth_date, email, password, role)
values
    ('Jonh', 'Smith', '2001-01-02', 'js1@gmail.com', '$2a$10$Ztg08Gh6cZoKqzmFUJI1DOqp4ny3PPuyuJT2SuWkZo/mwDZXYdpdO', 'USER'),
    ('Sam', 'Smith', '1986-01-02', 'js_admin@gmail.com', '$2a$10$/56CNzaiEBA6r1EjUkv/f.vndPyVNlqIgfCLlaQ6uUWq0BaC7xpvO', 'ADMIN'),
    ('Iam', 'Smith', '1999-01-02', 'js_2@gmail.com', '$2a$10$GMadtuFmzVhQsIoADo4v7.5p/4UnB7tOn9kDOyntY4mD0dRdLD3Ca', 'USER'),
    ('Steve', 'Smith', '1999-01-02', 'js5@gmail.com', '$2a$10$EEnsXmn2gDcguHV3Qsgyluwpx4EZq3SSvNrvGYWFmcxtIYBWteN/6', 'USER'),
    ('Steve', 'Smith', '1999-01-02', 'boxal@yandex.ru', '$2a$10$RkWzbw90pMfzPXCbGTATvuwYrGNec4c/xJlaJ/iAhY0ff6GyWMb.6', 'USER');


insert into card (user_id, card_number, expiry_date, status, balance)
values
    (1, '1fc458296ffdb259d75430012720f3c10cbeb4602f461caabf9a2c47b6b67c20c39dac4f3b51d3a93fefc57cf164deaf', '2032-02-01', 'ACTIVE', 100777.00),
    (1, 'ebf9f2a5b2762bea91201da1fa066a507456a7ad02e6f156854babde649070b0a714648d6c7608ddab709b9fb1c72529', '2032-02-05', 'ACTIVE', 5000.00),
    (3, '088896a8e1a9fce12aeeefb0bea4bed8f747abc7a1ce0e997729acd2f915ce255b1960c165b2884a9ec98dc7ab1c0883', '2032-02-01', 'ACTIVE', 10000.00),
    (5, '3bba7ee151118c2b0080e6798753cc602f9ba2069f2066b8fe8477547e66500c96447455d05decb540c967187b9ada6b', '2032-02-01', 'ACTIVE', 7000.00),
    (5, 'db497964e821c88cc1a89359a15ece30370a147f12d8600f5cef59747be5f39c139b5a473821dd83e43433530b84eae4', '2032-02-01', 'ACTIVE', 13000.00);


