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
    
    
INSERT INTO product_type (name) VALUES
	("Embutidos"), -- 1
    ("Pastas"), -- 2
    ("Carnes"), -- 3
    ("Postres"); -- 4
    
INSERT INTO product (description,price,product_type_id,restaurant_id) VALUES
("Jamón crudo, 300g",1290.32,1,1),
("Roast beef, 200g",19020.2,3,1),
("Fideos a la carbonara",16059.29,2,1),
("Tiramisu",12292,4,1),
("Milanesa Napolitana",7893.2,3,1);
    