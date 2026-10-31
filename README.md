# EduManager

Software modular de gestión educativa para instituciones escolares y universitarias (públicas o privadas) de Venezuela,
adaptable a otros países. Se instala como paquete en servidores propios o VPS, y opcionalmente en la nube (AWS de referencia).

**Stack objetivo:** Java 21 · Spring Boot 3 (monolito modular con Spring Modulith) · PostgreSQL · Angular · Docker · AWS (Terraform).

**Estado:** diseño aprobado; `backend/` contiene una demo (Spring Boot + H2) que se reorganizará por módulos en la fase 1; `frontend/` y `deploy/` están por crear.

## Documentación
- [`docs/ARQUITECTURA.md`](docs/ARQUITECTURA.md): decisiones, módulos, IA, despliegue, fases.
- [`docs/MODELO-NOTAS-Y-FINANZAS.md`](docs/MODELO-NOTAS-Y-FINANZAS.md): evaluación con escalas de letras, 01–20 y 1–10; finanzas multimoneda.
- [`docs/ACCESO-Y-CONFIDENCIALIDAD.md`](docs/ACCESO-Y-CONFIDENCIALIDAD.md): roles, niveles de autoridad y confidencialidad.
- [`docs/REFACTOR-PLAN.md`](docs/REFACTOR-PLAN.md): estado encontrado el 31-oct-2026 y primer paso.

## Demo actual del backend
```
cd backend && mvn verify
java -jar target/edumanager-0.2.0.jar --spring.profiles.active=demo   # http://localhost:8080
```

## Estructura
`backend/` · `frontend/` · `deploy/` · `docs/` · `legacy/` (esqueletos antiguos de React y web2py, sin mantenimiento).
