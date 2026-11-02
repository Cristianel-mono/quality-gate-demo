import java.time.LocalDate;

/** Version DEFECTUOSA (D-103 y D-105). Provoca el fallo del job unit en el pull request. */
public class Pricing {
    public static long iva(long montoCop) {
        return (long) (montoCop * 0.19);                       // trunca en vez de redondear
    }

    public static long ivaCentavos(long centavos) {
        return Math.round(centavos / 100.0 * 0.19 * 100);      // precio con centavos en double
    }

    public static boolean bannerActivo(LocalDate hoy, LocalDate inicio, LocalDate fin) {
        return !hoy.isBefore(LocalDate.of(2025, 11, 27)) && !hoy.isAfter(LocalDate.of(2025, 11, 30));  // fechas fijas de 2025
    }
}
