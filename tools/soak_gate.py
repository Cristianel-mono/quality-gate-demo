"""Gate automatico para soak test (propuesto en la Etapa 7 del pipeline).

Lee un CSV (minuto,p95_ms,error_pct,conexiones_activas) y falla (exit 1) si:
  G1  p95 de cualquier minuto del ultimo cuarto > 600 ms (SLO de Act. 3/4)
  G2  degradacion: mediana p95 de los ultimos 10 min > 1.20 x mediana de los primeros 10 min
  G3  tasa de error promedio > 1.0 %
  G4  pendiente de conexiones activas > 0.10 conexiones/min (indicio de fuga)
Uso: python3 soak_gate.py archivo.csv
"""
import csv
import statistics
import sys

SLO_P95_MS = 600
MAX_DEGRADACION = 1.20
MAX_ERROR_PCT = 1.0
MAX_PENDIENTE_CONN = 0.10   # conexiones/min


def pendiente(xs, ys):
    mx, my = statistics.fmean(xs), statistics.fmean(ys)
    den = sum((x - mx) ** 2 for x in xs)
    return sum((x - mx) * (y - my) for x, y in zip(xs, ys)) / den


def main(ruta):
    with open(ruta, newline="") as f:
        filas = [{k: float(v) for k, v in r.items()} for r in csv.DictReader(f)]
    t = [r["minuto"] for r in filas]
    p95 = [r["p95_ms"] for r in filas]
    err = [r["error_pct"] for r in filas]
    con = [r["conexiones_activas"] for r in filas]
    n = len(filas)
    ultimo_cuarto = p95[-(n // 4):]

    resultados = [
        ("G1 p95 (ultimo cuarto) <= 600 ms", max(ultimo_cuarto) <= SLO_P95_MS, f"max={max(ultimo_cuarto):.0f} ms"),
        ("G2 degradacion <= 1.20x", statistics.median(p95[-10:]) <= MAX_DEGRADACION * statistics.median(p95[:10]),
         f"ratio={statistics.median(p95[-10:]) / statistics.median(p95[:10]):.2f}x"),
        ("G3 error promedio <= 1.0 %", statistics.fmean(err) <= MAX_ERROR_PCT, f"prom={statistics.fmean(err):.2f} %"),
        ("G4 pendiente conexiones <= 0.10/min", pendiente(t, con) <= MAX_PENDIENTE_CONN, f"pendiente={pendiente(t, con):.2f}/min"),
    ]
    print(f"Archivo: {ruta}  ({n} minutos)")
    for nombre, ok, detalle in resultados:
        print(f"  [{'PASS' if ok else 'FAIL'}] {nombre:<40} {detalle}")
    ok_total = all(ok for _, ok, _ in resultados)
    print(f"RESULTADO: {'PASS' if ok_total else 'FAIL'}  (exit {0 if ok_total else 1})")
    return 0 if ok_total else 1


if __name__ == "__main__":
    sys.exit(main(sys.argv[1]))
