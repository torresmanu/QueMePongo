package que_me_pongo.prenda;

import java.awt.*;

public class PrendaBuilder {
		TipoDePrenda tipo;
		Material material;
		Color colorPrimario, colorSecundario;
		String imagen;

		public TipoDePrenda getTipo() {
			return tipo;
		}

		public void setTipo(TipoDePrenda tipo) {
			this.tipo = tipo;
		}

		public Material getMaterial() {
			return material;
		}

		public void setMaterial(Material material) {
			this.material = material;
		}

		public Color getColorPrimario() {
			return colorPrimario;
		}

		public void setColorPrimario(Color colorPrimario) {
			this.colorPrimario = colorPrimario;
		}

		public Color getColorSecundario() {
			return colorSecundario;
		}

		public void setColorSecundario(Color colorSecundario) {
			this.colorSecundario = colorSecundario;
		}

		public String getImagen() {
			return imagen;
		}

		public void setImagen(String imagen) {
			this.imagen = imagen;
		}

		public Prenda getPrenda() { return new Prenda(tipo, material, colorPrimario, colorSecundario, imagen); }
}
