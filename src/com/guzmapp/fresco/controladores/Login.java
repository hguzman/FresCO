/*	Login.java
 * 	2013-11-11
 * 	Copyright (c) 2013 Guzamapp
 *  All rights reserved.
 */

package com.guzmapp.fresco.controladores;

import static com.guzmapp.fresco.modelos.CommonUtilities.SENDER_ID;
import static com.guzmapp.fresco.modelos.CommonUtilities.TAG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.guzmapp.fresco.ConnectionDetector;
import com.guzmapp.fresco.R;
import com.guzmapp.fresco.modelos.CommonUtilities;
import com.guzmapp.fresco.modelos.CustomHttpClient;
import com.guzmapp.fresco.modelos.GcmUtilities;
import com.guzmapp.fresco.modelos.Preferencias;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

/*	CLase Controladora
 * 	Controla la vista login en la cual se verifica el acceso al app 
 * 
 * 	@version	1.0 2013-11-11
 * 	@author		Ing.Esp. Henry Miguel Guzmán Escorcia
 */

public class Login extends Activity {
	private EditText oCorreo;
	private EditText oClave;
	private CheckBox oTerminos;
	private Context oContexto;
	private GoogleCloudMessaging gcm;
	private Preferencias pref;
	private String regid="";
	private ProgressDialog mensaje;
	private List<NameValuePair> parametros;
	// Connection detector
	private ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pref=new Preferencias(getApplicationContext());
		if (pref.obtenerId()!=""){		
			
			//Aqui se realizo el cambio para mandar al menú
//			Intent i=new Intent(getApplicationContext(), CtrlVehiculo.class);
			
			Intent i=new Intent(getApplicationContext(), CtrlMenu.class);
			startActivity(i);			
		}
		this.setContentView(R.layout.activity_main);
		oContexto=getApplicationContext();
		oCorreo=(EditText)findViewById(R.id.txtCorreo);
		oClave=(EditText)findViewById(R.id.txtLogin);
		oTerminos=(CheckBox)findViewById(R.id.terminos);
		
		cd = new ConnectionDetector(getApplicationContext());
		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			Toast.makeText(getApplicationContext(), R.string.msgProblemasConexion, Toast.LENGTH_LONG).show();

			// stop executing code by return
			return;
		}

		Button oBtnRegistrar=(Button)findViewById(R.id.btnRegistro);
		oBtnRegistrar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(getApplicationContext(), CtrlNuevoUsuario.class);
				startActivity(i);			
			}
		});
		
		Button oBtnEntrar=(Button)findViewById(R.id.btnEntrar);
		oBtnEntrar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String sCorreo=oCorreo.getText().toString();
				String sClave=oClave.getText().toString();
				if (sClave.trim().length()>0 && sCorreo.trim().length()>0 && oTerminos.isChecked()){
					if (sCorreo.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){

						//Chequemos si está instalado Google Play Services
						GcmUtilities gcmUtilities=new GcmUtilities(oContexto,getParent());
//						GcmUtilities gcmUtilities=new GcmUtilities(oContexto,Login.this);
						if (gcmUtilities.checkPlayServices()){
							gcm=GoogleCloudMessaging.getInstance(Login.this);
							TareaRegistroGCM tarea=new TareaRegistroGCM();
							parametros = new ArrayList<NameValuePair>();
							parametros.add(new BasicNameValuePair("email", sCorreo));
							parametros.add(new BasicNameValuePair("clave", sClave));				
							
							tarea.execute(sCorreo);
						}
					}else{
						Toast.makeText(getApplicationContext(), R.string.msgCorreoNoValido, Toast.LENGTH_LONG).show();
						Log.d(CommonUtilities.TAG, "Correo no valido");
					}					
				}else{
					Toast.makeText(getApplicationContext(), R.string.msgFaltanDatos, Toast.LENGTH_LONG).show();
					Log.d(CommonUtilities.TAG, "Faltan Datos");					
				}
			}
		});
	}
	
	private class TareaRegistroGCM extends AsyncTask<String, Integer, String>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mensaje= new ProgressDialog(Login.this);
			mensaje.setMessage("Registrando Dispositivo...");
			mensaje.setIndeterminate(false);
			mensaje.setCancelable(true);
			mensaje.show();
		}
		
		

		@Override
		protected void onPostExecute(String result) {
			mensaje.dismiss();
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
		}

		@Override
		protected String doInBackground(String... params) {
        	String msg = "";            
	        try{
	            if (gcm == null){
	                gcm = GoogleCloudMessaging.getInstance(oContexto);
	            }	            
	            //Nos registramos en los servidores de GCM
	            regid = gcm.register(SENDER_ID);
	            
	            Log.d(TAG, "Registrado en GCM: registration_id=" + regid);
	
	            //Nos registramos en nuestro servidor
				parametros.add(new BasicNameValuePair("idReg", regid));				
				String resp=CustomHttpClient.ejecutarPost(getString(R.string.servidor), parametros);
				Log.d(TAG, "Respuesta del Server: "+resp);
				JSONObject ob=new JSONObject(resp);
				
				Log.d(TAG, "Primer valor"+ ob.getString("usuarioEncontrado"));
				Log.d(TAG, "Segundo valor"+ ob.getString("equipoRegistrado"));
				msg=ob.getString("mensaje");
				if (ob.getInt("usuarioEncontrado")==1 && ob.getInt("equipoRegistrado")==1){
					pref.registrarPreferencia(params[0], regid,CommonUtilities.getDatePhone());
					Log.d(TAG, "Se guardarón las preferencias");
					
					//Aqui se realizo el cambio para mandar al menú
//					Intent i=new Intent(getApplicationContext(), CtrlVehiculo.class);
					Intent i=new Intent(getApplicationContext(), CtrlMenu.class);

					startActivity(i);			

				}
				
	        } catch (IOException ex){
	                Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
	        } catch (JSONException e) {
	        	Log.e(TAG, "Problemas con el JSON"+e.getMessage());
			}
	        
	        return msg;

		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}


}
