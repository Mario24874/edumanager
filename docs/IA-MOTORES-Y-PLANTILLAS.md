# Motores de IA elegibles y plantillas de documentos

Versión 0.1 · 1-nov-2026 · Estado: **diseño aprobado por Mario (1-nov)**; sin código. Amplía `ARQUITECTURA.md` §4 y `CONFIGURACION-GUIADA.md`.

## 1. Idea

La IA de EduManager (agentes para configurar, ajustar y analizar documentos) **no puede depender de una suscripción de pago**: muchas instituciones son públicas, de bajo presupuesto o no tienen medios de pago internacionales. Por eso el motor de IA es **elegible en la configuración inicial**, con opciones de pago, gratuitas, de código abierto, locales o alojadas donde viva el software. Sin motor, el sistema funciona completo.

Decisiones de Mario (1-nov-2026):
1. El sistema trabaja por **niveles de motor** y le dice a la institución qué esperar de cada uno.
2. Los agentes que tocan datos de la institución corren **dentro de su instalación**; las reglas de datos (C0–C3) no cambian según el motor.
3. Cada institución usa **su propio motor o su propia clave**; el proveedor no revende IA en esta versión.
4. Todo lo que la IA proponga, incluida la configuración, **lo revisa y aprueba una persona autorizada**, con vista previa y reversión.

## 2. Niveles de motor

| Nivel | Qué es | Infraestructura | Funciones razonables | Costo |
|---|---|---|---|---|
| **N0** | Sin IA | ninguna | todo manual: plantillas, configuración y perfiles se hacen a mano | ninguno |
| **N1** | Modelo pequeño de código abierto en el servidor de la institución, más herramientas que no son modelos de lenguaje para leer documentos (OCR y análisis de diseño y tablas) | servidor común, sin GPU | leer formatos y proponer plantillas básicas; responder preguntas de configuración con apoyo de texto | ninguno recurrente |
| **N2** | Modelo mayor de código abierto, con visión si hace falta | servidor con GPU, o un VPS de la institución o del proveedor | análisis de documentos complejos, propuestas de configuración más precisas | infraestructura |
| **N3** | API de pago en la nube | internet y clave propia | mejor calidad, sin hardware propio | por uso, a cargo de la institución |

- Los modelos concretos recomendados se mantienen en un **catálogo aparte** que se revisa periódicamente; este documento no fija nombres ni versiones, porque envejecen rápido.
- Opción intermedia: **planes gratuitos de proveedores en la nube**, con las restricciones de §4.
- El motor se conecta por una interfaz común (`AiProvider`), con la dirección y la clave configurables. Un motor local puede estar en el mismo servidor, en otro equipo de la red de la institución o en un VPS.

### Prueba de capacidad
Al elegir o cambiar de motor, el sistema ejecuta una **prueba corta** con tareas de muestra (leer un formato en blanco, proponer una plantilla, contestar una pregunta de configuración) y muestra, por función, un resultado en lenguaje simple: **recomendado · usar con revisión cuidadosa · no recomendado con este motor**. La institución decide con esa información. Si cambia el motor, la prueba se repite.

## 3. Dónde corre cada agente y qué ve

| Agentes | Dónde corren | Datos que ven |
|---|---|---|
| **Normativa** (`REGULACION-Y-CUMPLIMIENTO.md`) | servicio del proveedor | solo documentos públicos; ningún dato de estudiantes |
| **Configuración, plantillas y redacción** (este documento) | **dentro de la instalación de la institución** | formatos y documentos que el usuario sube; datos del sistema solo según los permisos de quien lo pide |

Reglas de datos por tipo de motor (no se relajan por comodidad):

| Motor | Datos C0–C1 | Datos C2 (académico confidencial) | Datos C3 (restringido) |
|---|---|---|---|
| Local en el servidor o en la red de la institución | sí | solo con los permisos del usuario que lo pide | **nunca** |
| VPS u otra infraestructura alojada por un tercero | sí | solo seudonimizados y con autorización expresa de la institución | **nunca** |
| API de pago en la nube | sí | solo seudonimizados y con autorización expresa de la institución | **nunca** |
| Plan gratuito en la nube | solo formatos en blanco y datos públicos | **nunca** | **nunca** |

- El C3 queda fuera de cualquier motor, incluso local (decisión conservadora de este diseño; revisable con la primera institución).
- Los planes gratuitos suelen permitir que el proveedor use los datos y tienen límites de uso: la pantalla de configuración lo avisa antes de activarlos.
- Si un documento subido parece contener datos de personas, el sistema lo avisa y ofrece ocultarlos antes de enviarlo a un motor que no sea local.

## 4. Quién paga

- **Clave propia (BYOK):** cada institución pone su motor local o su clave de API. El proveedor no revende ni cobra IA en esta versión.
- El **tope de costo** del diseño se aplica a la clave de la institución; para motores locales se limita el uso simultáneo y se registra el consumo.
- Cada uso queda registrado: usuario, función, motor y versión del modelo, y quién aprobó el resultado.

## 5. Configuración y ajustes con IA

Aplica lo ya definido en `CONFIGURACION-GUIADA.md`, con estas precisiones:

1. El agente lee las normas y documentos que se le pasen (reglamento interno, resoluciones, normativa oficial) y **propone** configuraciones y ajustes, cada uno con su cita y su impacto.
2. **La calidad de la interpretación depende del motor.** Cada propuesta muestra qué motor y modelo la generó y, según la prueba de capacidad, un aviso de «revisar con cuidado» cuando corresponde.
3. Una **persona autorizada** (nivel A4 u otro que la institución defina según el riesgo) revisa y aprueba. Nada se aplica sin esa aprobación.
4. La persona puede **pedir cambios en lenguaje natural** («cambia el lapso 2 para que cierre el 15», «usa la escala de 1 a 10 en primaria»). El agente rehace la propuesta; sigue sin aplicarse hasta que se apruebe.
5. Antes de aplicar se muestra una **vista previa** con datos ficticios; después, cada cambio queda **versionado y reversible**, con quién lo aprobó.
6. Sin cita que lo respalde, el cambio se marca como «decisión propia de la institución» (regla existente).

## 6. Plantillas de documentos editables

Objetivo: que la institución pueda subir un documento o una imagen de lo que debe entregar a otras instituciones o entes oficiales y obtener un **modelo editable que reutiliza cada vez**: informes, reportes, notificaciones, comunicados, constancias, oficios.

### Flujo
1. **Subir** un PDF, imagen, hoja de cálculo o documento de texto (un formato oficial, un documento anterior o un ejemplo).
2. **Lectura.** Herramientas de OCR y análisis de diseño más el modelo de IA identifican la estructura: encabezado, logo, textos fijos, campos variables, tablas, firmas y sellos, pie de página, estilos.
3. **Borrador de plantilla.** El sistema construye un modelo editable y lo muestra **al lado del original** para comparar.
4. **Edición por una persona.** La institución cambia textos, sube su logo, reordena, corrige y marca qué es fijo y qué es un campo variable. Puede pedirle ajustes a la IA en lenguaje natural.
5. **Vincular campos.** Cada campo variable se asocia con un dato del sistema (por ejemplo nombre de la institución, periodo, matrícula total), dentro del catálogo permitido por los permisos de quien usa la plantilla.
6. **Aprobar.** Una persona autorizada (A4) aprueba la plantilla; queda **versionada**.
7. **Usar siempre que se requiera.** Se elige la plantilla y se genera el documento con los datos del sistema; se puede editar antes de enviarlo y se descarga en **PDF y en formato editable**.

### Reglas
- **Los datos los inserta código determinista**, no la IA. La IA interviene al analizar el formato y, si se pide, al **redactar borradores de texto libre** que una persona edita.
- La IA propone la plantilla; **no se promete una copia exacta del original**: la fidelidad depende del motor y de la calidad del escaneo, y siempre pasa por revisión humana.
- Distinción importante: la **plantilla de documento** es el aspecto del documento (informe, comunicado). El **perfil de exportación** (`ARQUITECTURA.md` §4) define los datos y columnas que se entregan a la autoridad educativa. Pueden usarse juntos, pero son cosas distintas.
- Sin motor de IA (N0), el editor de plantillas funciona igual: se construyen a mano.

## 7. Impacto en las fases (propuesta mía, sin aprobar)

- El **editor de plantillas sin IA** nace junto con los boletines (fase 3) y se reutiliza para constancias, comunicados e informes.
- La **importación con IA desde PDF o imagen**, la selección de motor y la prueba de capacidad llegan en la fase 7, junto con la IA conectable.
- El catálogo de modelos recomendados se define antes de la fase 7, con pruebas reales sobre formatos de la autoridad educativa venezolana.

## 8. Riesgos y puntos abiertos

- **Calidad variable.** Un modelo pequeño leerá peor un documento complejo. Mitigación: la prueba de capacidad, la revisión humana y las herramientas de OCR y diseño.
- **Hardware insuficiente** en instituciones pequeñas: el nivel N1 debe ser útil en un servidor sin GPU; hay que medirlo con pruebas reales.
- **Formato de almacenamiento de la plantilla** (documento propio con marcadores, HTML renderizado a PDF, u otro) y el editor visual: decisión técnica para la fase 3, del constructor.
- **Licencias de los modelos de código abierto:** algunas limitan el uso comercial; revisar cada modelo del catálogo.
- **Datos de menores en motores locales:** la validación legal pendiente (`ARQUITECTURA.md` §5) también cubre este uso.
- **C3 fuera de todo motor:** puede revisarse más adelante si una institución lo necesita, con su propia autorización escrita.
