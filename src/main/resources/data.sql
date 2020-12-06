insert into user(username, password, fullname, department, roles)
values('admin', '{bcrypt}$2a$10$U.WOJo.QlM34tWREOLAtjumjGdNQVBu765hJgTYK9xE0wWs1HRpyi', 'Administrator', 'main', 'ROLE_ADMIN');
insert into user(username, password, fullname, department, roles)
values('user', '{bcrypt}$2a$10$U.WOJo.QlM34tWREOLAtjumjGdNQVBu765hJgTYK9xE0wWs1HRpyi', 'Simple user', 'main', 'ROLE_USER');
insert into user(username, password, fullname, department, roles)
values('superuser', '{bcrypt}$2a$10$U.WOJo.QlM34tWREOLAtjumjGdNQVBu765hJgTYK9xE0wWs1HRpyi', 'Super user', 'main', 'ROLE_SUPERUSER');
