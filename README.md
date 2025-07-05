# Fingeso-Hito-3
Repositorio para hito 3 de fingeso

# Instrucciones Generales del Proyecto

## 1. Requisitos previos para el Backend

1. **Tener instalado un JDK de Java 17.**
2. **Configurar las variables de entorno en la aplicación Backend:**
   - Modifica la configuración de variables de entorno en tu IDE o en la configuración de ejecución del proyecto (por ejemplo, en la opción "Modify option -> Environmental variable").
   - Agrega las siguientes variables:
     - `DB_USERNAME` = nombredepostgres
     - `DB_PASSWORD` = passwordpostgres
     - `DB_HOST` = localhost
     - `DB_PORT` = puerto indicado en tu instalación de Postgres
3. **Base de datos:**
   - Debes tener PostgreSQL abierto y funcionando.
   - La base de datos debe llamarse `backendFingeso`.
   - El sistema creará las tablas automáticamente al iniciar el backend.

---

## 2. Inicialización de datos en el Backend (usando Postman)

Antes de probar el sistema, es necesario inicializar los datos base en la base de datos. Para esto, existen tres endpoints que deben ser llamados usando Postman (o cualquier cliente HTTP):

### 2.1 Inicializar Roles
- **Método:** POST
- **URL:** `http://localhost:8080/api/roles/initialize`
- **Descripción:** Elimina todos los roles existentes y crea los roles básicos: Administrador, Médico, Técnico, Recepcionista, Farmacéutico y Paciente.

### 2.2 Inicializar Farmacias
- **Método:** POST
- **URL:** `http://localhost:8080/api/farmacia/initialize`
- **Descripción:** Elimina todas las farmacias existentes y crea las farmacias base: Farmacia Central y Farmacia Norte.

### 2.3 Inicializar Especialidades
- **Método:** POST
- **URL:** `http://localhost:8080/api/especialidad/initialize`
- **Descripción:** Elimina todas las especialidades existentes, reinicia el autoincremental de IDs y crea especialidades como Cardiología, Dermatología, Pediatría, Neurología, etc.

#### Pasos para ejecutar los endpoints en Postman

1. Abre Postman.
2. Crea una nueva petición de tipo POST.
3. Ingresa la URL correspondiente a cada endpoint.
4. Haz clic en "Send" (no es necesario enviar body).
5. Repite el proceso para los tres endpoints.

> **Nota:** Estos endpoints eliminan todos los datos previos en las tablas correspondientes, por lo que solo deben usarse para inicializar o reiniciar el sistema en un entorno de pruebas.

---

## 3. Cómo crear un Administrador (ejemplo de JSON)

Para crear un administrador, realiza una petición POST al siguiente endpoint:

- **URL:** `http://localhost:8080/api/administrador/`
- **Método:** POST
- **Body (JSON):**

```json
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "correo": "juan.perez@example.com",
  "telefono": "123456789",
  "password": "password123",
  "rut": "123456789",
  "rol": {
    "id_rol": 1,
    "nombre": "Administrador"
  }
}
```

Donde:
- `rol.id_rol` debe corresponder al ID del rol de Administrador (usualmente 1 si inicializaste los roles con el endpoint correspondiente).
- Todos los campos son obligatorios excepto `telefono`.

Puedes hacer esta petición usando Postman o cualquier cliente HTTP.

---

## 4. Cómo iniciar el Frontend

1. Abre una terminal y navega a la carpeta `Fronend` del proyecto.
2. Ejecuta el siguiente comando para instalar las dependencias:
   ```sh
   npm install
   ```
3. Para iniciar el servidor de desarrollo, ejecuta:
   ```sh
   npm run dev
   ```
4. El frontend estará disponible normalmente en `http://localhost:5173`.

---

## 5. Recomendaciones adicionales
- Asegúrate de tener Node.js y npm instalados para el frontend.
- El backend debe estar corriendo en `http://localhost:8080` para que el frontend pueda comunicarse correctamente.
- Si tienes una colección de Postman, impórtala para facilitar las pruebas de los endpoints.
