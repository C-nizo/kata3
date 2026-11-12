# Kata 3 — Transformación de datos a JSON

**Ingeniería de Software 2 — Universidad de Las Palmas de Gran Canaria**

## 1. Objetivo

Esta kata consiste en obtener datos externos, interpretarlos, validarlos, transformarlos en objetos de dominio y generar una salida JSON válida y verificable.

La aplicación consulta la API pública [RandomUser](https://randomuser.me/), convierte sus registros en objetos `User` y escribe un archivo `users.json` con los usuarios válidos y un bloque de metadatos. La implementación separa las responsabilidades mediante una organización inspirada en la arquitectura hexagonal.

## 2. Tecnologías y dependencias

- **Java 27 (OpenJDK)**.
- **Maven**, para compilar el proyecto y gestionar las dependencias.
- **Gson 2.15.0**, para interpretar y generar JSON.
- **IntelliJ IDEA**, como entorno de desarrollo.
- **API RandomUser**, como origen de datos mediante HTTP.

Se requiere acceso a Internet para ejecutar el flujo completo con la API.

## 3. Estructura y responsabilidades

Código principal en `src/main/java/software/ulpgc/katas/`:

```text
software/ulpgc/katas/
├── Main.java
├── application/
│   ├── domain/model/
│   │   └── User.java
│   ├── ports/
│   │   ├── UserStore.java
│   │   └── UserOutput.java
│   └── usecases/
│       └── ProcessUsersUseCase.java
└── infrastructure/
    ├── adapters/
    │   ├── RandomUserStore.java
    │   └── JsonUserOutput.java
    └── randomuser/
        ├── UserDto.java
        ├── UserParser.java
        ├── UserValidator.java
        └── UserMapper.java
```

- **`User`**: modelo de dominio con nombre completo, fecha de nacimiento (`Instant`) y correo electrónico.
- **`UserStore` / `UserOutput`**: puertos que abstraen la obtención y la salida de usuarios.
- **`ProcessUsersUseCase`**: coordina la lectura y la escritura sin depender de HTTP ni de Gson.
- **`RandomUserStore`**: adaptador que solicita datos mediante HTTP y utiliza los componentes de interpretación y transformación.
- **`UserDto`**: representación de los campos de la respuesta externa.
- **`UserParser`**: convierte el JSON recibido en una lista de DTO mediante Gson.
- **`UserValidator`**: descarta registros sin nombre, con fecha incorrecta o con correo que no supera una comprobación básica de formato.
- **`UserMapper`**: convierte cada DTO válido en un objeto `User`.
- **`JsonUserOutput`**: serializa los usuarios y el metadato `count` en un fichero JSON UTF-8.
- **`Main`**: instancia los adaptadores, los inyecta en el caso de uso y ejecuta el programa.

El núcleo `application` depende de interfaces, mientras que `infrastructure` contiene las implementaciones concretas. Así se evita que el caso de uso conozca la tecnología de entrada o salida.

## 4. Compilación y ejecución

1. Abrir el proyecto con IntelliJ IDEA y configurar **JDK 27**.
2. Importar o sincronizar las dependencias de Maven desde `pom.xml`.
3. Compilar desde la raíz del proyecto:

   ```bash
   mvn clean compile
   ```

4. Ejecutar `Main.java` desde IntelliJ IDEA. Como alternativa, con Maven y el plugin de ejecución disponible:

   ```bash
   mvn exec:java -Dexec.mainClass=software.ulpgc.katas.Main
   ```

El programa solicita **20 usuarios** a `https://randomuser.me/api/?results=20` y escribe `users.json` en el directorio de trabajo (normalmente, la raíz del proyecto en IntelliJ). Este archivo es una salida generada y no forma parte del código fuente entregado.

**Nota:** la ejecución depende de la disponibilidad y conectividad con RandomUser. Un error de conexión HTTP impide generar la nueva salida, pero no demuestra por sí mismo un fallo de parsing o transformación.

## 5. Ejemplos de entrada y salida esperada

La API entrega un objeto JSON con una propiedad `results`. El siguiente ejemplo reducido incluye un registro válido y uno con fecha incorrecta:

**Entrada de ejemplo:**

```json
{
  "results": [
    {
      "name": { "first": "Ana", "last": "García" },
      "dob": { "date": "2000-05-15T10:30:00.000Z" },
      "email": "ana@example.com"
    },
    {
      "name": { "first": "Juan", "last": "López" },
      "dob": { "date": "fecha-invalida" },
      "email": "juan@example.com"
    }
  ]
}
```

**Salida esperada de ese ejemplo:**

```json
{
  "users": [
    {
      "name": "Ana García",
      "birthday": "2000-05-15T10:30:00Z",
      "email": "ana@example.com"
    }
  ],
  "metadata": {
    "count": 1
  }
}
```

`count` indica el número de usuarios válidos serializados. El registro de Juan se descartaría por su fecha inválida. **Este ejemplo es un caso esperado para verificar; no se presenta como una ejecución ya realizada.**

## 6. Repeticiones realizadas

Se ha reconstruido la aplicación tres veces, manteniendo el objetivo de obtener usuarios externos, crear objetos de dominio y generar JSON:

| Repetición | Alcance y variación |
|---|---|
| **1** | Consulta a RandomUser, transformación de nombre y fecha de nacimiento, y salida JSON con `count`. |
| **2** | Reconstrucción desde cero; incorporación del metadato `source` en la salida. |
| **3** | Reconstrucción desde cero; incorporación y validación básica del correo electrónico de cada usuario. |

Las reconstrucciones se han desarrollado en `develop`, dejando identificables los avances y los reinicios mediante commits. `master` se reserva para la versión final evaluable.

## 7. Verificación

Se ha comprobado el funcionamiento de la aplicación mediante una ejecución real y una serie de pruebas manuales automatizadas, sin utilizar JUnit.

### Ejecución con RandomUser

Se ejecutó `Main` desde IntelliJ IDEA, realizando una petición HTTP a la API RandomUser.

La ejecución finalizó correctamente (`exit code 0`) y generó el archivo `users.json` con:

- 20 usuarios, cada uno con nombre, fecha de nacimiento y correo electrónico.
- Fechas representadas en formato ISO 8601.
- Un bloque `metadata` con `count: 20`, coincidente con el número de usuarios generados.

### Verificaciones adicionales

Se utilizó la clase `ManualVerification`, ubicada en `src/test/java`, para comprobar el procesamiento mediante datos JSON controlados, sin depender de Internet.

| Comprobación | Resultado |
|---|---|
| Interpretación y procesamiento de entradas válidas | OK |
| Descarte de registros con datos incorrectos | OK |
| Tratamiento de una lista de resultados vacía | OK |
| Detección de JSON sintácticamente malformado | OK |
| Generación y posterior interpretación del JSON de salida | OK |
| Lectura equivalente desde fichero y memoria | OK |

Todas las verificaciones finalizaron correctamente (`exit code 0`).

### Reproducción

Para repetir las comprobaciones, basta con ejecutar el método `main()` de `ManualVerification` desde IntelliJ IDEA. No se requieren dependencias adicionales.

Para verificar la conexión HTTP y la generación del archivo `users.json`, se debe ejecutar `Main`, teniendo acceso a Internet.

## 8. Git y GitHub

Repositorio público: **[C-nizo/kata3](https://github.com/C-nizo/kata3)**.

- `develop`: trabajo y repeticiones.
- `master`: versión final para la evaluación.

Los commits permiten localizar las implementaciones de cada repetición y los puntos de preparación de la siguiente reconstrucción.

## 9. Vídeo explicativo

**Pendiente de incorporar el enlace al vídeo** (duración máxima: 7 minutos).

En él se mostrarán la entrada desde RandomUser, la salida JSON, el papel de DTO/parser/validator/mapper, las responsabilidades de los puertos y adaptadores, y las evidencias de verificación manual.