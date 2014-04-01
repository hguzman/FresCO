/*	Preferencias.java
 * 	2013-11-11
 * 	Copyright (c) 2013 Guzamapp
 *  All rights reserved.
 */

package com.guzmapp.fresco.modelos;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import static com.guzmapp.fresco.modelos.CommonUtilities.TAG;

/*	CLase que permite guardar las preferencias del dispositivo
 * 	Permite identificar el numero de registro del Dispositivo 
 * 
 * 	@version	1.0 2013-11-11
 * 	@author		Ing.Esp. Henry Miguel Guzm??n Escorcia
 */
public class Preferencias {
	private Context oContext;
	private SharedPreferences oPreferencias;
	private String NOMBRE_PREFERENCIA="fresco";
	private String correo;
	
	public Preferencias(Context contex) {
		oContext=contex;
		oPreferencias=contex.getSharedPreferences(NOMBRE_PREFERENCIA, Context.MODE_PRIVATE);
	}
	
	public void registrarPreferencia(String user, String regId, String fecha){
		int versionApp=getAppVersion();
		SharedPreferences.Editor editor=oPreferencias.edit();
		editor.putString("correo", user);
        editor.putString("idregistro", regId);
        editor.putInt("version", versionApp);
        editor.putString("fecha", fecha);
        editor.commit();
	}
	
	public void registrarNumeroVehiculos(int numero){
		SharedPreferences.Editor editor=oPreferencias.edit();
        editor.putInt("numeroVehiculos", numero);
        editor.commit();		
	}
	
	public void registrarNotificacion(String placa, String fecha){
		SharedPreferences.Editor editor=oPreferencias.edit();
        editor.putString("placa", placa);
        editor.putString("fecha", fecha);
        editor.commit();		
	}
	
	public String getNotificacion(){
		return oPreferencias.getString("placa", "");
	}
	
	public int getNumVehiculos(){
		return oPreferencias.getInt("numeroVehiculos", 0);		
	}
	
	public String getFecha(){
		return oPreferencias.getString("fecha", "");
	}
	
	// Retorna "" Si no encuentra nada o en su defecto la respectiva preferencia
	public String obtenerId(){
		return oPreferencias.getString("idregistro", "");
	}
	
	private int getAppVersion(){
		int resp=0;
		try {
			PackageInfo infoPaquete= oContext.getPackageManager().getPackageInfo(oContext.getPackageName(), 0);
			resp= infoPaquete.versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Error al obtener la versi??n");
		}
		return resp;
	}

	public String getCorreo() {
		return oPreferencias.getString("correo", "");
	}

	
}
