DROP SCHEMA IF EXISTS public CASCADE;

CREATE SCHEMA public AUTHORIZATION pg_database_owner;

COMMENT ON SCHEMA public IS 'standard public schema';

CREATE TYPE public."account_status_type" AS ENUM (
	'DISABLED',
	'ENABLED',
	'SUSPENDED');

CREATE CAST (varchar as account_status_type) WITH INOUT AS IMPLICIT;

CREATE TYPE public."debt_status_type" AS ENUM (
	'PENDING',
	'CLEARED'
);

CREATE CAST (varchar as debt_status_type) WITH INOUT AS IMPLICIT;

CREATE TYPE public."customer_gender_type" AS ENUM (
	'MALE',
	'FEMALE'
);

CREATE CAST (varchar as customer_gender_type) WITH INOUT AS IMPLICIT;

CREATE TYPE public."customer_role_type" AS ENUM (
	'CUSTOMER',
	'ADMIN'
);

CREATE CAST (varchar as customer_role_type) WITH INOUT AS IMPLICIT;

CREATE TABLE public.customers (
	id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
	email varchar(80) NOT NULL,
	password_hash varchar(60) NOT NULL,
	"role" public."customer_role_type" DEFAULT 'CUSTOMER'::customer_role_type NOT NULL,
	account_status public."account_status_type" DEFAULT 'ENABLED'::account_status_type NOT NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT customers_email_key UNIQUE (email),
	CONSTRAINT customers_pkey PRIMARY KEY (id)
);

CREATE TABLE public.customer_profiles (
	id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
	customer_id int4 NOT NULL,
	"name" varchar(20) NOT NULL,
	surname varchar(40) NOT NULL,
	phone varchar(14) NOT NULL,
	birthdate date NOT NULL,
	gender public."customer_gender_type" NOT NULL,
	photo_path varchar(100) NULL, -- path to image
	updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT profiles_pkey PRIMARY KEY (id),
	CONSTRAINT profiles_customer_id_key UNIQUE (customer_id),
	CONSTRAINT profiles_customer_id_fkey FOREIGN KEY (customer_id) REFERENCES public.customers(id) ON DELETE CASCADE
);

CREATE TABLE public.activities (
	id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
	name varchar(80) NOT NULL,
	created_by int4 NOT NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT activity_pkey PRIMARY KEY (id),
    CONSTRAINT activity_created_by_fkey FOREIGN KEY (created_by) REFERENCES public.customers(id) ON DELETE CASCADE
);

CREATE TABLE public.activities_members (
	id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
	activity_id int4 NOT NULL,
	customer_id int4 NOT NULL,
	added_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT activity_members_pkey PRIMARY KEY (id),
	CONSTRAINT customer_id_unique UNIQUE (customer_id),
	CONSTRAINT activity_id_fkey FOREIGN KEY (activity_id) REFERENCES public.activities(id) ON DELETE CASCADE,
	CONSTRAINT customer_id_fkey FOREIGN KEY (customer_id) REFERENCES public.customers(id) ON DELETE CASCADE
);

CREATE TABLE public.activities_members_payments (
	id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
	from_customer_id int4 NOT NULL,
	to_customer_id int4 NULL,
	description text NOT NULL,
	amount numeric(15, 2) NOT NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	updated_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT customers_debts_pkey PRIMARY KEY (id),
	CONSTRAINT customers_debts_from_customer_id_fkey FOREIGN KEY (from_customer_id) REFERENCES public.customers(id) ON DELETE CASCADE,
	CONSTRAINT customers_debts_to_customer_idfkey FOREIGN KEY (to_customer_id) REFERENCES public.customers(id) ON DELETE CASCADE
);