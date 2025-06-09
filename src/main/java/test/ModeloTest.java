package test;

import modelo.Usuario;
import modelo.FactoriaContacto;
import modelo.ContactoIndividual;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.FactoriaDescuento;
import persistencia.RepositorioUsuarios;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ModeloTest {
    public static void main(String[] args) {
        // 1) Creo un usuario de ejemplo
        Usuario u = new Usuario.Builder("Prueba", "prueba@ej.com", "600000000", "1234")
                        .fechaNacimiento(LocalDate.of(1990,1,1))
                        .fechaRegistro(LocalDate.now().minusYears(6))
                        .saludo("Hola, soy demo")
                        .premium(true)
                        .build();
        System.out.println("Usuario creado: " + u.getNombre() + ", tel=" + u.getTelefono());
        System.out.println("Saludo: " + u.getSaludo() + ", registrado el " + u.getFechaRegistro());

        // 2) Creo un contacto individual y un grupo
        ContactoIndividual ci = FactoriaContacto.crearIndividual("Amigo", "611111111");
        Grupo grupo = FactoriaContacto.crearGrupo("GrupoTest", null, List.of(ci));
        u.addContacto(ci);
        u.addContacto(grupo);
        System.out.println("Contactos de usuario: " + u.getContactos().size());

        // 3) Creo mensajes y los añado
        Mensaje m1 = new Mensaje.Builder("Hola", LocalDateTime.now(), Mensaje.Tipo.ENVIADO).build();
        Mensaje m2 = new Mensaje.Builder("¿Qué tal?", LocalDateTime.now(), Mensaje.Tipo.RECIBIDO).build();
        u.addMensaje(m1);
        ci.addMensaje(m2);
        System.out.println("Mensajes de usuario: " + u.getMensajes().size());
        System.out.println("Mensajes de contacto individual: " + ci.getMensajes().size());

        // 4) Pruebo los descuentos
        double descFecha    = FactoriaDescuento.getDescuento("FECHA").calcularDescuento(u);
        double descMensajes = FactoriaDescuento.getDescuento("MENSAJES").calcularDescuento(u);
        System.out.printf("Descuento por fecha (años>=5): %.2f%n", descFecha);
        System.out.printf("Descuento por mensajes (>=100): %.2f%n", descMensajes);

        // 5) Uso del repositorio en memoria para registrar y validar login
        RepositorioUsuarios.INSTANCE.add(u);
        boolean loginOk = RepositorioUsuarios.INSTANCE.login("600000000", "1234");
        System.out.println("Login a través del repositorio: " + (loginOk ? "OK" : "FAIL"));
    }
}
