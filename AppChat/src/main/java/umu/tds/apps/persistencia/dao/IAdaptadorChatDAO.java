package umu.tds.apps.persistencia.dao;

import java.util.List;

import umu.tds.apps.modelo.Chat;

public interface IAdaptadorChatDAO {

	public void registrarChat(Chat chat);

	public void modificarChat(Chat chat);

	public Chat obtenerChat(int codigo);

	public List<Chat> recuperarTodosChats();

}
