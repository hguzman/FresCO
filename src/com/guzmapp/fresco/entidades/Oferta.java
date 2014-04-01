package com.guzmapp.fresco.entidades;



public class Oferta {
	private Integer id;
	private String pkOferta;
	private String oportunidad;
	private String ubicacion;
	private String fechaVencimiento;
	private String edadObjetivo;
	private String urlFuente;
	private String entidadNombre;

	public Oferta(Integer i, String pOferta, String op,String ub, String fe, String ed, String url,String en) {
		
		setId(i);
		setPkOferta(pOferta);
		setOportunidad(op);
		setUbicacion(ub);
		setFechaVencimiento(fe);
		setEdadObjetivo(ed);
		setUrlFuente(url);
		setEntidadNombre(en);
		
	}

	public String getPkOferta() {
		return pkOferta;
	}

	public void setPkOferta(String pkOferta) {
		this.pkOferta = pkOferta;
	}

	public String getOportunidad() {
		return oportunidad;
	}

	public void setOportunidad(String oportunidad) {
		this.oportunidad = oportunidad;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getEdadObjetivo() {
		return edadObjetivo;
	}

	public void setEdadObjetivo(String edadObjetivo) {
		this.edadObjetivo = edadObjetivo;
	}

	public String getUrlFuente() {
		return urlFuente;
	}

	public void setUrlFuente(String urlFuente) {
		this.urlFuente = urlFuente;
	}

	public String getEntidadNombre() {
		return entidadNombre;
	}

	public void setEntidadNombre(String entidadNombre) {
		this.entidadNombre = entidadNombre;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	

	
}