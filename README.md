# JavaFX POS - UX/UI & Smart Persistence

Este es un sistema de Punto de Venta (POS) diseñado con un enfoque prioritario en la **experiencia de usuario (UX)** y una arquitectura de persistencia flexible. Utiliza **JavaFX** y **CSS** para lograr una interfaz moderna, limpia y altamente personalizable.

---

### Persistencia Inteligente (Zero-Config)

Una de las características más destacadas de este proyecto es su capacidad de **auto-configuración de base de datos**:

* **Conexión Automática:** El sistema intenta conectar inicialmente con **MySQL**.
* **Mecanismo de Fallback:** Si el servidor MySQL no está disponible o la base de datos no existe, el sistema detecta la interrupción y genera automáticamente una base de datos local **SQLite**.
* **Despliegue Inmediato:** Esto permite que la aplicación sea funcional desde el primer segundo, sin necesidad de configuraciones complejas por parte del usuario final.
* *Toda esta lógica reside en la clase `database.java`.*

---

### Personalización y Experiencia de Usuario (UX)

El sistema fue diseñado para adaptarse visualmente a las necesidades del operador mediante plantillas CSS:

* **Temas Dinámicos:** Cambio de color instantáneo entre **Azul, Gris y Rojo**, cada uno con contrastes ajustados para evitar la fatiga visual.
* **Preferencias del Sistema:**
    * Autocompletado de nombres de usuario en el login.
    * Opción de "Saltar Inicio de Sesión" para acceso rápido en entornos de confianza.
    * Configuración de cantidad de ítems automáticos al escanear códigos de barras.
    * Activación/Desactivación de alertas globales.

---

### Detalles Técnicos y Seguridad

* **Tecnologías:** JavaFX, CSS, JDBC (MySQL & SQLite).
* **Aviso de Seguridad:** Las credenciales de la base de datos se encuentran *hardcoded* dentro del código. Soy consciente de que esto representa un riesgo de seguridad en entornos de producción; esta implementación fue parte del proceso de aprendizaje inicial y ha sido corregida en proyectos posteriores.

---

### Vista Previa del Sistema

### Pantalla de Acceso (Login)
> Incluye funciones de autocompletado y bypass de seguridad configurable.
<img width="1025" height="625" alt="image" src="https://github.com/user-attachments/assets/327d4296-3cd9-45ba-aed5-320ffe3d8fff" />

### Panel: Crear Venta
> Flujo de trabajo optimizado para ventas rápidas y manejo de cantidades.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/12808029-0436-4800-b34a-74d017cb14d4" />

### Panel: Gestión de Productos
> Filtros de búsqueda avanzada y herramientas de creación/edición.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/ff27971b-ed52-4f77-942b-1806312d478d" />
<img width="1411" height="315" alt="image" src="https://github.com/user-attachments/assets/19fb4622-957f-44a4-a564-e7ca72b38eca" />

### Panel: Registro de Administradores
> Gestión de perfiles con privilegios de administrador.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/674bdba3-ce3a-4709-bcc5-2f8ba0f9b4c9" />

### Panel: Listado de Ventas
> Historial completo y auditoría de transacciones realizadas.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/d21d4858-edc9-4471-91fd-c8ce6d07e9af" />

### Tema rojo
> Panel de la aplicación con colores rojos y la barra lateral abierta.
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/77b06152-3264-468a-9f73-5cd3e36952a0" />

### Configuración
> Panel donde se muestran las configuraciones mencionadas.
<img width="777" height="627" alt="image" src="https://github.com/user-attachments/assets/d20e88fa-5dd9-4003-b3a5-4b2354d7d1bd" />

---

### Instrucciones de Inicio

1. Ejecuta el proyecto desde tu IDE favorito asegurándote de tener configurado el SDK de **JavaFX**.
2. Al iniciar, el sistema buscará una instancia de MySQL. Si no la encuentra, verás que se crea el archivo `.db` de **SQLite** automáticamente en la carpeta raíz para empezar a trabajar de inmediato.

---
**Desarrollado con enfoque en la usabilidad y la resiliencia de datos.**
