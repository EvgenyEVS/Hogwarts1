CREATE TABLE person
(
    person_id bigserial primary key,
    name      varchar(50) not null,
    age       smallint    not null check ( age > 0 ),
    driver    boolean default false
);

CREATE TABLE car
(
    car_id bigserial primary key,
    brand  varchar(30) not null,
    model  varchar(30) not null,
    price  integer     not null check ( price > 0 )
);

CREATE TABLE person_car
(
    person_id bigint not null references person (person_id) on delete cascade,
    car_id    bigint not null references car (car_id) on delete cascade,
    primary key (person_id, car_id)
);