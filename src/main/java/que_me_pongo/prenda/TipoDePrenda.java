package que_me_pongo.prenda;

import java.util.List;
import java.util.Objects;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class TipoDePrenda {
    @Id
    @GeneratedValue
    private long id;
    private Tipo tipo;
    private Categoria categoria;

    @ElementCollection(targetClass = Material.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "materiales_validos", joinColumns = @JoinColumn(name = "id_tipo_de_prenda"))
    @Column(name = "material", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<Material> materialesValidos;

    private int capa;
    private double nivelAbrigo;

    public TipoDePrenda() {
    }

    public TipoDePrenda(Tipo tipo, Categoria categoria, List<Material> materialesValidos, int capa, double nivelAbrigo) {
        this.tipo = Objects.requireNonNull(tipo);
        this.categoria = Objects.requireNonNull(categoria);
        this.materialesValidos = Objects.requireNonNull(materialesValidos);
        this.capa = capa;
        this.nivelAbrigo = nivelAbrigo;
    }

    public Boolean validarMaterial(Material material) {
        return materialesValidos.contains(material);
    }

    public Tipo getTipo() {
        return this.tipo;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public int getCapa() {
        return this.capa;
    }

    public double getNivelAbrigo() {
        return this.nivelAbrigo;
    }

    public List<Material> getMaterialesValidos() {return materialesValidos;}
}

