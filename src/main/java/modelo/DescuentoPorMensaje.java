package modelo;


public class DescuentoPorMensaje implements Descuento {
 private final int mensajesMinimos;
 private final double factor;

 public DescuentoPorMensaje(int mensajesMinimos, double factor) {
     this.mensajesMinimos = mensajesMinimos;
     this.factor          = factor;
 }

 @Override
 public double calcularDescuento(Usuario u) {
     int total = u.getMensajes().size();
     return (total >= mensajesMinimos) ? factor : 0.0;
 }
}
