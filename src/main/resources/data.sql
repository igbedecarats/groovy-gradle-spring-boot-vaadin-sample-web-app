-- Service Categories
INSERT INTO service_category (id, name) VALUES (1, 'Oficios');
INSERT INTO service_category (id, name) VALUES (2, 'Ropa y Moda');
INSERT INTO service_category (id, name) VALUES (3, 'Servicio Técnico');
INSERT INTO service_category (id, name) VALUES (4, 'Limpieza');
INSERT INTO service_category (id, name) VALUES (5, 'Cuidado de Personas');

-- Service SubCategories
INSERT INTO service_sub_category (id, name, category_id) VALUES (1, 'Albañilería', 1);
INSERT INTO service_sub_category (id, name, category_id) VALUES (2, 'Carpintería', 1);
INSERT INTO service_sub_category (id, name, category_id) VALUES (3, 'Cerrajería', 1);
INSERT INTO service_sub_category (id, name, category_id) VALUES (4, 'Herrería', 1);
INSERT INTO service_sub_category (id, name, category_id) VALUES (5, 'Pintor', 1);
INSERT INTO service_sub_category (id, name, category_id) VALUES (6, 'Plomero', 1);
INSERT INTO service_sub_category (id, name, category_id) VALUES (7, 'Gasista', 1);
INSERT INTO service_sub_category (id, name, category_id) VALUES (8, 'Arreglos en General', 2);
INSERT INTO service_sub_category (id, name, category_id) VALUES (9, 'Diseño', 2);
INSERT INTO service_sub_category (id, name, category_id) VALUES (10, 'Lavandería y Tintorería', 2);
INSERT INTO service_sub_category (id, name, category_id) VALUES (11, 'Hogar y Oficina', 3);
INSERT INTO service_sub_category (id, name, category_id) VALUES (12, 'Tecnología', 3);
INSERT INTO service_sub_category (id, name, category_id) VALUES (13, 'Hogares', 4);
INSERT INTO service_sub_category (id, name, category_id) VALUES (14, 'Oficinas', 4);
INSERT INTO service_sub_category (id, name, category_id) VALUES (15, 'Niños', 5);
INSERT INTO service_sub_category (id, name, category_id) VALUES (16, 'Ancianos', 5);

-- Locations
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(1, 'GBA_SUR', '-34.7206336', '-58.2546051', 'Quilmes');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(2, 'GBA_SUR', '-34.7108688', '-58.2800827', 'Bernal');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(3, 'GBA_SUR', '-34.748497', '-58.238205', 'Ezpeleta');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(4, 'GBA_SUR', '-34.6610756', '-58.3669739', 'Avellaneda');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(5, 'GBA_SUR', '-34.7678337', '-58.3792534', 'Temperley');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(6, 'GBA_SUR', '-34.7030869', '-58.2974319', 'Don Bosco');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(7, 'GBA_SUR', '-34.695385', '-58.3320999', 'Villa Dominico');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(8, 'GBA_SUR', '-34.6539459', '-58.3489914', 'Dock Sud');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(9, 'GBA_NORTE', '-34.425087', '-58.5796585', 'Tigre');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(10, 'GBA_NORTE', '-35.0241698', '-58.4237574', 'San Vicente');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(11, 'GBA_NORTE', '-34.470829', '-58.5286102', 'San Isidro');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(12, 'GBA_SUR', '-34.7611823', '-58.4302476', 'Lomas de Zamora');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(13, 'GBA_SUR', '-34.6994795', '-58.3920795', 'Lanus');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(14, 'GBA_SUR', '-34.4938049', '-58.6272715', 'Don Torcuato');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(15, 'GBA_OESTE', '-34.6685004', '-58.7282483', 'Merlo');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(16, 'GBA_OESTE', '-34.6340099', '-58.791382', 'Moreno');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(17, 'GBA_NORTE', '-34.5281205', '-58.473816', 'Vicente Lopez');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(18, 'GBA_NORTE', '-34.47509', '-58.753746', 'Tortuguitas');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(19, 'GBA_SUR', '-34.7900852', '-58.1548511', 'Hudson');
INSERT INTO location
(id, AREA, LATITUDE, LONGITUDE, NAME)
VALUES(20, 'GBA_SUR', '-34.762001', '-58.2112961', 'Berazategui');

-- Users
INSERT INTO user (id, username, password, email, first_name, last_name, preferred_location_id, role)
VALUES (1, 'johnsmith', 'x28', 'john.smith@gmail.com', 'John', 'Smith', 1, 'ADMIN');

INSERT INTO user (id, username, password, email, first_name, last_name, preferred_location_id, role)
VALUES (2, 'johnsnow', 'js', 'john.snow@gmail.com', 'John', 'Snow', 2, 'PROVIDER');

INSERT INTO user (id, username, password, email, first_name, last_name, preferred_location_id, role)
VALUES (3, 'jamesbond', 'jb', 'james.bond@gmail.com', 'James', 'Bond', 2, 'CLIENT');

INSERT INTO user (id, username, password, email, first_name, last_name, preferred_location_id, role)
VALUES (4, 'ironman', 'ironman', 'tony.stark@avengers.com', 'Tony', 'Stark', 7, 'PROVIDER');

INSERT INTO user (id, username, password, email, first_name, last_name, preferred_location_id, role)
VALUES (5, 'hulk', 'hulk', 'bruce.bannerk@avengers.com', 'Bruce', 'Banner', 16, 'PROVIDER');

INSERT INTO user (id, username, password, email, first_name, last_name, preferred_location_id, role)
VALUES (6, 'archers', 'archers', 'archer.sterling@isis.com', 'Archer', 'Stirling', 16, 'PROVIDER');

-- Services
INSERT INTO service (name, description, category_id, sub_category_id, provider_id, location_id, start_time, end_time, start_day, end_day)
VALUES ('Limpieza de Hogares', 'Servicio Integral de Limpieza', 4, 13, 2, 3, '09:00', '12:00', 1, 5);
INSERT INTO service (name, description, category_id, sub_category_id, provider_id, location_id, start_time, end_time, start_day, end_day)
VALUES ('Limpieza de Oficinas', 'Servicio Integral de Limpieza', 4, 14, 2, 1, '13:00', '20:00', 1, 5);

INSERT INTO service (name, description, category_id, sub_category_id, provider_id, location_id, start_time, end_time, start_day, end_day)
VALUES ('Reparación de Electrodomésticos', 'Arreglos de todo tipo', 3, 12, 4, 4, '09:00', '19:00', 1, 4);

INSERT INTO service (name, description, category_id, sub_category_id, provider_id, location_id, start_time, end_time, start_day, end_day)
VALUES ('Trabajos de Construcción', 'Maestro Mayor de Obras', 1, 1, 5, 15, '09:00', '16:00', 1, 4);
INSERT INTO service (name, description, category_id, sub_category_id, provider_id, location_id, start_time, end_time, start_day, end_day)
VALUES ('Pintura de Hogares', 'Con mas de 20 años de experiencia', 1, 5, 5, 16, '09:00', '17:00', 5, 6);

INSERT INTO service (name, description, category_id, sub_category_id, provider_id, location_id, start_time, end_time, start_day, end_day)
VALUES ('Cerrajero a Domicilio', 'Servicio las 24 hrs', 1, 3, 6, 2, '00:00', '23:59', 1, 7);

