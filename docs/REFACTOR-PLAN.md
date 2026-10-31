# Plan de refactor (2026-10-31)

## Estado real encontrado
- El repo NO contiene Angular, Spring Boot, S3, Redshift ni D3. Es React 18 (Create React App, TS) con paginas casi vacias
  + un esqueleto `web2py/` (modelos/controladores Python sin DB definida). 0 lineas en factory/models/repository TS.
- `src/App.test.js` busca "learn react" y fallaria. No hay Java ni build de backend. El stack del portafolio no coincide con el codigo.
- `edumanager-web2py` es la version antigua del mismo esqueleto: no aporta.

## Decision
Crear `backend/` (Spring Boot 3.3 / Java 17 / Maven / JPA / H2) con el dominio del esqueleto
(Student, Subject, Classroom, Attendance) y un perfil `demo` con datos ficticios. Una pagina estatica
(`/`) muestra la demo. Lo que seria S3/Redshift queda tras la interfaz `AttendanceReportSource`.

## Riesgos
- El portafolio dice Angular; aqui el frontend original es React. Se decide sin tocarlo: la demo usa HTML estatico.
- Sin autenticacion (demo con datos ficticios; no exponer datos reales).
- H2 en memoria: los datos se pierden al reiniciar (intencional).

## Reversibilidad
Todo es aditivo en `backend/` y `docs/`; el codigo React/web2py no se modifico. Revertir = borrar `backend/`.

## Ejecucion
`cd backend && mvn verify && java -jar target/edumanager-0.2.0.jar --spring.profiles.active=demo` -> http://localhost:8080
