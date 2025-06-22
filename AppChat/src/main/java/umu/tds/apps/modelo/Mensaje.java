package umu.tds.apps.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;


public class Mensaje {

    private String texto;
    private int emoticono;
    private LocalDate fecha;
    private LocalDateTime hora;
    private Usuario emisor;
    private Usuario receptor;
    private Chat chat;
    private int codigo;
    
    public Mensaje(Usuario emisor, String texto, Usuario receptor, Chat chat) {
        validarChat(chat);
        inicializarComun(emisor, receptor, chat);
        this.texto = texto;
    }


    public Mensaje(Usuario emisor, int emoticono, Usuario receptor, Chat chat) {
        validarChat(chat);
        inicializarComun(emisor, receptor, chat);
        this.emoticono = emoticono;
    }

    public Mensaje() {

    }

    public String getTexto() {
        return texto;
    }
    
    public void setTexto(String texto) {
        this.texto = texto;
    }


    public int getEmoticono() {
        return emoticono;
    }
    
    public void setEmoticono(int emoticono) {
        this.emoticono = emoticono;
    }


    
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDateTime getHora() {
        return hora;
    }

    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }

    public Usuario getEmisor() {
        return emisor;
    }
    
    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
    }

    public Usuario getReceptor() {
        return receptor;
    }

    public void setReceptor(Usuario receptor) {
        this.receptor = receptor;
    }

    public Chat getChat() {
        return chat;
    }
    
    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    private void inicializarComun(Usuario emisor, Usuario receptor, Chat chat) {
        this.emisor = emisor;
        this.receptor = receptor;
        this.chat = chat;
		this.fecha = LocalDate.now();
		this.hora = LocalDateTime.now();
    }

    // Comprobar que el chat no sea nulo antes de iniciar un mensaje
    
    private void validarChat(Chat chat) {
        if (chat == null) {
            throw new IllegalArgumentException("Error al acceder al chat: valor nulo");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Mensaje other = (Mensaje) obj;
        return codigo == other.codigo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
    
    
}
