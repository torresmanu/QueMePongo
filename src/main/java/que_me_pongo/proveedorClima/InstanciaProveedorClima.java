package que_me_pongo.proveedorClima;

public class InstanciaProveedorClima {
	static ProveedorClima instancia;
	
	public static ProveedorClima getInstancia() {
		return instancia;
	}

	public static void setInstancia(ProveedorClima instancia) {
		InstanciaProveedorClima.instancia = instancia;
	}
}
