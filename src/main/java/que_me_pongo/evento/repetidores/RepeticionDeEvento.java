package que_me_pongo.evento.repetidores;

import que_me_pongo.evento.Evento;

public enum RepeticionDeEvento {
	NOREPITE {
		@Override
		public void generarRepeticion(Evento evento) {}
	},
	SEMANAL {
		@Override
		public void generarRepeticion(Evento evento) {
			evento.generarRepeticion(evento.getFecha().plusWeeks(1));
		}
	},
	DIARIO {
		@Override
		public void generarRepeticion(Evento evento) {
			evento.generarRepeticion(evento.getFecha().plusDays(1));
		}
	},
	MENSUAL {
		@Override
		public void generarRepeticion(Evento evento) {
			evento.generarRepeticion(evento.getFecha().plusMonths(1));
			
		}
	},
	ANUAL {
		@Override
		public void generarRepeticion(Evento evento) {
			evento.generarRepeticion(evento.getFecha().plusYears(1));
		}
	};
	
	abstract public void generarRepeticion(Evento evento);
}
