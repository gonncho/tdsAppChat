package modelo;

public class ContactoIndividual extends Contacto {

    public ContactoIndividual(String nombre, String telefono) {
        super(nombre, telefono);
    }


	@Override
    public String toString() {
        return getNombre() != null 
            ? getNombre() 
            : getTelefono();
    }
}
