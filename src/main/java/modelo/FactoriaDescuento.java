package modelo;

//modelo

import java.util.Map;
import java.util.HashMap;

public class FactoriaDescuento {
 private static final Map<String,Descuento> tipos = new HashMap<>();
 static {
     tipos.put("FECHA", new DescuentoPorFecha(5, 0.2));
     tipos.put("MENSAJES", new DescuentoPorMensaje(100, 0.15));
 }

 public static Descuento getDescuento(String clave) {
     return tipos.get(clave);
 }
 

}
