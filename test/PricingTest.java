import java.time.LocalDate;

/** Pruebas del gate de la etapa 1. Termina con codigo 1 si alguna falla. */
public class PricingTest {
    static int fallos = 0;

    static void esperar(String nombre, long esperado, long obtenido) {
        boolean ok = esperado == obtenido;
        System.out.printf("[%s] %s (esperado=%d, obtenido=%d)%n", ok ? "PASS" : "FAIL", nombre, esperado, obtenido);
        if (!ok) fallos++;
    }

    public static void main(String[] args) {
        // D-103: valores borde, el IVA cae justo en .5
        esperar("IVA de COP 1.050 (199,5 -> 200)", 200, Pricing.iva(1_050));
        esperar("IVA de la linea 3 x COP 1.050 (598,5 -> 599)", 599, Pricing.iva(3 * 1_050));
        esperar("IVA con centavos: precio 42,50 -> 808 centavos", 808, Pricing.ivaCentavos(4_250));
        esperar("IVA con centavos: precio 49,50 -> 941 centavos", 941, Pricing.ivaCentavos(4_950));

        // D-105: el banner debe estar activo el 27-nov-2026 con la ventana de la campana 2026
        boolean activo = Pricing.bannerActivo(LocalDate.of(2026, 11, 27), LocalDate.of(2026, 11, 27), LocalDate.of(2026, 11, 30));
        esperar("Banner activo el 27-nov-2026", 1, activo ? 1 : 0);

        System.out.println(fallos == 0 ? "RESULTADO: PASS" : "RESULTADO: FAIL (" + fallos + " pruebas)");
        System.exit(fallos == 0 ? 0 : 1);
    }
}
