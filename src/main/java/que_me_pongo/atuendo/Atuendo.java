package que_me_pongo.atuendo;

import que_me_pongo.prenda.Prenda;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Atuendo {

    @Id
    @GeneratedValue
    private long id;

    @ManyToMany
    private List<Prenda> prendas;

    private Atuendo() {}

    public Atuendo(List<Prenda> p){
        prendas=p;
    }
    
    public List<Prenda> getPrendas(){
    	return prendas;
    }
    
    public boolean tienePrenda(Prenda prenda) {
    	return getPrendas().contains(prenda);
    }
    
    public int cantidadPrendas() {
    	return getPrendas().size();
    }
    
    public boolean mismoAtuendo(Atuendo otro) {
  		return cantidadPrendas() == otro.cantidadPrendas() && 
  				getPrendas().stream().allMatch(prenda -> otro.tienePrenda(prenda));
  	}
    
    public boolean estaDisponible(LocalDate fecha) {
    	return prendas.stream().noneMatch(prenda -> prenda.getReserva(fecha));
    }

		public long getId() {
			return id;
		}
}
