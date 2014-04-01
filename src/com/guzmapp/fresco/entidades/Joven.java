package com.guzmapp.fresco.entidades;



public class Joven {
	private Integer id;
	private String descripcion;
	private String edad;
	private String vigencia;
	private String entidad;


	public Joven(Integer i, String desc, String edad,String vigen, String ent) {
		
		setId(i);
		setDescripcion(desc);
		setEdad(edad);
		setVigencia(vigen);
		setEntidad(ent);
				
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getEdad() {
		return edad;
	}


	public void setEdad(String edad) {
		this.edad = edad;
	}


	public String getVigencia() {
		return vigencia;
	}


	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}


	public String getEntidad() {
		return entidad;
	}


	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	
}