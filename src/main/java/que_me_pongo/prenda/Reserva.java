package que_me_pongo.prenda;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import que_me_pongo.LocalDateAttributeConverter;

import java.time.LocalDate;

@Entity
public class Reserva {

    @Id
    @GeneratedValue
    private long id;

    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate fecha;

    public Reserva() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Reserva(LocalDate f){
        fecha=f;
    }
}
