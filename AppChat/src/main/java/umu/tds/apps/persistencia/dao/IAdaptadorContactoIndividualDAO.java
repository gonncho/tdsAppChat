
package umu.tds.apps.persistencia.dao;

import java.util.List;

import umu.tds.apps.modelo.ContactoIndividual;

/**
 * Interfaz que establece las operaciones de almacenamiento para
 * {@link ContactoIndividual}.
 */
public interface IAdaptadorContactoIndividualDAO {

	/**
	 * Guarda un contacto individual en la base de datos.
	 *
	 * @param contacto contacto a registrar.
	 */
	public void registrarContactoIndividual(ContactoIndividual contacto);

	/**
	 * Actualiza los datos de un contacto individual ya almacenado.
	 *
	 * @param contacto entidad con la nueva información.
	 */
	public void modificarContactoIndividual(ContactoIndividual contacto);

	/**
	 * Borra un contacto individual de la capa de datos.
	 *
	 * @param contacto contacto a eliminar.
	 */
	public void borrarContactoIndividual(ContactoIndividual contacto);

	/**
	 * Obtiene un contacto individual a partir de su identificador.
	 *
	 * @param codigo id del contacto deseado.
	 * @return contacto hallado o null en caso contrario.
	 */
	public ContactoIndividual recuperarContactoIndividual(int codigo);

	/**
	 * Devuelve la lista de contactos individuales existentes.
	 *
	 * @return colección con todos los contactos recuperados.
	 */
	public List<ContactoIndividual> recuperarTodosContactosIndividuales();

}
