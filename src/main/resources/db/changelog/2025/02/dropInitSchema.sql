--liquibase formatted sql
--changeset {Afler}:{clearLocations}

DROP TABLE IF EXISTS public.city CASCADE;
DROP TABLE IF EXISTS public.app_users CASCADE;
DROP TABLE IF EXISTS public.comment CASCADE;
DROP TABLE IF EXISTS public.location CASCADE;
DROP TABLE IF EXISTS public.user_authorities CASCADE;
DROP SEQUENCE IF EXISTS public.location_seq;
DROP SEQUENCE IF EXISTS public.app_users_seq;
DROP SEQUENCE IF EXISTS public.comment_seq;
DROP SEQUENCE IF EXISTS public.city_seq;
