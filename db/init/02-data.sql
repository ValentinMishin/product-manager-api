TRUNCATE TABLE product, category, rating, users, user_role  RESTART IDENTITY;

insert into users (username, password) values
('admin', '$2y$12$l9nyPaWm917Nt2zxWMU6J.ThdCnQ/RdbGlYotMeg1zc9MNPDHFfAe'),
('client', '$2y$12$fVL.tbFwWIuwde6SAsjgOuNaxFtvff9RGEoCY4ZhANJ1om0.SIq2O');
-- admin : admin
-- client : client
insert into user_role(user_id, role) values
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER');