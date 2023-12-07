--crear base de datos
create database ucb_sisacademico;
--para crear el contenedor de docker
--docker run -p 5432:5432 --name postgres-ucb -e POSTGRES_PASSWORD=password -d postgres

--Datos 
--colegios
INSERT INTO colegio (colegio_id, direccion, estado, nombre_colegio, tipo) VALUES
                                                                              (1, 'Av. Arce', true, 'San Calixto', 'Particular'),
                                                                              (2, 'Calle 7 de Mayo', true, 'Colegio Alemán', 'Particular'),
                                                                              (3, 'Av. Las Américas', true, 'Colegio América', 'Particular'),
                                                                              (4, 'Calle Sucre', true, 'Colegio Sucre', 'Particular'),
                                                                              (5, 'Av. Bolívar', true, 'Colegio Bolívar', 'Público'),
                                                                              (6, 'Calle Junín', true, 'Colegio Junín', 'Público'),
                                                                              (7, 'Av. Libertad', true, 'Colegio Libertad', 'Público'),
                                                                              (8, 'Calle Murillo', true, 'Colegio Murillo', 'Público'),
                                                                              (9, 'Av. Washington', true, 'Colegio Washington', 'Particular'),
                                                                              (10, 'Calle 6 de Agosto', true, 'Colegio 6 de Agosto', 'Particular'),
                                                                              (11, 'Av. Mariscal Santa Cruz', true, 'Colegio Santa Cruz', 'Público'),
                                                                              (12, 'Calle Illampu', true, 'Colegio Illampu', 'Público'),
                                                                              (13, 'Av. Montes', true, 'Colegio Montes', 'Particular'),
                                                                              (14, 'Calle Campero', true, 'Colegio Campero', 'Particular'),
                                                                              (15, 'Av. Ballivián', true, 'Colegio Ballivián', 'Público');--colegio

--carreras_departamentos
INSERT INTO departamento_carrera (departamento_carrera_id, carrera, estado, nombre, programa, sigla) VALUES
(1, true, true, 'Ingeniería de Sistemas', 'Ingeniería de Sistemas', 'IS'),
(2, true, true, 'Ciencias de la Computación', 'Ciencias de la Computación', 'CC'),
(3, true, true, 'Ingeniería Eléctrica', 'Ingeniería Eléctrica', 'IE'),
(4, false, true, 'Departamento de Matemáticas', 'Matemáticas', 'MAT'),
(5, false, true, 'Departamento de Física', 'Física', 'FIS'),
(6, false, true, 'Departamento de Biología', 'Biología', 'BIO'),
(7, false, true, 'Departamento de Química', 'Química', 'QUI'),
(8, false, true, 'Departamento de Historia', 'Historia', 'HIS'),
(9, false, true, 'Departamento de Economía', 'Economía', 'ECO'),
(10, false, true, 'Departamento de Lenguas Modernas', 'Lenguas Modernas', 'LENG'),
(11, false, true, 'Departamento de Arte', 'Arte', 'ARTE'),
(12, false, true, 'Departamento de Psicología', 'Psicología', 'PSI'),
(13, true, true, 'Medicina', 'Medicina', 'MED'),
(14, false, true, 'Departamento de Derecho', 'Derecho', 'DER'),
(15, false, true, 'Departamento de Arquitectura', 'Arquitectura', 'ARQ'),
(16, true, true, 'Ingeniería Civil', 'Ingeniería Civil', 'IC'),
(17, true, true, 'Medicina Veterinaria', 'Medicina Veterinaria', 'MV'),
(18, true, true, 'Arquitectura de Interiores', 'Arquitectura de Interiores', 'AI'),
(19, true, true, 'Ciencias Políticas', 'Ciencias Políticas', 'CP'),
(20, true, true, 'Diseño Gráfico', 'Diseño Gráfico', 'DG'),
(21, true, true, 'Economía Internacional', 'Economía Internacional', 'EI'),
(22, true, true, 'Psicología Clínica', 'Psicología Clínica', 'PC'),
(23, true, true, 'Derecho Internacional', 'Derecho Internacional', 'DI'),
(24, true, true, 'Biomedicina', 'Biomedicina', 'BM'),
(25, true, true, 'Ciencias Ambientales', 'Ciencias Ambientales', 'CA');


--profesion
INSERT INTO profesion (profesion_id, nombre_profesion, estado) VALUES
                                                                   (1, 'Médico', true),
                                                                   (2, 'Ingeniero de Software', true),
                                                                   (3, 'Profesor de Matemáticas', true),
                                                                   (4, 'Abogado', true),
                                                                   (5, 'Enfermera', true),
                                                                   (6, 'Arquitecto', true),
                                                                   (7, 'Psicólogo', true),
                                                                   (8, 'Economista', true),
                                                                   (9, 'Diseñador Gráfico', true),
                                                                   (10, 'Farmacéutico', true),
                                                                   (11, 'Ingeniero Eléctrico', true),
                                                                   (12, 'Historiador', true),
                                                                   (13, 'Físico', true),
                                                                   (14, 'Periodista', true),
                                                                   (15, 'Chef', true);

--persona - administrador
insert into persona (persona_id, nombre, apellido_paterno, apellido_materno, correo, carnet_identidad, fecha_nacimiento, genero, celular, descripcion, uuid_foto, uuid_portada, direccion, fecha_registro, estado_civil, estado) values
(1, 'Juan', 'Peres', 'Lopez', 'juan.peres@ucb.edu.bo', '1234567', '05/05/1990', 'Hombre', '7777777', 'Administrador', '', '', '', now(), '', true);

insert into usuario (user_id, username, secret, rol, persona_id, estado) values
(1, 'admin', '$2a$12$YQQn4unEh7zvCePHNhUn3e7jONEL3IaqxHNonYzge8HSQefqeAPMC', 'ADMIN', 1, true);

-- Para reiniciar el autoincremental de la tabla carrera, debido a que se insertaron datos manualmente
ALTER SEQUENCE seq_carrera RESTART WITH 26;

-- Para reiniciar el autoincremental de la tabla profesion, debido a que se insertaron datos manualmente
ALTER SEQUENCE seq_profesion RESTART WITH 16;

ALTER SEQUENCE seq_persona RESTART WITH 2;
ALTER SEQUENCE seq_usuario RESTART WITH 2;