# Configuración guiada por el perfil de país

Versión 0.1 · 31-oct-2026 · Estado: **planificación**. Complementa `GENERICO-Y-USABILIDAD.md` y `REGULACION-Y-CUMPLIMIENTO.md`.

## 1. Problema

Nadie en el equipo del proveedor conoce la gestión educativa, no sabemos qué país pedirá el software, y el personal de cada institución no es técnico.
Por eso **el sistema debe preguntar al operador, paso a paso y en palabras simples, todo lo que necesita para configurarse conforme a la regulación**, y justificar cada pregunta con la fuente oficial.
El **perfil de país** es el mapa: dice qué hay que preguntar, qué opciones permite la ley, qué valores por defecto usar y de dónde sale cada regla.

## 2. El perfil declara sus requisitos

Además de reglas (escalas, periodos, impuestos), cada perfil trae un **cuestionario de configuración** (`config_requirements`):

```
config_requirement
  id, section              (institución, niveles, calendario, evaluación, documentos, finanzas, usuarios, reportes)
  question_text            (en lenguaje simple, es/en)
  why_text                 («Se pregunta porque…» con la cita: documento, artículo, enlace)
  input_type               (elegir una, elegir varias, número, fecha, texto, archivo, sí/no)
  allowed_values           (las que permite la normativa)
  default_value            (el que fija la normativa o el más común, con su fuente)
  validation               (reglas: rangos, formatos, coherencia con otras respuestas)
  depends_on               (solo se pregunta si otra respuesta lo requiere)
  evidence_required        (p. ej. subir la autorización de funcionamiento o el reglamento interno)
  risk_level               (bajo/medio/alto: define cuánta confirmación pide)
  effects                  (qué configura: escalas, plantillas, calendario, formato de reporte…)
```

Así el cuestionario cambia con el país sin tocar el programa, y cuando la normativa cambia solo se preguntan las **preguntas nuevas**.

## 3. Cómo es la experiencia

1. **Selección de país.** Si existe el perfil, se carga. Si no existe, ver §6.
2. **Preguntas por secciones**, una idea por pantalla: «¿Qué tipo de institución es?», «¿Qué niveles ofrece?», «¿Cuántos periodos tiene su año escolar?», «¿Cómo califican en cada nivel?».
   Cada una muestra: la opción recomendada, una explicación breve, y un enlace «¿por qué me lo preguntan?» con la cita oficial.
3. **Respuestas inteligentes.** Si la normativa solo permite una opción, ya viene elegida. Si permite varias, se muestran con un ejemplo de cada una.
4. **«No lo sé».** Siempre existe. El sistema usa el valor por defecto de la normativa y lo marca como **pendiente de verificación**; esos pendientes aparecen en el panel de cumplimiento y bloquean solo las acciones que dependen de ellos (por ejemplo, no se puede cerrar un lapso si falta confirmar su fecha).
5. **Documentos propios.** El operador puede subir el reglamento interno de la institución o resoluciones de su autorización; el agente propone respuestas a partir de ellos y el operador las confirma una por una. La IA explica y propone; **no decide**.
6. **Vista previa antes de aplicar.** El sistema muestra cómo quedarían, con datos ficticios, un boletín, un historial y un horario con esa configuración. Quien no conoce la materia ve el resultado, no una lista de parámetros.
7. **Resumen firmado.** Al terminar se genera un informe con todas las respuestas, sus fuentes y los pendientes; lo aprueba el administrador. Queda como constancia de quién configuró qué y con qué base.
8. **Cambios posteriores.** Cuando llega un paquete normativo nuevo, el sistema muestra solo lo que cambia y pregunta lo necesario.

## 4. Panel de cumplimiento

Un tablero en lenguaje simple: qué está configurado y con qué fuente, qué está pendiente de verificar, qué cambió en la normativa y cuándo vence cada plazo (calendario escolar, entregas de reportes, obligaciones fiscales). Para cada pendiente: qué falta, por qué importa y cómo resolverlo.

## 5. Sin un experto del oficio

No hay un asesor del dominio en el equipo, así que el diseño no depende de uno:
- Las reglas salen de **fuentes oficiales con cita**; el sistema nunca configura algo sin respaldo escrito. Si no hay fuente, lo dice y pide la decisión de la institución, dejándola marcada como «decisión propia».
- **Valores conservadores por defecto** (los de la norma) y confirmaciones adicionales en lo de riesgo alto (cierres, notas, documentos oficiales, pagos).
- **Vista previa y simulación** antes de aplicar.
- **Comprobaciones de coherencia** (por ejemplo, que la escala elegida exista para ese nivel, que los periodos cubran el año, que cada materia tenga docente y aula).
- **Escalamiento:** el botón «pedir ayuda» envía al soporte la pregunta y su contexto; las preguntas sin respuesta clara pasan a revisión humana del proveedor.
- Cada institución tiene a su propio personal de control de estudios o administración: son quienes **confirman** las respuestas. Su revisión es la validación del oficio en cada instalación, y las preguntas dejan claro quién respondió.
- Los perfiles de país nuevos siguen necesitando una revisión humana local antes de publicarse (`REGULACION-Y-CUMPLIMIENTO.md` §9). Se resuelve con la primera institución cliente de ese país, sin pagar un asesor aparte.

## 6. País sin perfil

No sabemos qué país lo pedirá, así que el sistema funciona **sin perfil**:
- **Perfil base genérico:** sin normativa asociada. El asistente pregunta lo mínimo (niveles, periodos, escalas, monedas, idioma) y marca todo como «decisión propia de la institución»; no promete cumplimiento legal.
- **Solicitar perfil:** un botón envía al proveedor la solicitud de un perfil para ese país. El proveedor lo prepara con los agentes y un revisor local (`GENERICO-Y-USABILIDAD.md` §4) y lo entrega como paquete.
- Al instalar el perfil, el asistente retoma y pregunta solo lo que falte.

## 7. Cómo se prueba que es genérico (sin elegir países)

Se construye un conjunto de **perfiles sintéticos de prueba** que estiran el modelo en cada dimensión en la que los países pueden variar:

| Dimensión | Variaciones que deben convivir |
|---|---|
| Año escolar | de enero a diciembre; de septiembre a junio; hemisferio sur; trimestres, semestres, cuatrimestres, lapsos |
| Evaluación | letras, 0–20, 1–10, 0–100, promedio ponderado (GPA) con créditos, aprobado/reprobado, descriptiva |
| Estructura | por grado, por créditos, mixta; materias comunes y optativas |
| Documentos | distintos campos obligatorios, firmas y sellos |
| Identificación | uno o varios documentos, con formatos diferentes; nombres con uno o varios apellidos |
| Fiscal | con y sin impuesto sobre ventas, con y sin factura obligatoria, varias monedas |
| Reportes | con autoridad central, descentralizados, o sin reporte obligatorio |
| Idioma | español, inglés, otros por añadir |

El modelo debe poder expresar todos los perfiles sintéticos **sin cambiar código**. Cada fallo señala un lugar donde el núcleo es menos genérico de lo debido, y se corrige antes de la fase 1.

## 8. Idiomas

Ver `ARQUITECTURA.md` (decisión 13): inglés como idioma de trabajo del código y de la documentación técnica; la interfaz y los documentos para el usuario, en español e inglés desde el primer lanzamiento. El cuestionario y las explicaciones de cada perfil se escriben en ambos idiomas.

## 9. Pendientes

1. Mario revisa este flujo y confirma que describe la experiencia que quiere.
2. Definir el catálogo inicial de preguntas del perfil de Venezuela a partir de la normativa oficial (lo prepara el agente académico en R2, o a mano en R0).
