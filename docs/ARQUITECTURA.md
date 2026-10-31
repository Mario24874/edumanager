# EduManager — Arquitectura

Versión 0.1 · 31-oct-2026 · Estado: **diseño aprobado por Mario**, implementación por fases.

## 1. Objetivo y alcance

Software modular para instituciones educativas (escolares y universitarias, públicas o privadas) de Venezuela, adaptable a otros países.
Se instala como paquete en servidores propios o VPS y, opcionalmente, en la nube (AWS de referencia).
Cubre lo administrativo, contable y académico: personas, estructura académica, inscripciones y matrícula, horarios y aulas
(presencial, remota, híbrida), evaluación con escalas distintas, asistencia, finanzas multimoneda, comunicaciones y alertas,
actividades, bienestar estudiantil (consejería), reportes y exportaciones a la autoridad educativa.

Fuera de alcance al inicio: aula virtual propia (se integra con Moodle, Google Classroom o enlaces de videoconferencia), nómina docente completa, biblioteca, comedor, transporte e inventario (módulos posteriores).

## 2. Decisiones de arquitectura

| # | Decisión | Motivo | Alternativa descartada |
|---|---|---|---|
| 1 | **Monolito modular** con Spring Modulith | Se instala en servidores de instituciones sin equipo de operaciones; inscripción, finanzas y notas necesitan transacciones coherentes; un solo despliegue y un solo respaldo. Los módulos tienen fronteras verificadas por pruebas y se comunican por eventos, así que se pueden extraer más adelante. | Microservicios: exigen orquestación, trazabilidad distribuida y consistencia entre servicios que la institución no va a operar. |
| 2 | **Java 21 (LTS), Spring Boot 3, Maven** | Pedido de Mario y buen ajuste al tipo de sistema. El código actual está en Java 17: se sube a 21 en la fase 1. | — |
| 3 | **PostgreSQL + Flyway** | Sin costo de licencia, sólido, JSONB para configuración flexible, seguridad por filas (row-level security) si hace falta. | H2 solo para pruebas rápidas; MySQL. |
| 4 | **Angular (LTS), componentes standalone, signals, Angular Material, PWA** | Pedido de Mario; rutas por módulo con carga bajo demanda; cliente de la API generado desde OpenAPI. | React (el esqueleto antiguo queda en `legacy/`). |
| 5 | **Una instalación por institución**, con `institution_id` en las tablas desde el primer día | La instalación estándar es de una sola institución; la columna permite agrupar instituciones (distritos, redes) o un servicio alojado sin migrar después. | Multi-tenant puro: más complejidad hoy sin necesidad inmediata. |
| 6 | **Escalas de calificación como datos**, no como código | Letras, 01–20 y 1–10 conviven; cada institución y programa elige las suyas. Ver `MODELO-NOTAS-Y-FINANZAS.md`. | Un campo numérico único con conversiones fijas. |
| 7 | **Finanzas multimoneda** con tipo de cambio versionado y asientos de doble partida | Venezuela opera en bolívares y divisas a la vez. Ver `MODELO-NOTAS-Y-FINANZAS.md`. | Una moneda base con conversión al mostrar. |
| 8 | **Autenticación local con Spring Security** (usuarios propios, MFA, sin internet) y adaptadores opcionales OIDC (Keycloak, Cognito, Google) | Debe funcionar en un servidor aislado; la nube es opcional. | Depender de un proveedor externo. |
| 9 | **IA opcional, nunca automática, con persona que aprueba** | Ver §4. | IA obligatoria o que actúa sola. |
| 10 | **Normativa oficial como fuente de configuración** (módulo `regulatory`): agentes de IA especialistas proponen reglas con cita, una persona las aprueba y llegan a las instalaciones como **paquete normativo firmado** | Las reglas académicas y fiscales cambian; deben salir de documentos oficiales, con procedencia, y no codificarse de memoria. Ver `REGULACION-Y-CUMPLIMIENTO.md`. | Codificar las reglas a mano; que la IA cambie la configuración por sí sola. |
| 11 | **Empaquetado en Docker Compose** + instalador, y guía de AWS con Terraform | Instalable por personal de TI básico; la nube es una variante, no un requisito. | Kubernetes como único camino. |

## 3. Módulos

Un paquete Java por módulo (`work.mariomoreno.edumanager.<modulo>`). Cada módulo expone una interfaz pública pequeña y
publica eventos (por ejemplo `StudentEnrolled`, `GradeChanged`, `PaymentReceived`). Ningún módulo accede a las tablas de otro:
solo a su interfaz pública o a sus eventos. Esto lo verifican las pruebas de Spring Modulith y ArchUnit.

| Módulo | Contenido |
|---|---|
| `identity` | usuarios, roles, niveles de autoridad, permisos, MFA, sesiones, invitaciones, bitácora de acceso |
| `institution` | institución, sedes, años escolares, lapsos o semestres, calendario, configuración |
| `people` | estudiantes, representantes, personal docente y administrativo, documentos, vínculos familiares |
| `academic` | programas, niveles, grados, secciones, materias, planes de estudio, aulas y recursos |
| `enrollment` | admisión, cupos, inscripción, matrícula, traslados, retiros, reingresos |
| `scheduling` | horarios, asignación de aulas y secciones, modalidad presencial/remota/híbrida, disponibilidad, choques |
| `assessment` | escalas, evaluaciones, notas, cierre de lapso, promoción, historial académico |
| `attendance` | asistencia por clase, justificaciones, alertas de inasistencia |
| `finance` | conceptos, mensualidades, pagos, becas y descuentos, donaciones, aportes, contribuciones, contabilidad, monedas |
| `communication` | notificaciones y alertas por rol (correo, SMS, WhatsApp, push, bandeja interna), plantillas, reglas |
| `activities` | actividades extracurriculares, clubes, eventos, cursos, mentorías, acompañamientos |
| `welfare` | consejería y psicología, casos, seguimiento (acceso restringido) |
| `reporting` | boletines, resúmenes de notas, estadísticas, informes, perfiles de exportación a la autoridad educativa |
| `documents` | constancias, certificados, PDF, almacenamiento de archivos |
| `audit` | bitácora inmutable de cambios y lecturas sensibles |
| `ai` | proveedor de IA conectable, asistentes y controles (ver §4) |
| `regulatory` | paquetes normativos con citas, aplicación con diferencias y vigencias (los agentes viven en un servicio aparte del proveedor) |
| `integration` | puertos hacia pasarelas de pago, SMS/WhatsApp, aulas virtuales y servicios de nube |

**Dependencias permitidas:** `identity`, `institution`, `audit` y `communication` son transversales. El resto depende de `people` y
`academic`, no al revés. `finance` y `assessment` reaccionan a eventos de `enrollment` en vez de llamarlo.

Estructura del repositorio:
```
backend/    Spring Boot (Maven), módulos como paquetes
frontend/   Angular
deploy/     compose, instalador sin internet, Terraform para AWS
docs/       esta documentación
legacy/     esqueletos antiguos (React, web2py); sin mantenimiento
```

## 4. Inteligencia artificial

Reglas (coinciden con las de Mario): la IA **solo actúa cuando un usuario autorizado la pide**, **propone y una persona aprueba**,
tiene **tope de costo** por institución y deja **registro** de cada uso. Con el proveedor desactivado el sistema funciona completo.

Proveedor conectable (`AiProvider`): desactivado · modelo local (por ejemplo Ollama, para instituciones que no pueden sacar datos) · API en la nube.
**Los datos de estudiantes (clases C2 y C3, ver `ACCESO-Y-CONFIDENCIALIDAD.md`) no se envían a un servicio externo por defecto.**
Solo con autorización expresa de la institución y datos seudonimizados; lo restringido (C3) nunca sale.

| Función | Cómo se hace | Datos que toca |
|---|---|---|
| **Adaptar el formato oficial de la autoridad educativa** | El usuario sube el documento de referencia (PDF o Excel). La IA lo analiza (campos, columnas, orden, firmas) y propone un **perfil de exportación**; una persona lo revisa y lo versiona. La exportación real la hace código determinista, no la IA. | Solo el formato, sin datos de estudiantes |
| **Generar horarios, asignar secciones y aulas** | Un **solver de restricciones** (Timefold Solver, Java, Apache 2.0), no un modelo de lenguaje. Restricciones: disponibilidad docente, capacidad de aula, modalidad, bloques seguidos, carga horaria. La IA, si se activa, solo convierte restricciones escritas en lenguaje natural a restricciones estructuradas y explica conflictos. | Datos de horarios y aulas, sin notas ni expedientes |
| **Consultas en lenguaje natural sobre reportes** | Sobre vistas de solo lectura, con los permisos del usuario que pregunta. | Según permisos; C3 excluido |
| **Vigilar y proponer normativa** (módulo `regulatory`) | Dos agentes con base de conocimiento de documentos oficiales y citas verificadas; flujo con LangGraph; propuestas que revisa una persona. Ver `REGULACION-Y-CUMPLIMIENTO.md`. | Solo documentos públicos, sin datos de estudiantes |
| **Redacción de comunicados y resúmenes de informes** | Borrador que una persona edita y envía. | Sin datos restringidos |

Justificación del solver: horarios y asignación de aulas son un problema de optimización con restricciones, donde un solver da resultados
verificables y repetibles; un modelo de lenguaje por sí solo no garantiza que no haya choques.

## 5. Seguridad (resumen; detalle en `ACCESO-Y-CONFIDENCIALIDAD.md`)

- Acceso por rol, nivel de autoridad y **alcance** (el representante solo ve a sus hijos; el docente solo sus secciones; el supervisor solo su zona).
- Datos clasificados en cuatro niveles de confidencialidad (C0–C3). Consejería, salud y casos disciplinarios son C3 y solo se abren con pertenencia al equipo del caso.
- MFA obligatorio para administración, finanzas, control de estudios y consejería. Contraseñas con hash fuerte, TLS, cifrado de columnas sensibles.
- Bitácora de solo añadir de notas, pagos, matrícula y lecturas de C3. Cambios de nota con versión, motivo y aprobación.
- Respaldos programados y verificados; plan de continuidad ante apagones y conectividad limitada (operación en red local sin internet).
- Datos de menores: validar con asesoría legal el cumplimiento de la normativa venezolana aplicable (por ejemplo la LOPNNA) antes de producción.

## 6. Empaquetado y despliegue

1. **Servidor propio o VPS (instalación estándar):** Docker Compose con la aplicación, PostgreSQL, Nginx con TLS y almacenamiento de archivos; script de instalación, `.env`, migraciones automáticas al arrancar, respaldos con rotación y verificación de salud.
2. **Sin internet:** instalador sin conexión con las imágenes empaquetadas.
3. **AWS (referencia):** Terraform con VPC, RDS PostgreSQL, S3 (documentos y exportaciones), SES (correo), ECS Fargate o EC2 con Compose (opción económica), CloudFront para el frontend, Secrets Manager y CloudWatch. S3 y Athena o Redshift quedan para análisis agregados en la nube, no para operar.
4. **Otras nubes o Kubernetes:** posible después con el mismo contenedor; no es parte de la primera entrega.

## 7. Condición de arranque y fases

**Condición de arranque:** no se ejecuta nada de este diseño (código, instalación, servicios) hasta que se eliminen los proyectos y servicios de Italianto del VPS y se libere espacio suficiente.
Hoy el disco está al 93 % (7,5 GB libres). Hasta entonces solo se escribe planificación. Meta sugerida antes de arrancar: disco por debajo del 70 % y al menos 25 GB libres para JDK, Maven, Node, imágenes de Docker y PostgreSQL.

### Fases

| Fase | Entrega | Criterio de «hecho» |
|---|---|---|
| 0 | Esta documentación y la estructura del repo | Documentos revisados por Mario |
| 1 | `identity`, `institution`, `people` + Java 21 + PostgreSQL + Flyway + Angular base + empaquetado Compose | Instalar con un comando, iniciar sesión con MFA, crear institución, usuarios y roles con alcance |
| 2 | `academic`, `enrollment`, **`assessment` (escalas, notas, historial)** + paquete normativo manual con citas (R0) | Un alumno de prueba recorre las tres escalas y su historial se consulta completo; **es la demo del portafolio** |
| 3 | `attendance`, `scheduling` (solver), boletines | Horario generado sin choques; boletín PDF |
| 4 | `finance` multimoneda | Cargo en USD pagado en VES con el cambio del día; asientos cuadrados |
| 5 | `communication`, alertas por rol | Alertas por inasistencia y por mora, por rol |
| 6 | `activities`, `welfare` | Caso con equipo y registro de lecturas |
| 7 | `reporting` oficial, IA conectable, despliegue AWS; agentes de normativa (R1–R4 de `REGULACION-Y-CUMPLIMIENTO.md`) | Perfil de exportación adaptado desde un formato real; guía de AWS probada |

Cada fase termina con pruebas automáticas (JUnit, Testcontainers con PostgreSQL, pruebas de fronteras de módulos) y una demostración.

## 8. Riesgos, supuestos y validaciones pendientes

- **Normativa oficial.** La normativa es pública. Reglas de aprobación, repitencia, formatos de entrega y reglas fiscales se configuran como datos, con procedencia, mediante el módulo `regulatory` (primero carga manual con citas; después agentes que proponen y una persona aprueba). No se codifican de memoria.
- **Aspectos fiscales y contables** (facturación, IVA, impuestos sobre pagos en divisas): reglas configurables por país que provienen de la normativa del SENIAT; un contador las revisa antes de producción.
- **Protección de datos de menores:** validación legal pendiente (§5).
- **Alcance grande:** el orden de fases lo controla; cada fase es utilizable por sí sola.
- **Código existente:** el `backend/` actual (Student, Subject, Classroom, Attendance con H2) es una demo; se reorganiza por módulos y se migra a PostgreSQL en la fase 1.
