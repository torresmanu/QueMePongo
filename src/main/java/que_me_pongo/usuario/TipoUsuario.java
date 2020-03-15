package que_me_pongo.usuario;

import que_me_pongo.guardarropa.Guardarropa;
import que_me_pongo.prenda.Prenda;

public enum TipoUsuario {
	
		GRATUITO{
			@Override
			public void agregarPrenda(Prenda prenda,Guardarropa guardarropa){
					int cantidadMaxima = Integer.parseInt(System.getenv("prendasMaximasGratuito"));
	        if(guardarropa.estaLleno(cantidadMaxima)){
	            throw new UsuarioGratuitoNoTieneLugarException("Su guardarropas esta lleno, si desea tener mas lugar puede hacerse socio premium");
	        }
	        else{
	            guardarropa.agregarPrenda(prenda);
	        }
	    }
		},
		PREMIUM{
			@Override
			public void agregarPrenda(Prenda prenda, Guardarropa guardarropa) {
				guardarropa.agregarPrenda(prenda);
	    }
		};

    abstract public void agregarPrenda(Prenda prenda, Guardarropa guardarropa);

}
