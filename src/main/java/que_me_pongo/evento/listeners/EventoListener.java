package que_me_pongo.evento.listeners;

import que_me_pongo.evento.Evento;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class EventoListener {

	@Id
	@GeneratedValue
	private long id;

	public abstract void sugerenciasRealizadas(Evento evento);
	public abstract void alertaMeteorologica(Evento evento);
}
