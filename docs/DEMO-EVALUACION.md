# Demo de evaluación

Versión 0.1 · 2-nov-2026 · Estado: **diseño aprobado por Mario (2-nov)**; sin código. Complementa `GENERICO-Y-USABILIDAD.md` (la instalación con datos de demostración para capacitar es otra cosa) y `ARQUITECTURA.md`.

## 1. Para qué sirve

Nadie compra un sistema de gestión educativa sin verlo funcionar. La demo es la **primera impresión y la principal herramienta de venta**: una institución debe poder probar EduManager en minutos, sin instalar servidores ni comprometer recursos, y salir pensando «esto resuelve lo mío».

Decisiones de Mario (2-nov-2026):
1. Se ofrecen **dos formas**: una versión en línea y un paquete descargable.
2. El primer evaluador a conquistar es el **director o administrador**, no el técnico.
3. La IA se muestra con **ejemplos ya preparados**, sin modelos pesados.
4. Sin registro para la versión en línea; correo opcional para la descarga; sin telemetría sin consentimiento.

## 2. Dos formas de probarla

| Forma | Para qué | Cómo funciona |
|---|---|---|
| **En línea** | primera impresión inmediata y enlace fácil de compartir entre directivos | abre con un clic, sin registro, con datos ficticios que se restablecen solos |
| **Descargable** | probarla en la propia máquina, sin internet, y ver que el producto se instala y corre en equipos comunes | paquete portable que se abre con doble clic y lanza el navegador; todo incluido |

### Paquete descargable
- Un solo archivo comprimido desde una URL pública; **ligero** (meta de tamaño a medir en la fase de empaquetado).
- Incluye su propio entorno de ejecución y una **base de datos ligera embebida**: no pide PostgreSQL, Docker ni conocimientos técnicos.
- Windows primero; luego macOS y Linux.
- Verificable: se publica su suma de verificación y, cuando sea posible, firmado.
- Los datos de la demo viven en una carpeta propia; borrarla la deja limpia. No toca nada más del equipo.
- Las dos formas usan **el mismo producto y el mismo conjunto de datos**, para que lo visto en línea sea lo que se descarga.

## 3. Experiencia de usuario: los primeros 60 segundos

Lo que más cuenta. Principios:
- **Resultado antes que menús.** En la primera pantalla se ve algo valioso, no una lista de módulos.
- **Cero fricción:** sin registro, sin configuración, sin manual.
- **Sin vacíos:** nunca una pantalla vacía; todo viene con datos creíbles.
- **Una historia, no un catálogo:** recorridos cortos tipo «un día en la vida».
- **Lenguaje simple**, sin tecnicismos; español e inglés, con el idioma detectado y cambiable.
- **Funciona en celular:** muchos directivos la verán primero desde el teléfono.
- **Rápida:** cargas casi inmediatas; si algo tarda, se muestra progreso.

### Apertura
Una pantalla de bienvenida con la pregunta **«¿Cómo quiere verlo?»** y tres caminos:

| Camino | Qué vive la persona (ejemplos de «momentos de valor») |
|---|---|
| **Director o administrador** (el principal) | un panel con la institución al día: alertas de inasistencia, cierres de lapso pendientes, mora; un informe oficial listo para enviar; el cambio de escala de notas sin tocar código |
| **Docente** | cargar notas de su sección y ver el boletín generado; su horario sin choques |
| **Representante** | ver las notas, inasistencias y pagos de sus hijos; recibir un aviso |

Cada camino es un **recorrido guiado y corto** (unos pocos pasos), con una forma clara de salir a explorar libremente y volver. Un indicador discreto muestra que es una demo con datos ficticios.

### Momentos que hay que diseñar (los «¡ajá!»)
1. **Boletín listo:** del registro de notas al PDF en un clic.
2. **Horario sin choques:** generado por el solver ante los ojos del usuario.
3. **Cambio de configuración:** elegir otra escala o idioma y ver al instante cómo cambia el boletín.
4. **Plantilla desde un documento:** subir un formato de muestra y ver la plantilla editable (con IA, ver §5).
5. **Alerta útil:** una inasistencia que genera un aviso al representante.

## 4. Datos y contenido

- Una **institución ficticia creíble** (con logo, sedes, niveles, docentes, estudiantes y representantes de mentira), en el idioma y perfil de país elegidos. Nada de datos reales, nunca.
- Datos pensados para contar una historia: un estudiante con riesgo de reprobar, un pago vencido, un docente con conflicto de horario.
- Se puede **restablecer** la demo a su estado inicial con un botón.
- En línea, cada visitante trabaja en su propia copia temporal, para que nadie vea ni dañe lo de otro.

## 5. Qué hace la IA en la demo

- Sin modelo pesado: la demo trae **resultados de IA ya preparados** (por ejemplo, una plantilla creada a partir de un formato de muestra, o una propuesta de configuración con su cita) para que se vea el efecto sin instalar nada.
- Se marca claramente cuándo algo es un ejemplo preparado, para no prometer más de lo que hay.
- Opcional: conectar un motor propio para probar con documentos reales de la institución (`IA-MOTORES-Y-PLANTILLAS.md`). Las reglas de datos de ese documento aplican igual.

## 6. Límites de la demo

- Datos de muestra; **sin exportación oficial real** a la autoridad educativa.
- No caduca, pero no sirve para operar: no hay respaldos ni usuarios reales ni correo saliente real.
- Las funciones que requieren la instalación completa se muestran como vista previa, con la indicación de cómo se obtienen.
- La demo termina con una invitación clara y sin presión: dejar un contacto, pedir una conversación o descargar el paquete.

## 7. Privacidad y medición

- **Sin telemetría por defecto.** Toda medición (qué recorrido se usó, dónde se abandona) es opcional y con consentimiento visible.
- La versión en línea no pide registro. La descarga puede pedir un **correo opcional** para seguimiento, con aviso de uso claro.
- Ningún dato personal se guarda en la demo; lo ficticio se genera.

## 8. Cómo se prueba la experiencia

- **Prueba de los 60 segundos:** alguien que no conoce el producto debe poder, en un minuto y sin ayuda, saber qué hace y llegar a un primer momento de valor.
- Revisión en celular y en un equipo modesto.
- Prueba con personas del perfil de director o administrador antes de publicarla.
- Medición de tamaño de la descarga y tiempo hasta la primera pantalla.

## 9. Impacto en las fases (propuesta mía, sin aprobar)

- **Fase 2:** primera versión **en línea** (ya hay un alumno de prueba con tres escalas e historial; es la demo del portafolio), con la apertura por camino y los primeros «¡ajá!».
- **Fase 3:** el boletín, el horario sin choques y las plantillas añaden los momentos de más impacto.
- **Después de la fase 3:** el **paquete descargable** portable, cuando haya suficiente producto que mostrar.
- **Fase 7:** los ejemplos de IA preparados pasan a ser reales con un motor conectado.
- El diseño de la apertura y del recorrido del director se prueba con maquetas **antes** de programarlos.

## 10. Puntos abiertos

- Entorno de ejecución del paquete portable y su tamaño final (decisión técnica del constructor).
- Dónde se aloja la descarga y la versión en línea, y cómo se limitan los abusos (ancho de banda, uso excesivo).
- Si la versión en línea corre en el VPS actual o en un servicio aparte, según el espacio y la carga.
- Qué perfil de país muestra por defecto la demo y cómo se elige el idioma.
- Mockups de la apertura y del recorrido del director para aprobar antes de construir.
