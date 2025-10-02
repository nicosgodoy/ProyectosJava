# Requerimientos Funcionales

## Requerimientos Funcionales
1. El sistema debe permitir a un bibliotecario subir y eliminar archivos PDF.
2. El sistema debe indexar automáticamente los PDFs usando Apache Lucene.
3. El sistema debe listar documentos relevantes con extractos destacados.
4. El sistema debe generar un resumen con IA sobre el contenido del documento buscado.
5. El usuario debe poder realizar búsquedas con lenguaje natural.
6. El usuario debe poder abrir un PDF y chatear con la IA sobre su contenido.
7. El usuario debe poder subir propios archivos PDF.
8. El administrador debe poder gestionar usuarios y configuraciones.

## Requerimientos No Funcionales
- El sistema debe estar disponible 99% del tiempo.
- La respuesta del motor de búsqueda debe ser menor a 10 segundos.
- El modelo de IA debe funcionar de manera local, sin conexión externa.
- La interfaz debe ser accesible desde navegadores modernos.

## Observaciones
- Se utilizará React para el frontend y Spring Boot para el backend.
- El modelo LLaMA 3.2 será utilizado para generar resúmenes y respuestas en lenguaje natural.
