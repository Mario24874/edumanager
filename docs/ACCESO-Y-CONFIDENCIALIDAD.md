# Accesos, autoridad y confidencialidad

Un usuario tiene uno o varios **roles**; cada rol concede permisos dentro de un **alcance** (institución, sede, programa, sección, estudiante)
y a un **nivel de autoridad**. Además, cada dato tiene una **clase de confidencialidad**. Un acceso se permite solo si coinciden las cuatro cosas:
rol, alcance, autoridad y clase del dato.

## 1. Niveles de autoridad

| Nivel | Puede |
|---|---|
| A1 Consulta | ver lo que su rol y alcance permiten |
| A2 Registro | registrar datos de su ámbito (notas de sus secciones, asistencia, actividades) |
| A3 Coordinación | aprobar cambios dentro del lapso abierto; ver su nivel, área o programa; gestionar a su personal |
| A4 Dirección / Control de estudios | cerrar lapsos, aprobar cambios posteriores al cierre, emitir documentos oficiales, aprobar exportaciones |
| A5 Administración de la institución | configuración, usuarios, roles, escalas, monedas, respaldos |
| Auditor (solo lectura) | ver bitácoras y reportes, sin modificar nada |

## 2. Clases de confidencialidad

| Clase | Contenido | Regla |
|---|---|---|
| C0 Público | calendario, comunicados generales, oferta académica | cualquiera, incluso sin sesión si la institución lo publica |
| C1 Interno | horarios, aulas, listas de secciones, actividades | personal y usuarios del ámbito |
| C2 Académico confidencial | notas, historial, asistencia individual, datos de contacto, estado de pagos | solo quien lo necesita para su función y dentro de su alcance |
| C3 Restringido | consejería, psicología, salud, disciplina, casos, informes de acompañamiento | solo el **equipo del caso**; cada lectura queda registrada; sin envío a servicios externos |

## 3. Roles

Todos los roles son configurables por la institución; estos son los iniciales, con nombres neutrales que la terminología de cada país traduce («representante» o «tutor», por ejemplo). Al crear un usuario, el administrador elige un **cargo** («Secretaria de control de estudios», «Docente») y el sistema aplica el conjunto de permisos correcto; los permisos finos son un modo avanzado. Un usuario puede acumular roles (por ejemplo docente y coordinador).

| Rol | Autoridad típica | Alcance | Ve / hace |
|---|---|---|---|
| Administrador de la institución | A5 | institución | configuración, usuarios, roles; **no** accede a C3 por ser administrador |
| Director / Subdirector | A4 | institución o sede | informes, cierres, aprobaciones, documentos oficiales; C3 solo si integra el equipo del caso |
| Control de estudios | A4 | institución | inscripciones, cierres de lapso, historial, constancias, exportaciones oficiales |
| Coordinador (académico, de nivel, de área o de actividades) | A3 | su nivel, área o programa | aprueba cambios, ve notas y asistencia de su ámbito, gestiona horarios y a su personal |
| Supervisor educativo (externo) | A1 | las instituciones que se le asignen | reportes e indicadores de solo lectura; sin C3; acceso con vigencia y acuerdo de uso |
| Docente / maestro | A2 | sus secciones y materias | registra notas y asistencia, ve su horario y a sus estudiantes (C2 solo de ellos) |
| Docente auxiliar o colaborador | A2 | las actividades o cursos asignados, con fecha de vencimiento | participa y registra en lo asignado |
| Especialista, guía | A2 | los estudiantes asignados | seguimiento de su área; C3 solo si integra el caso |
| Consejero / psicólogo | A2 | casos asignados | expediente de consejería (C3); sus colegas no lo ven salvo que se les sume al caso |
| Mentor | A2 | mentorías asignadas | sesiones y avances de sus mentoreados |
| Personal administrativo | A1–A2 | su función | atención, inscripciones, documentos, según permisos |
| Contable / cajero | A2–A3 | finanzas | cargos, pagos, recibos, reportes financieros; sin notas ni C3 |
| Representante | A1 | sus hijos o representados | notas, asistencia, pagos, comunicados y citas de sus hijos |
| Estudiante | A1 | sí mismo | su horario, notas, asistencia, pagos y actividades |

## 4. Reglas de acceso a casos y expedientes (C3)

- **Equipo del caso:** quien abre el caso define quién lo integra. Solo ellos ven su contenido. Directores y administradores **no** lo ven por su cargo.
- **Acceso de emergencia (romper el vidrio):** una persona con A4 puede pedir acceso a un caso con una justificación escrita; se concede por tiempo limitado, se notifica al responsable del caso y a la dirección, y queda en la bitácora.
- **Registro de lecturas:** cada vez que alguien abre un caso C3 se anota quién, cuándo y desde dónde.
- **Cifrado:** los campos C3 se cifran a nivel de columna; los respaldos van cifrados.
- **Retención y eliminación:** plazos configurables por tipo de expediente; el borrado deja constancia, no el contenido.
- **Representantes y estudiantes** ven el resumen que el equipo del caso decida compartir, no el expediente.

## 5. Gestión de cuentas

- Altas por invitación; el usuario define su contraseña y activa MFA. MFA obligatorio para A4–A5, finanzas, control de estudios y consejería.
- Cuentas de docentes colaboradores y supervisores **con fecha de vencimiento**; al terminar el curso o la actividad se desactivan solas.
- Al salir un empleado, su cuenta se desactiva el mismo día, sin borrar su historial de acciones.
- Un representante puede vincularse a varios hijos; un estudiante puede cambiar de representante solo con aprobación de control de estudios.
- Sesiones con expiración, cierre remoto y límite de intentos.

## 6. Notificaciones por rol

| Quién recibe | Ejemplos |
|---|---|
| Representantes y estudiantes | notas publicadas, inasistencias, citas, mensualidades por vencer, comunicados |
| Docentes | cambios de horario o de aula, plazos de entrega de notas, solicitudes de aprobación |
| Coordinadores | notas pendientes de aprobar, alertas de inasistencia por sección, cambios fuera de plazo |
| Dirección y control de estudios | cierres de lapso pendientes, solicitudes de acceso de emergencia, cambios de nota posteriores al cierre |
| Contabilidad | pagos recibidos, morosidad, diferencias cambiarias |
| Equipo de un caso (C3) | solo los eventos del caso, sin contenido sensible en el cuerpo del mensaje |

Los mensajes no incluyen datos C2 o C3 en el texto; avisan que hay algo nuevo y piden iniciar sesión.

## 7. Cambios de notas

- Dentro del lapso abierto: el docente corrige y el sistema guarda la versión anterior; los cambios grandes los aprueba un coordinador (A3).
- Después del cierre: solo con solicitud con motivo, aprobación A4 y registro en la bitácora; el representante recibe aviso.
- Ninguna nota se borra; siempre se puede ver quién la cambió, cuándo y por qué.

## 8. Cómo se hace cumplir

- Permisos evaluados en el servidor (nunca solo en la interfaz), a nivel de servicio y de consulta.
- Pruebas automáticas por rol que comprueban qué puede y qué no puede ver cada uno (matriz de pruebas por cada módulo).
- Consultas filtradas por alcance (por ejemplo, el docente solo recibe sus secciones desde la propia consulta SQL).
- Bitácora de solo añadir, con encadenamiento de hashes para detectar manipulación.
