--liquibase formatted sql
--changeset {Afler}:{insertLocations}

INSERT INTO city (id, name)
VALUES
    (1, 'Самара'),
    (2, 'Москва'),
    (3, 'Санкт-Петербург');

INSERT INTO location (id, name, category, latitude, longitude, rating, rating_num, city_id)
VALUES
    (1, 'Location 1', 'HISTORY', '53.247950', '50.265060', '0', '0', 1),
    (2, 'Парк Гагарина', 'NATURE', '53.230383', '50.206045', '0', '0', 1),
    (3, 'Ладья', 'ARCHITECTURE', '53.215937', '50.132186', '0', '0', 1),
    (4, 'Монумент славы', 'ARCHITECTURE', '53.203812', '50.109916', '0', '0', 1),
    (5, 'ЖД вокзал', 'ARCHITECTURE', '53.185844', '50.121749', '0', '0', 1)

