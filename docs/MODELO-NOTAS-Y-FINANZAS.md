# Modelo de datos: evaluación multiescala y finanzas multimoneda

Esquema lógico (PostgreSQL). Los nombres son propuestas; se afinan con Flyway en las fases 2 y 4.
Todas las tablas llevan `institution_id`, `created_at`, `created_by`, y las de cambio sensible tienen versión.

## A. Evaluación con varias escalas

### Principio
Se guarda **siempre el valor original en su propia escala**. Nunca se convierte sobre el dato. Para comparar o rankear entre etapas
existe un **valor normalizado** (0–100) calculado con una regla versionada, y se usa solo en reportes. El historial oficial muestra el valor original.

### Escalas
```
grading_scale
  id, institution_id, code, name
  kind            NUMERIC | LETTER | DESCRIPTIVE
  min_value, max_value, step, decimals          -- solo NUMERIC
  pass_value                                    -- nota mínima aprobatoria
  rounding_mode   HALF_UP | HALF_DOWN | FLOOR | CEILING
  active

grading_scale_level                             -- solo LETTER / DESCRIPTIVE
  id, scale_id, code ('A','B',...), label, ordinal
  equiv_min, equiv_max                          -- rango numérico opcional para normalizar
  passing

program_scale                                   -- qué escala usa cada programa/nivel y desde cuándo
  id, program_id (o level_id), scale_id, valid_from, valid_to
```
**Ejemplos del perfil de Venezuela** (valores concretos a confirmar con la normativa vigente; otros países cargan sus propias escalas, incluso con promedio ponderado o créditos):

| Etapa | Tipo | Rango | Notas |
|---|---|---|---|
| Inicial | descriptiva | niveles con etiqueta | sin número |
| Primaria | letras | A–E (o la que defina la institución) | cada letra con etiqueta |
| Media general | numérica | 01–20 | aprobatoria configurable |
| Universidad | numérica | 1–10 o 1–20 según la institución | cada institución define la suya |

### Notas
```
assessment
  id, section_course_id, term_id, kind, name, weight, date

grade_entry
  id, enrollment_id, assessment_id, scale_id
  status          GRADED | NOT_EVALUATED | EXEMPT | WITHDRAWN
  raw_numeric     numeric(7,3) NULL
  raw_level_id    NULL → grading_scale_level
  normalized_pct  numeric(6,3)                  -- 0..100, calculado, nunca editable a mano
  version, supersedes_id, change_reason
  recorded_by, recorded_at, approved_by, approved_at
  CHECK: si status = GRADED, exactamente uno de raw_numeric / raw_level_id; si no, ninguno
  CHECK: raw_numeric entre min_value y max_value de su escala (validado en servicio y por trigger)
```
Un cambio de nota **no actualiza** la fila: crea una versión nueva (`supersedes_id`) con motivo, autor y, si el lapso está cerrado, aprobación de un nivel de autoridad superior.

### Resultados por lapso y por materia
```
term_result
  id, enrollment_id, course_id, term_id, scale_id
  final_numeric / final_level_id, final_status  APPROVED | FAILED | PENDING | EXEMPT | WITHDRAWN
  closed_at, closed_by
```

### Historial académico de un alumno que pasó por todas las etapas
Vista `academic_record` que une, por estudiante y en orden cronológico: año escolar, programa, materia, lapso, valor original, escala, estado y valor normalizado.
Responde de una sola consulta: «todas las notas de esta persona desde primaria hasta la universidad», cada una en su escala.

### Conversión
```
scale_conversion
  id, from_scale_id, to_scale_id, method LINEAR | TABLE, effective_from, approved_by

scale_conversion_point                          -- si method = TABLE
  conversion_id, from_value_or_level, to_value
```
- `LINEAR`: `(valor − mín) / (máx − mín) × 100`. Para letras se usa el punto medio de su rango.
- Una conversión nueva no reescribe resultados antiguos: cada `grade_entry` conserva con qué versión se normalizó, y se puede recalcular en bloque con aprobación.

### Casos especiales a contemplar
No evaluado, retirado, exonerado, equivalencias y revalidaciones entre instituciones, repitencia, materias pendientes, reparaciones y recuperaciones, cambio de escala a mitad de trayectoria.

### Exportación a la autoridad educativa
```
export_profile
  id, authority_name, version, format CSV | XLSX | PDF | XML | API
  mapping (JSONB): columna destino ← campo origen + transformación
  source_document_id                            -- el documento de referencia que se analizó
  approved_by, approved_at
```
Los datos exportados son los **originales**. La IA solo ayuda a crear y proponer el `mapping` a partir del documento de la autoridad (ver `ARQUITECTURA.md` §4).

## B. Finanzas multimoneda

### Principios
- Cada importe lleva siempre **monto + moneda**. Sin monedas implícitas.
- El **tipo de cambio es un dato versionado**, con fuente (por ejemplo el oficial del Banco Central o ingreso manual) y fecha. Un pago congela el tipo de cambio que se aplicó.
- Los cobros pueden definirse en una moneda y pagarse en otra. Los saldos se llevan en la moneda del cargo y en la moneda base de la institución.
- Contabilidad de **doble partida**: cada pago, cargo, donación o descuento genera asientos que cuadran.

```
currency
  code (ISO 4217), name, decimals, is_base_for_institution

exchange_rate
  id, from_currency, to_currency, rate numeric(18,8)
  source        OFFICIAL | MANUAL | API
  effective_at, entered_by
  UNIQUE (from_currency, to_currency, effective_at)

fee_concept                                     -- matrícula, mensualidad, uniforme, actividad, etc.
  id, name, default_amount, currency, recurrence, tax_rule_id

student_account
  id, student_id, guardian_id (responsable de pago), status

charge
  id, account_id, concept_id, amount, currency, due_date, status OPEN | PARTIAL | PAID | CANCELLED
  discount_id / scholarship_id

payment
  id, account_id, amount, currency, method CASH | TRANSFER | CARD | MOBILE_PAY | OTHER
  paid_at, reference, receipt_no
  rate_id                                       -- tipo de cambio aplicado
  base_equivalent numeric(18,2)                 -- equivalente en la moneda base en ese momento
  status RECEIVED | VERIFIED | REVERSED

payment_allocation
  payment_id, charge_id, amount_in_charge_currency, rate_used

contribution                                    -- donaciones, aportes y contribuciones, con recibo
  id, donor_name/id, kind DONATION | CONTRIBUTION | SPONSORSHIP, amount, currency, receipt_no

tax_rule                                        -- configurable por país; se valida con un contador
  id, country, name, rate, applies_to, valid_from

ledger_account / journal_entry / journal_line   -- doble partida
  journal_line: account_id, debit, credit, currency, amount_base
  CHECK por asiento: suma de débitos = suma de créditos (en moneda base)
```

### Reglas
- Un cargo en USD pagado en bolívares se convierte al **tipo de cambio de la fecha de pago**; la diferencia respecto al cambio del cargo se registra como diferencia cambiaria.
- Pagos parciales, adelantos, mora y recargos configurables; becas y descuentos con vigencia y aprobación.
- Anulación de un pago por reverso: nunca borra, genera asiento inverso.
- Recibos numerados y correlativos por institución; toda corrección queda en la bitácora.
- Impuestos y obligaciones fiscales (facturación, IVA, impuestos sobre pagos en divisas): **solo como reglas configurables**; su contenido se valida con un contador antes de producción.
