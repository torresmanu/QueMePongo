package que_me_pongo.usuario;

public class UsuarioGratuitoNoTieneLugarException extends RuntimeException{
    public UsuarioGratuitoNoTieneLugarException(String mensaje) {
        super(mensaje);
    }
}