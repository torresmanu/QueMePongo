package que_me_pongo.proveedorClima;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import que_me_pongo.LocalDateTimeAttributeConverter;

import java.time.LocalDateTime;

@Embeddable
public class PronosticoClima {
	
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	@Column(name = "fechaPronostico")
	private LocalDateTime fecha;
	/*Un bug de hibernate hace que los numeros 
	 *sean not-null por defecto.
	 */
	@Column(nullable = true)
	private double temperatura;
	//TODO Rellenar
	
	public PronosticoClima(LocalDateTime fechaPronostico, double temperatura) {
		this.fecha = fechaPronostico;
		this.temperatura = temperatura;
	}
	
	public LocalDateTime getFechaPronostico() {
		return fecha;
	}
	public double getTemperatura() {
		return temperatura;
	}
	
	public boolean difiere(PronosticoClima otro) {
		//TODO poner más condiciones una vez se rellene la clase
		return Math.abs(this.temperatura - otro.getTemperatura()) > 10;
	}

	public PronosticoClima() {
	}
}
