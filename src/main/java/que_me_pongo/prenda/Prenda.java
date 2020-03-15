package que_me_pongo.prenda;

import que_me_pongo.ColoresAttributeConverter;

import javax.persistence.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Entity
public class Prenda {

	@Id @GeneratedValue
	private long id;

	@ManyToOne
	private TipoDePrenda tipo;

	@Enumerated(EnumType.STRING)
	private Material material;

	@Convert(converter = ColoresAttributeConverter.class)
	private Color colorPrimario;

	@Convert(converter = ColoresAttributeConverter.class)
	private Color colorSecundario;

	@Lob
	private String linkImagen = null;

	@OneToMany(cascade = CascadeType.ALL) @JoinColumn(name="prenda_id")
	private Set<Reserva> reservas = new LinkedHashSet<Reserva>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Prenda prenda = (Prenda) o;
		return tipo.equals(prenda.tipo) &&
				material == prenda.material &&
				colorPrimario.equals(prenda.colorPrimario) &&
				Objects.equals(colorSecundario, prenda.colorSecundario) &&
				Objects.equals(linkImagen, prenda.linkImagen);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tipo, material, colorPrimario, colorSecundario, linkImagen);
	}

	@Override
	public String toString() {
		return "Prenda{" +
				"tipo=" + getTipo() +
				", categoria=" + getCategoria() +
				", material=" + material +
				", colorPrimario=" + colorPrimario +
				", colorSecundario=" + colorSecundario +
				'}';
	}

	public Prenda() {
	}

	public Prenda (TipoDePrenda tipo, Material material, Color colorPrimario, Color colorSecundario, String path) {
		this.tipo = Objects.requireNonNull(tipo, "es obligatorio introducir un tipo");
		this.material = Objects.requireNonNull(material, "es obligatorio introducir un material");
		this.validarMateriales(material);
		this.colorPrimario = Objects.requireNonNull(colorPrimario, "es obligatorio introducir un color primario");
		this.validarColor(colorPrimario, colorSecundario);
		this.colorSecundario = colorSecundario;
		this.linkImagen = path;
	}


	public Tipo getTipo() {
		return tipo.getTipo();
	}

	public Categoria getCategoria() {
		return tipo.getCategoria();
	}

	public Material getMaterial() {
		return material;
	}

	public Color getColorPrimario() {
		return colorPrimario;
	}

	public Color getColorSecundario() {
		return colorSecundario;
	}

	public String getLinkImagen() {
		return linkImagen;
	}

	public int getCapa() {
		return this.tipo.getCapa();
	}
	
	public double getNivelAbrigo() {
		return this.tipo.getNivelAbrigo();
	}

	public TipoDePrenda getTipoPrenda() {return tipo;}

	public void addReserva(LocalDate fecha) {
		if(getReserva(fecha))
			throw new PrendaYaReservadaException();
		reservas.add(new Reserva(fecha));
	}
	
	public void removeReserva(LocalDate fecha) {
		if(!getReserva(fecha))
			throw new PrendaNoReservadaException();
		reservas.removeIf(reserva -> reserva.getFecha().isEqual(fecha));
	}
	
	public boolean getReserva(LocalDate fecha) {
		return reservas.stream().anyMatch(reserva -> reserva.getFecha().isEqual(fecha));
	}

	private void validarColor(Color colorPrimario, Color colorSecundario) {
		if(colorPrimario.equals(colorSecundario))
			throw new ColoresIgualesException("Los colores son iguales");
	}

	private void validarMateriales(Material material) {
		if(!this.tipo.validarMaterial(material))
			throw new MaterialInvalidoException("Material invalido");
	}
}