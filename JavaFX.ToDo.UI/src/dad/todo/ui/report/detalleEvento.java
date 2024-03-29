package dad.todo.ui.report;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.EventoItem;
import dad.todo.ui.model.EventosModel;

public class detalleEvento {
	
	private String titulo;
	private String descripcion;
	private LocalDate fecha;
	private String direccion;
	private Double latitud;
	private Double longitud;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	
	public static detalleEvento fromEventoModel(EventosModel model){
		detalleEvento detalle = new detalleEvento();
		detalle.setTitulo(model.getTitulo());
		detalle.setDescripcion(model.getDescripcion());
		
		detalle.setFecha(model.getFecha());
		detalle.setHoraFin(model.getHoraFin());
		detalle.setHoraInicio(model.getHoraInicio());
		
		if(model.getLugar()!=null){
		detalle.setDireccion(model.getLugar().getDescripccion());
		detalle.setLatitud(model.getLugar().getLatitud());
		detalle.setLongitud(model.getLugar().getLongitud());
		}
		return detalle;
	}
	
	public detalleEvento(){
		
	}
	
	public detalleEvento(EventoItem model){
		setTitulo(model.getTitulo());
		setDescripcion(model.getDescripcion());
		setDireccion(model.getLugar().getDescripcion());
		setLatitud(model.getLugar().getLatitud());
		setLongitud(model.getLugar().getLongitud());
		
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(model.getFecha());
		LocalDate fechaAux = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH));
		setFecha(fechaAux);

		LocalTime horaIniAux = LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
		setHoraInicio(horaIniAux);

		calendar.add(Calendar.MINUTE, model.getDuracion().intValue());
		LocalTime horaFinAux = LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
		setHoraFin(horaFinAux);
	}
	
	public static detalleEvento fromItem(EventoItem model){
		detalleEvento detalle = new detalleEvento();
		detalle.setTitulo(model.getTitulo());
		detalle.setDescripcion(model.getDescripcion());
		detalle.setDireccion(model.getLugar().getDescripcion());
		detalle.setLatitud(model.getLugar().getLatitud());
		detalle.setLongitud(model.getLugar().getLongitud());
		
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(model.getFecha());
		LocalDate fechaAux = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH));
		detalle.setFecha(fechaAux);

		LocalTime horaIniAux = LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
		detalle.setHoraInicio(horaIniAux);

		calendar.add(Calendar.MINUTE, model.getDuracion().intValue());
		LocalTime horaFinAux = LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
		detalle.setHoraFin(horaFinAux);
		return detalle;
	}
	
		
	public static ArrayList<detalleEvento> getTodosLosEventos(){
		
		ArrayList<detalleEvento> listaEventos = new ArrayList<>();
		
		try {
			List<EventoItem> eventosItem = ServiceFactory.getEventosService().getEventos();
			eventosItem.forEach(e-> {
				detalleEvento eventoReport = new detalleEvento(); 
				eventoReport.setDescripcion(e.getDescripcion());
				eventoReport.setTitulo(e.getTitulo());
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(e.getFecha());
				eventoReport.setFecha(LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH)));
				eventoReport.setHoraInicio(LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
				listaEventos.add(eventoReport);
			});
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		
		return listaEventos;
		
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}



	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}

}
