create table rating (
    id bigint generated always as identity primary key,
    rate double precision not null check (rate >= 0 AND rate <= 10),
    count integer not null check (count >= 0)
);

create table category (
	id bigint generated always as identity primary key,
	title varchar(50) not null unique
);

create table product (
	id bigint generated always as identity primary key,
	external_id bigint unique,
	title varchar(50) not null,
	price decimal(12,2) not null,
	description varchar(255) not null,
	rating_id bigint not null unique,
	category_id bigint not null,
	foreign key (rating_id) references rating(id) on delete cascade,
	foreign key (category_id) references category(id) on delete restrict
);

comment on column product.external_id is 'идентификатор товара fakestoreapi.com';

create table users (
	id bigint generated always as identity primary key,
	username varchar(50) not null unique,
	password varchar(100) not null
);

create table user_role (
	user_id bigint not null,
	role varchar(50) not null,
	foreign key (user_id) references users(id)
);

-- поиск по external_id
create index idx_products_external_id on product(external_id);
-- по категориям
create index idx_products_category_id on product(category_id);