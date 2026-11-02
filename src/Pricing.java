import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/** Version CORREGIDA (D-103 y D-105): BigDecimal con HALF_UP y ventana de campana configurable. */
public class Pricing {
    private static final BigDecimal TASA_IVA = new BigDecimal("0.19");

    /** IVA en pesos enteros para un monto en pesos (por linea). */
    public static long iva(long montoCop) {
        return BigDecimal.valueOf(montoCop).multiply(TASA_IVA).setScale(0, RoundingMode.HALF_UP).longValueExact();
    }

    /** IVA en centavos para un monto expresado en centavos. */
    public static long ivaCentavos(long centavos) {
        return BigDecimal.valueOf(centavos).multiply(TASA_IVA).setScale(0, RoundingMode.HALF_UP).longValueExact();
    }

    /** El banner depende de una ventana que llega por configuracion, no de fechas escritas en el codigo. */
    public static boolean bannerActivo(LocalDate hoy, LocalDate inicio, LocalDate fin) {
        return !hoy.isBefore(inicio) && !hoy.isAfter(fin);
    }
}
