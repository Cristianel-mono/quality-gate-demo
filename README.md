# quality-gate-demo

Mini-repositorio para generar **evidencia real de pipeline** de la Actividad 5 (Calidad en Software II).
Contiene tres jobs que se ejecutan sin servicios externos: trazabilidad del PR, pruebas de D-103/D-105 y el gate del soak (D-102).

## Pasos para obtener la evidencia (unos 10 minutos)

1. Cree un repositorio vacio en GitHub y suba esta carpeta a la rama `main`.
2. **Pipeline verde.** Abra un PR cualquiera con titulo `A8: probar el gate` -> los tres jobs pasan.
3. **Rojo por trazabilidad.** Abra un PR con titulo `arreglo rapido` -> falla `traceability`.
4. **Rojo por D-103/D-105.** En una rama copie `ejemplos/Pricing_defectuoso.java` sobre `src/Pricing.java`, abra un PR
   titulado `D-103: regresion del calculo de IVA` -> falla `unit` (5 pruebas en FAIL).
5. **Rojo por D-102.** En la pestana Actions elija `quality-gate-demo` -> Run workflow -> archivo `soak_defectuoso.csv` -> falla `soak-gate`.
   Con `soak_defectuoso_24min.csv` falla solo el chequeo G4 (tendencia), y con `soak_corregido.csv` pasa.
6. Haga capturas de cada ejecucion (verde y rojo) y pegue los enlaces en el Anexo C del informe.

## Ejecucion local

```
javac -d out src/*.java test/*.java && java -cp out PricingTest
python3 tools/soak_gate.py data/soak_defectuoso.csv
```
