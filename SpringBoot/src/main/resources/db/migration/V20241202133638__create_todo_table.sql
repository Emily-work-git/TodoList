create table if not exists todo (
    id int auto_increment primary key,
    text varchar(255) null,
    done boolean null
);
INSERT INTO todo (text, done) values ('item1', false);
INSERT INTO todo (text, done) values ('item2', false);
INSERT INTO todo (text, done) values ('item3', false);
INSERT INTO todo (text, done) values ('item4', false);

