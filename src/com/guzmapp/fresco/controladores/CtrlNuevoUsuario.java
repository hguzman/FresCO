package com.guzmapp.fresco.controladores;

import static com.guzmapp.fresco.modelos.CommonUtilities.SENDER_ID;
import static com.guzmapp.fresco.modelos.CommonUtilities.TAG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.guzmapp.fresco.R;
import com.guzmapp.fresco.modelos.CommonUtilities;
import com.guzmapp.fresco.modelos.CustomHttpClient;
import com.guzmapp.fresco.modelos.Preferencias;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class CtrlNuevoUsuario extends Activity {
	private GoogleCloudMessaging gcm;
	private EditText oNombre;
	private EditText oEmail;
	private EditText oClave;
	private List<NameValuePair> parametros;
	private Context oContexto;
	private String regid="";
	private Preferencias pref;
	private ProgressDialog mensaje;
	private String msg = "";            
	private CheckBox oTerminos;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevousuario);
		oNombre=(EditText)findViewById(R.id.txtNombre);
		oEmail=(EditText)findViewById(R.id.txtEmail);
		oClave=(EditText)findViewById(R.id.txtClave);		
		oTerminos=(CheckBox)findViewById(R.id.terminos);
		
		oContexto=getApplicationContext();
		pref=new Preferencias(getApplicationContext());

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.regusuario, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.save:
        	String sEmail=oEmail.getText().toString();
        	String sNombre=oNombre.getText().toString().toUpperCase();
        	String sClave=oClave.getText().toString();
        	int iNombre=sNombre.length();
        	int iClave=sClave.length();
			if (sClave.trim().length()>0 && sEmail.trim().length()>0 && oTerminos.isChecked()){
        	
	        	if (sEmail.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$") && iNombre>0 && iClave>0){
					//Chequemos si está instalado Google Play Services
	//				GcmUtilities gcmUtilities=new GcmUtilities(oContexto,getParent());
	//				if (gcmUtilities.checkPlayServices()){
						gcm=GoogleCloudMessaging.getInstance(CtrlNuevoUsuario.this);
						RegistrarUsuario tarea=new RegistrarUsuario();
						parametros = new ArrayList<NameValuePair>();
						parametros.add(new BasicNameValuePair("email", sEmail));
						parametros.add(new BasicNameValuePair("clave", sClave));
						parametros.add(new BasicNameValuePair("nombre",sNombre));
						
						tarea.execute(sEmail);
	//				}
	
	        	}else{
					Toast.makeText(getApplicationContext(), R.string.msgCorreoNoValido, Toast.LENGTH_LONG).show();
					Log.d(CommonUtilities.TAG, "Correo no valido");
	        	}
			}else{
				Toast.makeText(getApplicationContext(), R.string.msgFaltanDatos, Toast.LENGTH_LONG).show();
				Log.d(CommonUtilities.TAG, "Faltan Datos");					
			}
			
            Log.i("ActionBar", "Guardar!");
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
	}

	private class RegistrarUsuario extends AsyncTask<String, Integer, String>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mensaje= new ProgressDialog(CtrlNuevoUsuario.this);
			mensaje.setMessage("Registrando Dispositivo...");
			mensaje.setIndeterminate(false);
			mensaje.setCancelable(true);
			mensaje.show();
		}
		
		

		@Override
		protected void onPostExecute(String result) {
			mensaje.dismiss();
			if (msg.length()>0){
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();				
			}else{
				Toast.makeText(getApplicationContext(), getString(R.string.msgProblemasRegistro), Toast.LENGTH_LONG).show();								
			}
		}

		@Override
		protected String doInBackground(String... params) {
	        try{
	            if (gcm == null){
	                gcm = GoogleCloudMessaging.getInstance(oContexto);
	            }	            
	            //Nos registramos en los servidores de GCM
	            regid = gcm.register(SENDER_ID);
	            
	            Log.d(TAG, "Registrado en GCM: registration_id=" + regid);
	
	            //Nos registramos en nuestro servidor
				parametros.add(new BasicNameValuePair("idReg", regid));				
				String resp=CustomHttpClient.ejecutarPost(getString(R.string.srvRegistroUsuario), parametros);
				Log.d(TAG, "Respuesta del Server: "+resp);
				JSONObject ob=new JSONObject(resp);
				
				Log.d(TAG, "Primer valor"+ ob.getString("usuarioRegistrado"));
				Log.d(TAG, "Segundo valor"+ ob.getString("equipoRegistrado"));
				msg=ob.getString("mensaje");
				
				if (ob.getInt("usuarioRegistrado")==1 && ob.getInt("equipoRegistrado")==1){
					pref.registrarPreferencia(params[0], regid,CommonUtilities.getDatePhone());
					Log.d(TAG, "Se guardarón las preferencias");
					
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
