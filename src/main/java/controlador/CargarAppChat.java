package controlador;

import java.time.LocalDate;

import modelo.ContactoIndividual;
import modelo.Mensaje;

public class CargarAppChat {
	

	public static void main(String[] args) {
		AppChat appChat = AppChat.INSTANCE;
		appChat.registrarUsuario("aa", "aa@mail.com", "11", "aa", LocalDate.of(1960, 10, 3), "/usuarios/fotoJGM.png", "Hola, soy jesus");
        appChat.registrarUsuario("bb", "bb@mail.com", "22", "bb", LocalDate.of(1995, 12, 28), "/usuarios/foto-elena.png", "hola, soy elena");
        appChat.registrarUsuario("cc", "cc@mail.com", "33", "cc", LocalDate.of(2000, 5, 15), "/usuarios/rosalia.jpg", "hola, soy rosalia");
        appChat.registrarUsuario("dd", "dd@mail.com", "44", "dd", LocalDate.of(1970, 5, 11), "/usuarios/foto-diego.png", "hola, soy diego");
        appChat.registrarUsuario("ee", "ee@mail.com", "55", "ee", LocalDate.of(1990, 3, 28), "/usuarios/annetaylor.jpg", "hola, soy anne");
		
		appChat.login("11", "aa");
		
		ContactoIndividual c2 = appChat.agregarContacto("elena", "22");
		ContactoIndividual c3 = appChat.agregarContacto("rosalia", "33");
		
		appChat.enviarMensajeContacto(c2, "Hola, ¿cómo estás?", -1, Mensaje.Tipo.ENVIADO);
        appChat.enviarMensajeContacto(c2, "", 2, Mensaje.Tipo.ENVIADO);
		
        appChat.enviarMensajeContacto(c3, "Cuando cantas?", -1, Mensaje.Tipo.ENVIADO);
        appChat.enviarMensajeContacto(c2, "", 6, Mensaje.Tipo.ENVIADO);
		
		appChat.login("22", "bb");
		
		//ContactoIndividual c1 =appChat.agregarContacto("jesus", "11");
//		ContactoIndividual c1 = RepositorioUsuarios.INSTANCE.buscarUsuarioPorMovil("22").getContactoIndividual("11");
		ContactoIndividual c1 = appChat.agregarContacto("jesus", "11");
		ContactoIndividual c4 = appChat.agregarContacto("diego", "44");
		ContactoIndividual c5 = appChat.agregarContacto("anne", "55");
		
		appChat.enviarMensajeContacto(c1, "Vienes este finde?", -1, Mensaje.Tipo.ENVIADO);
        appChat.enviarMensajeContacto(c1, "", 3, Mensaje.Tipo.ENVIADO);
        appChat.enviarMensajeContacto(c4, "Juegas esta semana?", -1, Mensaje.Tipo.ENVIADO);
	    
	    System.out.println("Fin de la carga de datos");
	}

}
