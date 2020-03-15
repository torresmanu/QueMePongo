package que_me_pongo.usuario;

import java.util.Set;
import java.util.stream.Collectors;

import que_me_pongo.guardarropa.Guardarropa;
import que_me_pongo.prenda.Prenda;
import que_me_pongo.prenda.Categoria;

import javax.persistence.*;

import com.google.common.hash.Hashing;

import que_me_pongo.atuendo.Atuendo;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Entity
public class Usuario {
	@Id
	@GeneratedValue
	private long id;
	
	public long getId() {
		return id;
	}

	private String nombre;

	private String mail;

	private String passwordDigest;

	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<Guardarropa> guardarropas = new HashSet<Guardarropa>();

	@Enumerated(EnumType.STRING)
	private TipoUsuario tipoUsuario;

	@ElementCollection
	@CollectionTable(name = "user_preferences_mapping",
	joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
	@MapKeyEnumerated
	private Map<Categoria, Double> preferencias;

	private Usuario() {
	}

	public Usuario(String name, String email, TipoUsuario tipo, String password){
		this.nombre = name;
		this.mail = email;
		this.tipoUsuario = tipo;
		this.preferencias = new HashMap();
		this.passwordDigest = hashPassword(password);

		this.guardarropas.add(new Guardarropa());
		this.guardarropas.add(new Guardarropa());
	}

//	La forma de instanciar una prenda sería:
	//Prenda miRemera = new PrendaFactory().crearRemera(Material.ALGODON, Color.WHITE, Color.BLACK);
	
	public Set<Guardarropa> getGuardarropas() {
		return this.guardarropas;
	}

	public String getNombre() { return nombre; }

	public String getMail() { return mail; }
	
	public String getPasswordDigest() { return passwordDigest; }

	public void agregarGuardarropas(Guardarropa guardarropa){
		guardarropas.add(guardarropa);
	}

	public void agregarPrenda(Prenda prenda, Guardarropa guardarropa){
		tipoUsuario.agregarPrenda(prenda, guardarropa);
	}

	public Set<Atuendo> atuendos() {
		return this.guardarropas.stream().
				flatMap(guardarropa -> guardarropa.atuendos().stream()).
				collect(Collectors.toSet());
	}

	public Set<Atuendo> atuendosDe(Guardarropa guardarropa){
		return guardarropa.atuendos();
	}

	public void ajustarPreferencias(Set<Categoria> aumentarAbrigo, Set<Categoria> reducirAbrigo) {
		aumentarAbrigo.forEach(categoria -> preferencias.put(categoria, preferencias.getOrDefault(categoria, 0.) + 0.25));
		reducirAbrigo.forEach(categoria -> preferencias.put(categoria, preferencias.getOrDefault(categoria, 0.) - 0.25));
	}
	
	public Double getPreferencia(Categoria categoria) {
		return preferencias.getOrDefault(categoria, 0.);
	}
	
	public boolean chequearPassword(String password) {
		String toCheckDigest = hashPassword(password); 
		return toCheckDigest.equals(passwordDigest);
	}
	
	private String hashPassword(String password) {
		return Hashing.sha256()
					 .hashString(password, StandardCharsets.UTF_8)
					 .toString();
	}

}
