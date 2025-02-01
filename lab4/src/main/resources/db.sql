create database checkers;
create user 'me'@'localhost';
grant all privileges on checkers.* to 'me'@'localhost';
flush privileges;