package que_me_pongo.evento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import org.uqbar.commons.model.annotations.Observable;

import que_me_pongo.atuendo.Atuendo;
import que_me_pongo.LocalDateTimeAttributeConverter;
import que_me_pongo.evento.listeners.EventoListener;
import que_me_pongo.evento.repetidores.RepeticionDeEvento;
import que_me_pongo.guardarropa.Guardarropa;
import que_me_pongo.prenda.Categoria;
import que_me_pongo.proveedorClima.PronosticoClima;
import que_me_pongo.sugeridor.Sugeridor;
import que_me_pongo.usuario.Usuario;

import javax.persistence.*;

@Observable @Entity
public class Evento {

    @Id @GeneratedValue
    private long id;

    public long getId() {
			return id;
		}

		@Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime fecha;

		@ManyToOne
    private Usuario usuario;

		@ManyToOne
    private Guardarropa guardarropa;

    private String descripcion;

    @Embedded
    private PronosticoClima pronostico;

    /*
     * Dado que el orm no hace diferencia entre no tener
     * coleccion y tener una vacia, ya no podemos usar null
     * para distinguir, arreglo booleanos.
     */
    private boolean tieneSugerencias = false;
    
    private boolean tieneOpiniones = false;
    
    /*
     * La relación de las sugerencias y los rechazados es OneToMany
     * pero Hibernate resuelve la lista ordenada manteniendo los 
     * valores de la columna indice y actualizado la columna de id
     * lo que choca con las constraint de OneToMany.
     */
    
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinTable(name = "Evento_Atuendo_Sugerencias")
    @OrderColumn(name = "ordSugerencias")
    private List<Atuendo> sugerencias;
    
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinTable(name = "Evento_Atuendo_Rechazados")
    @OrderColumn(name = "ordRechazados")
    private List<Atuendo> rechazados;

    @OneToOne(cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
    private Atuendo aceptado;

    public Atuendo getAceptado() {
			return aceptado;
		}

		@ManyToMany
    private Collection<EventoListener> listenersSugerir;

    @Enumerated(EnumType.STRING)
    private RepeticionDeEvento repetidor;

    @ElementCollection(targetClass = Categoria.class)
  	@CollectionTable(name = "aumento_abrigo", joinColumns = @JoinColumn(name = "id_evento"))
  	@Column(name = "categoria", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Categoria> aumentoAbrigo;
    
    @ElementCollection(targetClass = Categoria.class)
  	@CollectionTable(name = "reduccion_abrigo", joinColumns = @JoinColumn(name = "id_evento"))
  	@Column(name = "categoria", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Categoria> reduccionAbrigo;
    
    @SuppressWarnings("unused")
		private Evento() {}

    public Evento(LocalDateTime fecha, Usuario usuario, Guardarropa guardarropa, String descripcion, Collection<EventoListener> notificadores) {
    	settearEstadoInicial(fecha, usuario, guardarropa, descripcion, notificadores);
    	this.repetidor = RepeticionDeEvento.NOREPITE;
    }
    
    public Evento(LocalDateTime fecha,Usuario usuario,Guardarropa guardarropa,String descripcion,Collection<EventoListener> notificadores, RepeticionDeEvento tiempoParaRepetir) {
      settearEstadoInicial(fecha, usuario, guardarropa, descripcion, notificadores);
      this.repetidor = tiempoParaRepetir;
    }
    
    public LocalDateTime getFecha() {
    	return this.fecha;
    }
    
    public Usuario getUsuario() {
    	return this.usuario;
    }
    
    public Guardarropa getGuardarropa() {
    	return this.guardarropa;
    }
    
    public String getDescripcion() {
    	return this.descripcion;
    }

    public List<Atuendo> getSugerenciasPendientes() { return sugerencias; }
    
    public Atuendo getProximaSugerenciaPendiente() { 
    	Optional<Atuendo> posibleAtuendo = sugerencias.stream().filter(a -> a.estaDisponible(this.fecha.toLocalDate())).findFirst();
    	return posibleAtuendo.orElse(null);
    }

    public Collection<EventoListener> getListenersSugerir(){
    	return this.listenersSugerir;
    }
    
    public Evento addListenersSugerir(EventoListener listener) {
    	listenersSugerir.add(listener);
    	return this;
    }

    public void obtenerSugerencias(Sugeridor sugeridor, PronosticoClima pronostico){
        sugerencias = new LinkedList<Atuendo>(
                sugeridor.sugerir(guardarropa.atuendos(fecha.toLocalDate()), pronostico, usuario));
        rechazados = new LinkedList<Atuendo>();
        aceptado = null;
        this.pronostico = pronostico;
        
        if(sugerenciaEsUnica())
        	this.aceptarSugerencia();
    }

    //Le va a cargar una lista de atuendos al usuario en su lista atuendos pendientes
    public void sugerir(Sugeridor sugeridor, PronosticoClima pronostico){
        this.obtenerSugerencias(sugeridor,pronostico);
        listenersSugerir.forEach(listener -> listener.sugerenciasRealizadas(this));
        tieneSugerencias = true;
        repetidor.generarRepeticion(this);
    }

    public void resugerir(Sugeridor sugeridor, PronosticoClima pronostico){
        this.obtenerSugerencias(sugeridor,pronostico);
        listenersSugerir.forEach(listener -> listener.alertaMeteorologica(this));
    }
    
    public void rechazarSugerencia() {
    	validarExistenSugerencias();
    	validarNoAceptado();
    	Atuendo rechazada = getProximaSugerenciaPendiente();
    	sugerencias.remove(rechazada);
    	rechazados.add(rechazada);
    	if(sugerenciaEsUnica())
    		aceptarSugerencia();
    }
    
    public void aceptarSugerencia() {
    	validarExistenSugerencias();
    	validarNoAceptado();
    	Atuendo aceptada = getProximaSugerenciaPendiente();
    	sugerencias.remove(aceptada);
    	aceptado = aceptada;
    	rechazados.addAll(sugerencias);
    	sugerencias.clear();
    	guardarropa.reservarAtuendo(fecha.toLocalDate(), aceptado);
    }
    
    public void setOpiniones(Set<Categoria> aumentarAbrigo, Set<Categoria> reducirAbrigo) {
    	aumentoAbrigo = aumentarAbrigo;
    	reduccionAbrigo = reducirAbrigo;
    	usuario.ajustarPreferencias(aumentarAbrigo, reducirAbrigo);
    	tieneOpiniones = true;
    }
    
    public boolean opinado() {
    	return tieneOpiniones;
    }
    
    public void deshacerDecision() {
    	validarExistenSugerencias();
    	if(aceptado != null) {
    		sugerencias.add(0, aceptado);
    		guardarropa.liberarAtuendo(fecha.toLocalDate(), aceptado);
    		aceptado = null;
    		if(tieneOpiniones) {
    			usuario.ajustarPreferencias(reduccionAbrigo, aumentoAbrigo);
    			tieneOpiniones = false;
    		}
    	}
    	else if(!rechazados.isEmpty()) {
    		sugerencias.add(0, rechazados.remove(rechazados.size() - 1));
    	}
    	else
    		throw new NoHistorialException();
    }

    /*Verdadero si las fechas coinciden o la fecha del evento es posterior a la consultada
    y ademas esta dentro del rango de cantDias
    */
    public boolean esProximo(LocalDate fConsultada, int cantDias){
        return fConsultada.isEqual(fecha.toLocalDate()) || (fConsultada.isBefore(fecha.toLocalDate()) && fecha.toLocalDate().isBefore(fConsultada.plusDays(cantDias)));
    }
    
    public boolean getSugirio() {
    	return tieneSugerencias;
    }
    
    public boolean sugerenciaAceptada() {
    	return aceptado != null;
    }
    
    public boolean sugerenciasVacias() {
    	return getSugirio() && (sugerencias.isEmpty() || haySugerenciasNoDisponibles()) && aceptado == null;
    }
    
    public boolean haySugerenciasNoDisponibles() {
    	return getSugirio() && sugerencias.stream().anyMatch(atuendo -> !atuendo.estaDisponible(fecha.toLocalDate()));
    }
    
    public boolean sugerenciaEsUnica() {
    	return 1 == sugerencias.stream().
    							filter(atuendo -> atuendo.estaDisponible(fecha.toLocalDate())).
    							count();
    }
    
    private void validarNoAceptado() {
    	if(aceptado != null)
    		throw new AtuendoYaDecididoException();
    }
    
    private void validarExistenSugerencias() {
    	if(sugerencias == null)
    		throw new SinSugerenciasException();
    }
    
    private void settearEstadoInicial(LocalDateTime fecha,Usuario usuario, Guardarropa guardarropa,String descripcion,Collection<EventoListener> notificadores) {
    	this.fecha = Objects.requireNonNull(fecha);
      this.usuario = Objects.requireNonNull(usuario);
      this.descripcion = Objects.requireNonNull(descripcion);
      this.guardarropa = Objects.requireNonNull(guardarropa);
      if(notificadores == null)
      	this.listenersSugerir = new ArrayList<EventoListener>();
      else
      	this.listenersSugerir = notificadores;   
    }
    
    public void generarRepeticion(LocalDateTime nuevaFecha) {
    	Collection<EventoListener> listeners = new LinkedList<EventoListener>(this.listenersSugerir);
    	RepositorioEventos.getInstance().crearEvento(nuevaFecha, this.usuario, this.guardarropa, this.descripcion, listeners, this.repetidor);
    }

    public boolean chequearPronostico(PronosticoClima nuevoPronostico) {
			return pronostico.difiere(nuevoPronostico);
		}

    public boolean estaEntreFechas(Date desde, Date hasta){
        ZonedDateTime zdt = fecha.atZone(ZoneId.systemDefault());
        Date fechaConvertida = Date.from(zdt.toInstant());

        return fechaConvertida.after(desde) && fechaConvertida.before(hasta);
    }
}