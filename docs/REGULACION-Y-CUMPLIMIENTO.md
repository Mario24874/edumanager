# Módulo `regulatory`: normativa académica y fiscal

Versión 0.1 · 31-oct-2026 · Estado: **planificación** (propuesta de Mario). No se implementa hasta cumplir la condición de arranque de `ARQUITECTURA.md` §7.

## 1. Objetivo

Que las reglas académicas, fiscales y administrativas del sistema **provengan de la normativa oficial publicada y se mantengan al día**, con cita de la fuente,
en lugar de depender de lo que alguien recuerde o de lo que se codificó una vez. Las leyes, reglamentos y resoluciones son públicas (las publican los entes rectores):
el Ministerio de Educación (y el de Educación Universitaria, a confirmar cuáles aplican) para lo académico, el SENIAT para lo fiscal, y la Gaceta Oficial como fuente de vigencia.

## 2. Qué decide cada parte

| Quién | Hace | No hace |
|---|---|---|
| **Agentes de IA especialistas** | Vigilan las fuentes, detectan cambios, leen los textos, proponen «candidatos de regla» con la cita exacta | Nunca cambian la configuración de una institución por sí solos |
| **Revisor humano** (jurídico, control de estudios, contador) | Aprueba, corrige o rechaza cada candidato | — |
| **Paquete normativo firmado** | Lleva las reglas aprobadas, versionadas y con citas, a cada instalación | No se aplica sin que el administrador de la institución lo apruebe |
| **Administrador de la institución** | Revisa las diferencias, fija la fecha de vigencia y aplica | — |

## 3. Dos agentes especialistas

| Agente | Corpus | Reglas que propone (ejemplos) |
|---|---|---|
| **Normativa académica** | leyes de educación, reglamentos, resoluciones de evaluación por nivel, control de estudios, calendario escolar oficial, expedición de documentos, normativa universitaria | parámetros de `grading_scale` (rango, nota aprobatoria, redondeo), reglas de promoción y repitencia, número de lapsos, calendario, documentos obligatorios, plazos de entrega a la zona educativa, formato de reportes |
| **Fiscal, administrativa y contable** | código tributario, leyes y providencias del SENIAT (facturación, IVA, impuestos sobre divisas, retenciones), normas contables aplicables | parámetros de `tax_rule`, requisitos de recibos y facturas, obligaciones de registro y conservación, tipos de cambio oficiales y su fuente |

Los dos corpus se arman a mano con los documentos oficiales (URLs, PDF, Gaceta Oficial) y se amplían con el tiempo. La lista concreta de documentos
y la vigencia de cada uno **la confirma una persona**, no el agente.

## 4. Base de conocimiento, no «entrenamiento»

- No se hace *fine-tuning*. Se usa **recuperación de información con citas**: los documentos se almacenan, se parten por artículo y se indexan (PostgreSQL con `pgvector`, la misma base del producto).
- Cada respuesta o regla propuesta debe llevar **la cita textual**, el documento, el artículo, la fecha de publicación y la vigencia. Si el agente no encuentra un fragmento que lo respalde, **descarta** el candidato.
- Un paso de **verificación** independiente comprueba que la cita dice lo que el candidato afirma.
- Hay un conjunto de **pruebas con reglas ya conocidas** (por ejemplo, la escala de una etapa) que se ejecuta cada vez que cambia el modelo, el corpus o las instrucciones, para detectar regresiones.
- Se registra y se señala: vigencia, derogaciones, conflictos entre fuentes, y fuentes que dejaron de estar disponibles.

## 5. Flujo (LangGraph)

El flujo de agentes se modela como un grafo con **LangGraph**. Es la herramienta adecuada para pasos con estado, reintentos y revisión humana en el medio.

```
programación periódica
   → recolectar fuentes (copia cruda + huella + fecha)
   → ¿cambió? (comparación)               ── no → fin
   → clasificar relevancia (¿afecta alguna regla del sistema?)
   → recuperar contexto del corpus
   → extraer candidatos de regla (salida estructurada)
   → verificar cita ↔ afirmación           ── falla → descartar y registrar
   → cola de revisión humana               ── aprobar / corregir / rechazar
   → generar paquete normativo versionado y firmado
   → publicar
```

**Dónde corre.** Es un servicio aparte en Python (LangGraph) que opera **del lado del proveedor**, no dentro de cada instalación:
- Es la excepción justificada al monolito: las herramientas de agentes están en Python y el servicio no toca datos de estudiantes.
- Las instalaciones solo reciben el **paquete normativo** ya validado. Funciona igual en servidores sin internet, importando el paquete desde un archivo.
- Ahorra costo: un solo ciclo de análisis atiende a todas las instituciones, en lugar de uno por institución.

## 6. Paquete normativo

```
regulation_pack            (versión, país, autoridad, fecha de publicación, firma)
  rule                     (tipo: GRADING_SCALE | PROMOTION | TAX | CALENDAR | DOCUMENT | EXPORT_FORMAT, parámetros JSON,
                            vigente_desde, vigente_hasta)
    citation               (documento, artículo, texto, URL, fecha de captura, huella)
  reviewed_by, reviewed_at, notes
```
- La institución ve un **resumen de diferencias** respecto a lo que tiene (qué cambia, cuál es la cita) y elige la fecha de vigencia.
- Al aplicar, el sistema actualiza `grading_scale`, `tax_rule`, `export_profile`, calendario y plantillas, **con la procedencia** (paquete, regla, cita).
- Cada configuración muestra de dónde viene («Resolución X, art. Y») y puede volver a la versión anterior.
- Una institución puede **sobrescribir** una regla cuando la normativa lo permite (por ejemplo una escala propia en una universidad); la sobrescritura queda marcada y justificada.

## 7. Seguridad y límites

- El servicio de normativa **no recibe datos personales ni de estudiantes**, solo documentos públicos.
- Lista cerrada de fuentes (dominios oficiales aprobados); nada se descarga de fuera de esa lista.
- Tope de costo, programación fija y registro de cada ejecución. Nada se ejecuta de forma automática fuera de ese calendario.
- Paquetes firmados; la instalación verifica la firma antes de ofrecerlos.
- El sistema indica con claridad que es una **ayuda de referencia** y no asesoría legal ni contable: la responsabilidad de aplicar una regla es de quien la aprueba.
- Antes de producción: un abogado revisa el proceso y un contador revisa el módulo fiscal.

## 8. Fases del módulo

| Fase | Entrega |
|---|---|
| R0 | Esquema de paquete normativo + importación **manual** con citas (sin IA). La configuración de escalas de la fase 2 ya nace con procedencia. |
| R1 | Registro de fuentes y recolector con detección de cambios y alertas a un revisor |
| R2 | Agente académico (LangGraph) con verificación de citas y pruebas con reglas conocidas |
| R3 | Agente fiscal, administrativo y contable |
| R4 | Distribución firmada, importación sin internet y aplicación con diferencias en el panel de administración |

R0 va junto con la fase 2 del producto (motor de notas). R1–R4 después de la fase 5.

## 9. Alta de un país nuevo
El mismo mecanismo sirve para otros países: una persona aporta las fuentes oficiales iniciales (ministerio, autoridad tributaria, diario oficial), los agentes proponen un borrador de **perfil de jurisdicción** con citas, y un revisor local lo valida antes de publicarlo. Ver `GENERICO-Y-USABILIDAD.md` §4. Los agentes no sustituyen a alguien que conozca el sistema educativo del país.

## 10. Relación con la configuración guiada
Cada regla del paquete alimenta el cuestionario del asistente de `CONFIGURACION-GUIADA.md`: pregunta, explicación, cita y valores permitidos. Así el operador no necesita conocer la normativa para configurar conforme a ella.

## 11. Pendientes de definir con Mario

1. Lista inicial de documentos oficiales por agente, con su URL y vigencia (la arma una persona; el agente no decide qué es oficial).
2. Quién será el **revisor humano** de cada agente (jurídico, control de estudios, contador).
3. ¿Qué ministerios aplican a las instituciones objetivo (educación básica, educación universitaria)?
4. Dónde se ejecuta el servicio del proveedor (mismo VPS u otra infraestructura) y cómo se firman y distribuyen los paquetes.
