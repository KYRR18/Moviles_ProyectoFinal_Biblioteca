# 🖥️ API Biblioteca — Guía de Instalación Local

Esta guía explica cómo poner en marcha el servidor de la API REST para el proyecto **Biblioteca** usando XAMPP, de modo que la aplicación Android pueda conectarse a ella desde el emulador.

---

## ✅ Requisitos Previos

| Herramienta | Versión mínima | Enlace de descarga |
|---|---|---|
| XAMPP | 8.x | [https://www.apachefriends.org](https://www.apachefriends.org) |
| MySQL (incluido en XAMPP) | 5.7+ | — |
| Navegador web | Cualquiera | — |

---

## 📦 Paso 1 — Extraer los archivos del servidor

1. Dentro de este repositorio encontrarás el archivo **`server/api_biblioteca.zip`**.
2. Extrae su contenido dentro de la carpeta `htdocs` de XAMPP:

   ```
   C:\xampp\htdocs\
   └── api_biblioteca\
       ├── api\
       │   ├── buscar_libros.php
       │   ├── obtener_libros.php
       │   ├── crear_libro.php
       │   ├── actualizar_libro.php
       │   ├── eliminar_libro.php
       │   ├── crear_usuario.php
       │   ├── verificar_usuario.php
       │   ├── crear_estanteria.php
       │   ├── asignar_libro_estanteria.php
       │   ├── obtener_estanterias_usuario.php
       │   └── obtener_libros_estanteria.php
       ├── config\
       │   ├── db_config.php
       │   └── conexion.php
       └── database.sql
   ```

> [!IMPORTANT]
> La carpeta extraída debe llamarse exactamente `api_biblioteca`. No renombrarla, o las URLs de la app dejarán de funcionar.

---

## ⚙️ Paso 2 — Iniciar los servicios de XAMPP

1. Abre el **XAMPP Control Panel**.
2. Haz clic en **Start** para los módulos:
   - ✅ **Apache**
   - ✅ **MySQL**
3. Ambos deben quedar en verde antes de continuar.

---

## 🗄️ Paso 3 — Crear la base de datos

1. Abre tu navegador y ve a: **[http://localhost/phpmyadmin](http://localhost/phpmyadmin)**
2. En el panel izquierdo, haz clic en **Nueva** para crear una base de datos.
3. Ponle el nombre: **`biblioteca_db`** y haz clic en **Crear**.
4. Con la base de datos seleccionada, ve a la pestaña **SQL**.
5. Copia y pega el contenido del archivo **`api_biblioteca/database.sql`** en el cuadro de texto.
6. Haz clic en **Continuar / Go**.

   Deberías ver las 4 tablas creadas:
   - `libros`
   - `usuarios`
   - `estanterias`
   - `estanteria_libro`

---

## 🔑 Paso 4 — Verificar la configuración de conexión

Abre el archivo `C:\xampp\htdocs\api_biblioteca\config\db_config.php` y asegúrate de que los datos coincidan con tu entorno XAMPP:

```php
define('DB_HOST', 'localhost');
define('DB_NAME', 'biblioteca_db');
define('DB_USER', 'root');      // Usuario por defecto de XAMPP
define('DB_PASS', '');          // Contraseña vacía por defecto en XAMPP
```

> [!NOTE]
> Si cambiaste la contraseña de MySQL en tu XAMPP, actualiza `DB_PASS` con la tuya.

---

## 📱 Paso 5 — Conectar el Emulador Android

La app Android está configurada para usar la IP `10.0.2.2`, que es la forma en que el **Emulador de Android Studio** accede al `localhost` de tu PC.

| Entorno | URL base |
|---|---|
| Emulador Android (AVD) | `http://10.0.2.2/api_biblioteca/api/` |
| Dispositivo físico en la misma red WiFi | `http://<TU_IP_LOCAL>/api_biblioteca/api/` |

> [!TIP]
> Para encontrar tu IP local en Windows: abre CMD y escribe `ipconfig`. Busca la dirección bajo **Adaptador de LAN inalámbrica Wi-Fi > Dirección IPv4**.
> Luego actualiza las URLs en `app/src/main/res/values/strings.xml`.

---

## 🧪 Paso 6 — Verificar que la API funciona

Abre tu navegador y entra a:

```
http://localhost/api_biblioteca/api/obtener_libros.php
```

Deberías ver una respuesta JSON similar a esta (vacía al inicio):

```json
[]
```

Si ves eso, ¡la API está lista! 🎉

---

## 🗺️ Endpoints disponibles

| Endpoint | Método | Descripción |
|---|---|---|
| `obtener_libros.php` | `GET` | Lista todos los libros |
| `buscar_libros.php` | `GET` | Busca libros por término |
| `crear_libro.php` | `POST` | Crea un libro nuevo |
| `actualizar_libro.php` | `PUT` | Modifica un libro |
| `eliminar_libro.php` | `DELETE` | Elimina un libro |
| `crear_usuario.php` | `POST` | Registra un usuario |
| `verificar_usuario.php` | `POST` | Login — valida credenciales |
| `crear_estanteria.php` | `POST` | Crea una estantería para un usuario |
| `asignar_libro_estanteria.php` | `POST` | Asigna un libro a una estantería |
| `obtener_estanterias_usuario.php` | `GET` | Obtiene las estanterías de un usuario |
| `obtener_libros_estanteria.php` | `GET` | Obtiene los libros de una estantería |
| `editar_estanteria.php` | `PUT` | Edita el título de una estantería |
| `eliminar_estanteria.php` | `POST` | Elimina una estantería (y vacía sus libros) |
| `cambiar_estanteria_libro.php` | `PUT` | Mueve un libro a otra estantería |

---

## ❓ Problemas comunes

| Problema | Posible solución |
|---|---|
| Apache no inicia (puerto 80 ocupado) | Cambia el puerto de Apache a 8080 en XAMPP > Apache > Config > `httpd.conf` y actualiza las URLs de `strings.xml` |
| Error de conexión desde la app | Verifica que Apache y MySQL estén activos y que la IP en `strings.xml` sea correcta |
| Error `Access denied for user 'root'` | Comprueba `DB_PASS` en `db_config.php` |
| La base de datos no existe | Repite el Paso 3 |
