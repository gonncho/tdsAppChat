package modelo;



import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DescuentoPorFecha implements Descuento {
 private final int añosMinimos;
 private final double factor; // e.g. 0.1 = 10%

 public DescuentoPorFecha(int añosMinimos, double factor) {
     this.añosMinimos = añosMinimos;
     this.factor      = factor;
 }

 @Override
 public double calcularDescuento(Usuario u) {
     LocalDate hoy = LocalDate.now();
     LocalDate alta = u.getFechaNacimiento(); // o fecha de registro si la guardas ahí
     long años = ChronoUnit.YEARS.between(alta, hoy);
     return (años >= añosMinimos) ? factor : 0.0;
 }
}
