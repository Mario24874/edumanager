# Núcleo genérico, perfiles de país y facilidad de uso

Versión 0.1 · 31-oct-2026 · Estado: **planificación** (requisito de Mario). Complementa `ARQUITECTURA.md`.

## 1. Principios

1. **El núcleo no conoce ningún país.** Venezuela es el **primer perfil de jurisdicción**, no la base del sistema.
2. **Adaptar a un país = crear un perfil, no tocar código.** El perfil se prepara con ayuda de los agentes de normativa y lo valida una persona.
3. **Resolver problemas, no crearlos.** Cada función existe porque quita trabajo a una persona real; valores por defecto sensatos, pocas decisiones obligatorias.
4. **Sin lenguaje técnico para el usuario.** El soporte lo da el proveedor; el personal de la institución no necesita saber de sistemas.
5. **Seguro sin que se note.** La seguridad exige más solo donde el riesgo es mayor.
6. **Todo respaldado por fuentes oficiales públicas y vigentes**, con cita. Nadie (ni el proveedor) decide una regla de memoria.

## 2. Perfil de jurisdicción

Un país (o región dentro de un país) se describe con un **perfil** que se carga como paquete normativo (`REGULACION-Y-CUMPLIMIENTO.md`). Contiene:

| Componente | Qué define | Ejemplos de variación |
|---|---|---|
| Niveles educativos | catálogo nacional y su equivalencia con **ISCED** (clasificación de UNESCO) | inicial, primaria, secundaria, bachillerato, superior |
| Estructura del año | tipo de periodos, número, fechas típicas, hemisferio | lapsos, trimestres, semestres, cuatrimestres |
| Escalas de evaluación | las que usa cada nivel, con aprobación y redondeo | letras, 0–20, 1–10, 0–100, promedio ponderado (GPA) |
| Régimen académico | promoción, repitencia, créditos o grados, materias pendientes | por grado o por créditos |
| Documentos y registros | tipos de constancias, certificados, boletines y sus campos obligatorios | varían por país |
| Identificación de personas | tipos de documento de identidad y su formato | cédula, DNI, pasaporte, otros |
| Autoridades y reportes | a quién se reporta, con qué formato y qué plazo | ministerio, secretaría, zona educativa, distrito |
| Fiscal y contable | impuestos, facturación, conservación de registros, monedas | según la autoridad tributaria |
| Protección de datos | normativa de datos personales y de menores, plazos de retención | RGPD, FERPA, leyes locales |
| Terminología | nombre local de cada concepto | «representante» o «tutor», «lapso» o «periodo» |
| Idioma, calendario y zona horaria | formatos de fecha, número y moneda | según la región |

El perfil de **Venezuela** es el primero. La genericidad del núcleo se prueba con **perfiles sintéticos antes de construir** (ver §6).

## 3. Qué significa «genérico» en el modelo de datos

- **Conceptos neutrales** en el código: `Guardian`, `Term`, `Section`, `Program`, `Scale`. El nombre que ve el usuario sale de la capa de terminología del perfil.
- **Identificación:** `identity_document(tipo, número, país)`. No hay un campo «cédula» fijo.
- **Direcciones, nombres y teléfonos:** formatos flexibles por país (varios apellidos, nombres compuestos, direcciones sin estado o provincia).
- **Niveles y programas:** cada uno mapea a ISCED. Los reportes comparables entre países usan esa clasificación.
- **Escalas, periodos, aprobación, tasas de impuestos, calendarios, documentos:** todo es dato del perfil, nunca una constante en el código.
- **Monedas:** ISO 4217 con tipo de cambio versionado; países, ISO 3166; idioma y formatos con las bibliotecas estándar del lenguaje (CLDR/ICU); zonas horarias IANA.
- **Sistema por créditos y por grados:** el modelo admite ambos (una materia aporta créditos o pertenece a un grado), sin forzar uno.
- **Los ejemplos de venezolanos** en los otros documentos son del perfil de Venezuela, no del núcleo.

Estándares públicos a tener en cuenta para intercambiar datos con otros sistemas: ISCED (UNESCO), ISO 4217, ISO 3166, ISO 8601, y como candidatos de interoperabilidad con aulas virtuales y sistemas de información estudiantil, OneRoster y Ed-Fi. Se evalúa su uso en la fase de integraciones.

## 4. Alta de un país nuevo con ayuda de los agentes

```
1. Mario o el proveedor elige el país
2. Una persona aporta las fuentes oficiales iniciales (ministerio, autoridad tributaria, diario oficial)
3. Los agentes (académico y fiscal) recolectan, leen y proponen un borrador de perfil con citas
4. Un revisor local (educador o administrador del país y un contador) valida y corrige
5. Se publica el perfil como paquete firmado
6. La primera institución cliente de ese país lo confirma antes de abrirlo a más
```
La IA reduce el trabajo, pero **en un país nuevo hace falta revisión de una persona que conozca ese sistema educativo**. Si las fuentes oficiales de un país no están en línea o no son claras, esa parte la investiga una persona.
Para cada perfil nuevo también se necesita la traducción de la interfaz y de las plantillas de documentos.

## 5. Facilidad de uso para personal no técnico

### Primer uso
- **Asistente de configuración inicial:** país → tipo de institución → niveles → escalas sugeridas por el perfil → calendario → sedes → primeros usuarios. Cada paso explica en palabras simples qué se decide y propone la opción recomendada. Meta: dejarlo funcionando en una tarde, no en una semana.
- **Modo guiado y modo avanzado:** lo avanzado queda oculto hasta que se necesita.
- **Importación desde Excel** con vista previa y corrección de errores fila por fila. Nadie debe escribir cien estudiantes a mano.
- **Instalación con datos de demostración** para capacitar al personal sin tocar datos reales.

### Día a día
- **Cada rol ve solo lo suyo,** con un panel de «qué tengo pendiente hoy» (notas por cargar, cobros por vencer, aprobaciones, casos).
- **Roles por cargo, no por permisos:** el administrador elige «Secretaria de control de estudios» o «Docente de matemáticas» y el sistema aplica el conjunto correcto. Los permisos finos quedan para el modo avanzado.
- **Lenguaje claro:** sin siglas técnicas. Los errores dicen qué pasó y qué hacer. Si algo está bloqueado, el sistema explica por qué y a quién pedirlo.
- **Confirmar lo irreversible, deshacer lo reversible,** y siempre una pantalla de resumen antes de cerrar un lapso o emitir un documento.
- **Búsqueda única** (por nombre, documento, sección) y accesos directos a las tareas más frecuentes.
- **Ayuda en contexto:** cada pantalla con un botón «¿cómo se hace?» (texto corto y video breve) y un botón «pedir ayuda» que envía al soporte lo que el usuario estaba viendo, con su permiso.
- **Móvil primero** para docentes (asistencia y notas), representantes y estudiantes; accesible según WCAG 2.2 nivel AA (contraste, teclado, lector de pantalla).
- **Funciona con mala conexión:** en red local sin internet; en el móvil, pantallas ligeras y avisos de «sin conexión».

### Seguridad sin fricción
- Inicio de sesión con correo o teléfono; recuperación de cuenta asistida por un administrador.
- **MFA** con aplicación o código por mensaje, obligatorio solo para los roles críticos (administración, finanzas, control de estudios, consejería); representantes y estudiantes con verificación por correo o mensaje.
- Sesiones largas en dispositivos de confianza para roles de bajo riesgo; cierre automático en equipos compartidos.
- Contraseñas largas permitidas y compatibles con gestores; sin reglas absurdas de caracteres.
- Acciones sensibles piden una confirmación extra explicada en lenguaje claro, no un código técnico.

### Soporte
- **Modo soporte con consentimiento:** el proveedor entra a ayudar solo con un permiso temporal que da el administrador; todo queda en la bitácora.
- **Diagnóstico sin datos personales** que el usuario envía con un clic.
- **Actualizaciones guiadas** con respaldo previo automático y reversa.
- Avisos de salud del sistema al proveedor (espacio, respaldos, errores), con consentimiento de la institución.

### Cómo se comprueba
Antes de cerrar cada fase se prueba con **personas reales** del cargo (un secretario, un docente, un representante) sin explicarles nada: se mide si completan las tareas clave y cuánto tardan.
Ejemplos de criterios: inscribir a un estudiante, cargar las notas de una sección, emitir una constancia, cobrar una mensualidad en dos monedas. Lo que no se completa sin ayuda se rediseña.

## 6. Cómo evitar sesgo hacia Venezuela

- **Prueba de genericidad con perfiles sintéticos** (no con países concretos): antes de las fases 1 y 2 se modela, sobre el papel, un conjunto de perfiles de prueba que estiran cada dimensión en la que los países pueden variar (año escolar, evaluación, estructura, documentos, identificación, fiscal, reportes, idioma). Ver `CONFIGURACION-GUIADA.md` §7. El modelo debe expresarlos todos sin cambiar código.
- Cada vez que se agrega una regla se pregunta: ¿es de todos los países o de uno? Si es de uno, va al perfil.
- Revisión de términos de documentos e interfaz: el núcleo no habla de «lapsos», «zona educativa» o «cédula» salvo a través de la terminología del perfil.

## 7. Sin experto del oficio ni país conocido

El equipo del proveedor no conoce la gestión educativa y no sabe qué país pedirá el software. Por eso:
- El **sistema guía al operador** con preguntas justificadas por la normativa (`CONFIGURACION-GUIADA.md`).
- Quien **confirma** cada respuesta es el personal de control de estudios o administración de la propia institución; esa es la validación del oficio en cada instalación.
- Si no existe un perfil para el país, el sistema funciona con un **perfil base genérico** y permite **solicitar el perfil**.
- Los perfiles nuevos se revisan con una persona local, normalmente la primera institución cliente de ese país.

## 8. Pendientes

1. Mario confirma el flujo de `CONFIGURACION-GUIADA.md`.
2. Idiomas: español e inglés desde el primer lanzamiento (decisión de Mario). Política en `ARQUITECTURA.md`, decisión 13.
