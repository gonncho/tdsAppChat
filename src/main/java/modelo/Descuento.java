package modelo;

public interface Descuento {
 /** 
  * Devuelve el factor [0–1] de descuento sobre la cuota base
  * p.ej 0.2 = 20% de descuento
  */
 double calcularDescuento(Usuario u);
}
