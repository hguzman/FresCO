package com.guzmapp.fresco.entidades;


public class Comunicador {
//	private static Informacion objeto = null;
	private static Object object=null;
	   
/*	   public static void setObjeto(Informacion newObjeto) {
	      objeto = newObjeto;
	   }
	   
	   public static Informacion getObjeto() {
	      return objeto;
	   }
*/
	public static Object getObject() {
		return object;
	}

	public static void setObject(Object object) {
		Comunicador.object = object;
	}
	   
	   
}
