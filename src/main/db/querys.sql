-- QUERYS --

-- Obtener una lista de todos los restaurantes disponibles segun sus horarios

SELECT DISTINCT res.name, res.address, sch.day_of_week
FROM restaurant res
INNER JOIN schedule sch
	ON sch.restaurant_id = res.restaurant_id
WHERE sch.day_of_week = LOWER(DAYNAME(CURDATE()))
	AND time(now()) BETWEEN sch.start_time AND sch.end_time;
    
    
INSERT INTO user (email,name,surname,password,phone_number,dni,address,role) VALUES
	('asd@gmail.com','pepe','asdasd','123','32325325','34343434','Alvear 1231','client');

UPDATE user
	SET email= 'santifilippini2005@gmail.com', name='santi', surname='filippini'
    WHERE user_id=1;
    