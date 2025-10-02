# Historias de Usuario

## HU01 - Inicio de sesión

**Como** usuario del sistema,  
**quiero** poder iniciar sesión con mis credenciales(email y contraseña),  
**para** acceder a las funcionalidades según mi rol.

### 🧪 Criterios de aceptación

1. ✅ El sistema debe permitir el ingreso de email y contraseña.
2. ✅ Si las credenciales son correctas, el usuario es redirigido a su vista correspondiente (según el rol).
3. ✅ Si las credenciales son incorrectas, se muestra un mensaje de error claro y no se permite el acceso.
4. ✅ El campo de contraseña debe ocultar los caracteres ingresados.
5. ✅ El sistema debe validar que ningun campo estén vacíos.
6. ✅ Debe existir una opción para cerrar sesión luego de haber ingresado.
7. ✅ El sistema debe recordar la sesión activa (opcional: con checkbox "Recordarme").
8. ✅ Se debe impedir el acceso a usuarios inactivos o bloqueados.

------- 

## - Gestión de Usuarios

### 🟩 HU02A - Creación de usuarios

**Como** administrador,  
**quiero** poder crear cuentas de usuarios y bibliotecarios,  
**para** que puedan acceder al sistema según su rol.

### 🧪 Criterios de aceptación

1. ✅ El administrador puede acceder a un formulario para crear nuevos usuarios.  
2. ✅ El formulario permite ingresar datos como nombre, apellido, email, contraseña , rol y biblioteca (usuario o bibliotecario).  
3. ✅ No se permite crear una cuenta si el email junto con el rol asignado ya existen en el sistema.  
4. ✅ El sistema valida que los campos requeridos no estén vacíos.  
5. ✅ Una vez creado, el usuario recibe una confirmación o mensaje de éxito.   
6. ✅ Solo los administradores autenticados pueden acceder a esta funcionalidad.

-------

### 🟨 HU02B - Modificación de usuarios

**Como** administrador,  
**quiero** poder modificar los datos de cuentas de usuarios y bibliotecarios,  
**para** mantener su información actualizada o corregir errores.

### 🧪 Criterios de aceptación

1. ✅ El administrador puede ver una lista de usuarios existentes.  
2. ✅ El sistema permite seleccionar un usuario y editar sus datos (nombre, email, rol, etc).  
3. ✅ El sistema valida que los nuevos datos sean válidos y no duplicados (ej. rol).  
4. ✅ Al guardar los cambios, se muestra un mensaje de éxito.    
5. ✅ Solo los administradores autenticados pueden acceder a esta funcionalidad.

-------

### 🟥 HU02C - Eliminación de usuarios

**Como** administrador,  
**quiero** poder eliminar cuentas de usuarios y bibliotecarios,  
**para** quitar accesos innecesarios al sistema.

### 🧪 Criterios de aceptación

1. ✅ El administrador puede ver una lista de usuarios existentes.  
2. ✅ El sistema permite seleccionar un usuario para eliminar.  
3. ✅ Antes de eliminar, se solicita confirmación.  
4. ✅ Una vez eliminada la cuenta, el usuario ya no puede iniciar sesión.  
5. ✅ Se muestra un mensaje de éxito tras la eliminación.  .  
6. ✅ Solo los administradores autenticados pueden acceder a esta funcionalidad.

-------

## Gestion de documento

## HU03A - Subida de documento
**Como** administrador o bibliotecario 
**quiero** poder subir un archivo PDF o un direcotrio al sistema  
**para** que sea indexado y disponible para su visualizacion y descarga.

### Criterios de Aceptación:
1. ✅ El administrador o el bibliotecario puede seleccionar un archivo PDF o un directorio para subir al sistema.
2. ✅ El sistema verifica que sea un archivo válido en formato y tamaño (formato PDF).
3. ✅ Los metadatos y ruta de acceso del documentos queda disponible en la base de datos tras su indexación.
4. ✅ El usuario recibe un mensaje de confirmación.

## HU03B - Eliminación de documento
**Como** administrador o bilbiotecario
**quiero** poder eliminar un archivo PDF del sistema
**para** poder mejorar la gestion de los archivos en la biblioteca 

### 🧪 Criterios de aceptación

1. ✅ El bibliotecario y administrador puede visualizar una lista de documentos disponibles en el sistema.
2. ✅ El sistema permite seleccionar un documento específico para su eliminación.
3. ✅ Al seleccionar "Eliminar", el sistema solicita una confirmación previa antes de proceder.
4. ✅ Una vez confirmada la acción, el documento es eliminado del sistema y ya no aparece en la lista.
5. ✅ El sistema muestra un mensaje de éxito tras la eliminación del documento.
6. ✅ Si ocurre un error durante el proceso de eliminación, se muestra un mensaje claro y descriptivo.
7. ✅ Solo los usuarios con rol de **Biblioterio** y **Administrador** pueden eliminar documentos

---

## HU03C - Búsqueda de documentos  
**Como** usuario del sistema  
**quiero** buscar documentos escribiendo palabras claves  
**para** encontrar información específica fácilmente.

### 🧪 Criterios de aceptación

1. ✅ El usuario puede ingresar texto libre como consulta.  
2. ✅ El sistema devuelve resultados relevantes basados en búsqueda semántica.  
3. ✅ Se resaltan fragmentos coincidentes en los resultados.

---

## HU04 - Vista de resultados  
**Como** usuario del sistema  
**quiero** ver un listado de documentos relevantes con fragmentos destacados  
**para** decidir cuál consultar.

### 🧪 Criterios de aceptación

1. ✅ Se muestra una lista de documentos relacionados con la búsqueda.  
2. ✅ Cada resultado incluye fragmentos destacados del contenido.  
3. ✅ El usuario del sistema puede hacer clic en un resultado para abrir el documento.

---

## HU05 - Modificación del perfil  
**Como** usuario del sistema  
**quiero** poder modificar mis datos personales  
**para** tener los datos actualizados y correctos.

### 🧪 Criterios de aceptación

1. ✅ El usuario puede acceder a su perfil desde el sistema.  
2. ✅ El sistema permite editar nombre, correo electrónico y contraseña.  
3. ✅ El sistema solicita confirmación antes de guardar los cambios.  
4. ✅ El sistema muestra un mensaje de éxito tras una modificación válida.  
5. ✅ Si ocurre un error, el sistema muestra un mensaje claro y descriptivo.

---




