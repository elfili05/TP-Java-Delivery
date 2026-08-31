
-- DATOS DE EJEMPLO --

-- USERS --
INSERT INTO user
	VALUES (2,'santifilippini2005@gmail.com','Santiago','Filippini','santi123','3464440885',46653844,'L. de la Torre 4913','client');

INSERT INTO restaurant (restaurant_id, name, address) VALUES
(1, 'La Esquina', 'Av. Pellegrini 1234'),
(2, 'El Buen Sabor', 'San Lorenzo 850'),
(3, 'Don Giuseppe', 'Italia 456'),
(4, 'Sabores del Sur', 'Bv. Oroño 1720'),
(5, 'La Terraza', 'Córdoba 2100'),
(6, 'Parrilla El Fogón', 'Mendoza 1450'),
(7, 'Green Garden', 'Santa Fe 980'),
(8, 'Mar y Tierra', 'Entre Ríos 620');

INSERT INTO schedule 
(schedule_number, restaurant_id, start_time, end_time, day_of_week) VALUES

-- La Esquina: 5 horarios
(1, 1, '12:00:00', '16:00:00', 'monday'),
(2, 1, '12:00:00', '16:00:00', 'tuesday'),
(3, 1, '12:00:00', '16:00:00', 'wednesday'),
(4, 1, '20:00:00', '00:00:00', 'friday'),
(5, 1, '20:00:00', '00:00:00', 'saturday'),

-- El Buen Sabor: 3 horarios
(1, 2, '11:30:00', '15:30:00', 'monday'),
(2, 2, '11:30:00', '15:30:00', 'wednesday'),
(3, 2, '20:00:00', '23:30:00', 'friday'),

-- Don Giuseppe: 4 horarios
(1, 3, '12:00:00', '15:00:00', 'tuesday'),
(2, 3, '12:00:00', '15:00:00', 'thursday'),
(3, 3, '20:00:00', '23:30:00', 'friday'),
(4, 3, '20:00:00', '23:30:00', 'saturday'),

-- Sabores del Sur: 2 horarios
(1, 4, '20:00:00', '23:00:00', 'friday'),
(2, 4, '20:00:00', '23:00:00', 'saturday'),
(3, 4, '17:00:00', '21:00:00','tuesday'),

-- La Terraza: 5 horarios
(1, 5, '12:00:00', '16:00:00', 'monday'),
(2, 5, '12:00:00', '16:00:00', 'tuesday'),
(3, 5, '12:00:00', '16:00:00', 'wednesday'),
(4, 5, '20:00:00', '00:00:00', 'friday'),
(5, 5, '20:00:00', '00:00:00', 'saturday'),

-- Parrilla El Fogón: 1 horario
(1, 6, '20:00:00', '23:30:00', 'saturday'),

-- Green Garden: 3 horarios
(1, 7, '12:00:00', '15:00:00', 'monday'),
(2, 7, '12:00:00', '15:00:00', 'thursday'),
(3, 7, '12:00:00', '15:00:00', 'friday'),
(4, 7, '16:00:00', '19:30:00','tuesday')

-- Mar y Tierra: 4 horarios
(1, 8, '12:00:00', '16:00:00', 'tuesday'),
(2, 8, '12:00:00', '16:00:00', 'thursday'),
(3, 8, '20:00:00', '23:30:00', 'friday'),
(4, 8, '20:00:00', '23:30:00', 'sunday');




SELECT DISTINCT res.name, res.address, sch.start_time, sch.day_of_week
FROM restaurant res
INNER JOIN schedule sch
	ON sch.restaurant_id = res.restaurant_id
WHERE sch.day_of_week = LOWER(DAYNAME(CURDATE()))
	AND time(now()) BETWEEN sch.start_time AND sch.end_time;

select * from schedule;
