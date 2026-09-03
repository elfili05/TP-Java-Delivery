**Casos de Uso**

Aplicación web de Pedidos de comida a Domicilio

UTN FRRo — Java

# **CUR: Realizar pedido**

**Actor:**

Cliente

**Curso Básico (CB):**

1. El sistema informa al cliente todos los restaurantes activos según el horario actual, luego el cliente selecciona un restaurante.  
2. El sistema muestra el menú del restaurante seleccionado, con sus productos, precios.  
3. El cliente selecciona uno o más productos indicando la cantidad de los mismos y confirma el pedido.  
4. El sistema valida que el restaurante esté activo para el momento que se hizo, y calcula el monto total aplicando descuento.  
5. El sistema registra el pedido con estado “Inicial” y calcula el tiempo aproximado de entrega.  
6. El sistema muestra al cliente un resumen del pedido, con su estado y tiempo de entrega estimado.

**Cursos Alternativos (CA):**

1.a \<Previo\> No hay restaurantes disponibles en el horario actual:  
	1.a.1 El sistema informa al cliente que no hay restaurantes disponibles en este momento.  
	1.a.2 FCU.  
3.a \<durante\> El cliente no seleccionó ningún producto:  
	3.a.1 El sistema Informa que debe seleccionar al menos un producto.  
	3.a.2 Vuelve al paso 3\.  
4.a \<Durante\> El restaurante pasó a estado “Inactivo” entre la selección y confirmación de pedido:  
	4.a.1 El sistema informa al cliente que el restaurante ya no está disponible.  
	4.a.2 FCU.  
4.b \<Durante\> El monto del pedido no alcanza el mínimo para aplicar descuento:  
	4.b.1 El sistema calcula el monto total sin aplicar descuento.  
	4.b.2 Sigue al paso 5\.

**Precondiciones:**

* Cliente ya está registrado y cuenta con sesión activa.  
* Los restaurantes están registrados con estado “Activo”.  
* Los productos y tipos de producto están registrados.  
* Los descuentos están registrados.

**Postcondiciones:**

* El pedido queda registrado en el sistema, con estado “Inicial”.

