# Kata 3 — Consumo de un servicio web y serialización JSON

**Ingeniería del Software 2 — ULPGC**

## Objetivo

Practicar el consumo de un servicio web mediante `HttpClient` de Java, la deserialización de su respuesta con Gson, la transformación de datos y la serialización del resultado en un fichero JSON.

La aplicación consulta la API **Random User**, selecciona y valida datos de usuarios y genera `users.json` en UTF-8. Se han realizado tres reconstrucciones del ejercicio, introduciendo pequeñas variaciones.

## Tecnologías y dependencias

- Java 27 (OpenJDK).
- Maven.
- IntelliJ IDEA.
- Git y GitHub.
- Gson 2.15.0, gestionado mediante Maven.

El paquete base es `software.ulpgc.katas`.

## Arquitectura

El proyecto sigue una arquitectura hexagonal, separando la lógica de la aplicación de la comunicación HTTP y de la escritura del fichero.

**`architecture`**

- `User`: modelo de usuario con nombre, fecha de nacimiento y correo electrónico.
- `UserLoader`: puerto para obtener una lista de usuarios.
- `UserWriter`: puerto para escribir una lista de usuarios.
- `ProcessUsersOperation`: coordina la carga y la escritura sin conocer los adaptadores concretos.

**`infrastructure`**

- `Main`: configura los adaptadores y ejecuta la operación.
- `RandomUserLoader`: implementa `UserLoader` combinando la obtención y la transformación.
- `UserFetcher`: realiza la petición GET, comprueba el estado HTTP y controla los errores de comunicación.
- `UserDeserializer` y `UserDto`: interpretan el JSON recibido.
- `UserTransformer`, `UserValidator` y `UserMapper`: validan los registros y los convierten a `User`.
- `JsonUserSerializer`: convierte los usuarios y sus metadatos a una cadena JSON.
- `JsonUserWriter`: escribe esa cadena en un fichero UTF-8.

La comunicación HTTP utiliza un tiempo de espera de **15 segundos**. Se distinguen los errores de estado HTTP, conexión, espera agotada y respuesta JSON inválida.

## Servicio web y formato de los datos

Se utiliza [Random User API](https://randomuser.me/).

La URL base se configura en `Main`:

```text
https://randomuser.me/api
```

Para la tercera repetición se solicitan 20 usuarios mediante una petición GET:

```text
https://randomuser.me/api/?results=20
```

El cliente utiliza los campos externos `name.first`, `name.last`, `dob.date` y `email`.

**Ejemplo de entrada conocida:**

```json
{
  "results": [
    {
      "name": {"first": "Ana", "last": "Pérez"},
      "dob": {"date": "1990-05-14T10:30:00.000Z"},
      "email": "ana@example.com"
    }
  ]
}
```

**Resultado esperado:**

```json
{
  "users": [
    {
      "name": "Ana Pérez",
      "birthday": "1990-05-14T10:30:00Z",
      "email": "ana@example.com"
    }
  ],
  "metadata": {
    "count": 1
  }
}
```

Los registros sin nombre completo, fecha válida o correo con formato adecuado se descartan.

## Compilación y ejecución

En IntelliJ IDEA:

1. Abrir el proyecto como proyecto Maven y configurar **JDK 27**.
2. Compilar el proyecto.
3. Ejecutar `software.ulpgc.katas.infrastructure.Main`.

También puede hacerse desde la raíz del proyecto. En Windows, con Maven y JDK 27 disponibles:

```powershell
mvn clean test-compile dependency:copy-dependencies
java -cp "target/classes;target/dependency/*" software.ulpgc.katas.infrastructure.Main
```

En Linux o macOS, sustituir `;` por `:` en el classpath.

Al ejecutarse correctamente se genera `users.json` en la raíz del proyecto. En la tercera repetición contiene 20 usuarios y `metadata.count` vale 20.

## Repeticiones realizadas

La kata se reconstruyó tres veces manteniendo la consulta HTTP, la transformación y la salida con Gson.

| Repetición | Variación | Salida |
|---|---|---|
| Primera | Nombre y fecha de nacimiento de 20 usuarios. | `users` y `metadata.count`. |
| Segunda | Consulta de un usuario e incorporación del origen de los datos. | Se añade `metadata.source`. |
| Tercera | Recuperación de 20 usuarios e incorporación y validación del correo electrónico. | `users` incluye `email`; los metadatos vuelven a contener solo `count`. |

Cada reconstrucción se realizó en `develop`, con commits para identificar su preparación e implementación, y se integró posteriormente en `master`.

## Verificación

Se incluye una verificación reproducible sin JUnit:

```text
src/test/java/software/ulpgc/katas/verification/ManualVerification.java
src/test/resources/verification/
```

Para ejecutarla desde IntelliJ IDEA, iniciar `ManualVerification.main()`. Desde PowerShell, después de compilar con el comando indicado anteriormente:

```powershell
java -cp "target/classes;target/test-classes;target/dependency/*" software.ulpgc.katas.verification.ManualVerification
```

Se utilizan respuestas JSON controladas para comprobar:

1. Interpretación de una entrada válida.
2. Descarte de registros inválidos.
3. Tratamiento de una entrada vacía.
4. Detección de JSON malformado.
5. Coincidencia entre el JSON generado y el resultado esperado.
6. Coincidencia entre la serialización en memoria y el fichero escrito.

La ejecución de las seis comprobaciones terminó con **`Process finished with exit code 0`** y el mensaje `Todas las verificaciones superadas.`

También se verificó el funcionamiento del cliente HTTP con la API real: la petición tuvo éxito y generó el fichero con 20 usuarios.

**Errores HTTP comprobados manualmente:** se cambió temporalmente la URL base en `Main`, sin modificar el resto del programa:

- `https://httpbin.org/status/404`: se detectó `Error HTTP: 404`.
- `http://127.0.0.1:1`: se detectó `Error de conexión` ante una conexión rechazada.

En ambos casos se obtuvo un código de salida 1, como corresponde a las excepciones provocadas deliberadamente. Después se restauró la URL original de Random User.

## Git y GitHub

Repositorio: [C-nizo/kata3](https://github.com/C-nizo/kata3)

- `develop`: rama de desarrollo.
- `master`: versión evaluable.

El historial de commits permite identificar las tres repeticiones y recuperar el código de cada una.

Para clonar el proyecto:

```bash
git clone https://github.com/C-nizo/kata3.git
```

Después, abrirlo en IntelliJ IDEA, cargar Maven y ejecutar las instrucciones anteriores.

## Vídeo explicativo

Pendiente de incorporación del enlace al vídeo, de duración máxima de 7 minutos.