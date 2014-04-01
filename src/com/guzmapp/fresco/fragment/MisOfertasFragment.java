package com.guzmapp.fresco.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.guzmapp.fresco.ConnectionDetector;
import com.guzmapp.fresco.R;
import com.guzmapp.fresco.controladores.CtrlDetalle;
//import com.guzmapp.fresco.controladores.CtrlAcercaDe;
//import com.guzmapp.fresco.controladores.CtrlCategoria;
//import com.guzmapp.fresco.controladores.CtrlNuevoVehiculo;
//import com.guzmapp.fresco.controladores.CtrlPresentacion;
import com.guzmapp.fresco.entidades.Comunicador;
import com.guzmapp.fresco.entidades.Oferta;
import com.guzmapp.fresco.adaptadores.ItemOfertaAdapter;
import com.guzmapp.fresco.modelos.CustomHttpClient;
import com.guzmapp.fresco.modelos.Preferencias;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Clase que implementa el fragmento relacionado con mis vehiculos
 * @author Ing.Henry Guzmán
 * @version 1.0.0 2014-02-16
 * @since 2014-02-16
 *
 */
public class MisOfertasFragment extends Fragment {
	//Inicialización de Variables y Propiedades
	private Preferencias pref;
	private JSONArray respJSON;
	private ProgressDialog mensaje;
	private ListView miLista;
	private List<Oferta> listaVehiculos;
	private ItemOfertaAdapter aa;
	private Intent i;
	private Bundle bund;
	private ConnectionDetector cd;
	private Context contexto;
	
	public MisOfertasFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_misofertas, container, false);
        
		miLista=(ListView)rootView.findViewById(R.id.lstVehiculos);
		listaVehiculos = new ArrayList<Oferta>();
		
		contexto=rootView.getContext();
		
		aa=new ItemOfertaAdapter(contexto, listaVehiculos);
		
		miLista.setAdapter(aa);
		miLista.setOnItemClickListener(clickLista);
		pref=new Preferencias(contexto);

		registerForContextMenu(miLista);
		consultar();
		
		return rootView;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
//		super.onCreateOptionsMenu(menu, inflater);
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.menu_new:
/*			Intent i=new Intent(contexto, CtrlNuevoVehiculo.class);
			startActivity(i);			
            Log.i("ActionBar", "Nuevo!");
*/            return true;
        case R.id.action_settings:
            Log.i("ActionBar", "Settings!");;
            return true;
        case R.id.refrescar:
        	consultar();
            Log.i("ActionBar", "Refrescar!");;
            return true;
        case R.id.action_email:
        	reportarEmail();
            Log.i("ActionBar", "Emial!");;
            return true;
        case R.id.action_acercade:
/*			Intent in=new Intent(contexto, CtrlAcercaDe.class);
			startActivity(in);			
        	
            Log.i("ActionBar", "Emial!");;
*/            return true;
        default:
            return super.onOptionsItemSelected(item);
    }

	}

	private void consultar(){
		cd = new ConnectionDetector(contexto);
		// Check if Internet present
		if (cd.isConnectingToInternet()) {
			ConsultarVehiculos consultar=new ConsultarVehiculos();
			consultar.execute();		

		}else{
			// Internet Connection is not present
			Toast.makeText(contexto, R.string.msgProblemasConexion, Toast.LENGTH_LONG).show();
			
		}

	}

	private class ConsultarVehiculos extends AsyncTask<Void, Integer, Integer>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mensaje= new ProgressDialog(contexto);
			mensaje.setMessage(getString(R.string.msgBuscando));
			mensaje.setIndeterminate(false);
			mensaje.setCancelable(true);
			mensaje.show();
		}

		@Override
		protected void onPostExecute(Integer result) {
			mensaje.dismiss();
	    	aa.notifyDataSetChanged();
	    	if (result==1){
	    		Toast.makeText(contexto, "No registra ningun vehiculo adicione o actualice", Toast.LENGTH_LONG).show();
	    	}

		}

/*		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
	    	aa.notifyDataSetChanged();

		}
*/
		@Override
		protected Integer doInBackground(Void... params) {
			int respuesta=0;
			List<NameValuePair> parametros = new ArrayList<NameValuePair>();
			parametros.add(new BasicNameValuePair("email", pref.getCorreo()));

			try {
				String resp=CustomHttpClient.ejecutarGet(getString(R.string.srvConsultaOferta));
				listaVehiculos.clear();
				if (!resp.equals(null)){
					respJSON = new JSONArray(resp);
					int contador=0;
					for (int i=0; i<respJSON.length();i++){						
						JSONObject objeto=respJSON.getJSONObject(i);
/*						if (objeto.getInt("action")==1){
							respuesta=1;
							break;
						}
*/						contador++;
						Oferta oferta=new Oferta(i, objeto.getString("PkOferta").toString(), objeto.getString("Oportunidad").toString(),objeto.getString("Ubicacion").toString(),objeto.getString("FechaVencimiento").toString(),objeto.getString("EdadObejtivo").toString(),objeto.getString("UrlFuente").toString(),objeto.getString("EntidadNombre").toString());
						
/*						vehiculo.setFecha(pref.getFecha());
						vehiculo.setPago(objeto.getInt("pago"));
						Log.d("PAGO",objeto.getString("pago"));
*/						listaVehiculos.add(oferta);
//				    	aa.notifyDataSetChanged();

//						publishProgress(0);
						Log.d("Ciclo", "Entro Al ciclo");
					}
					pref.registrarNumeroVehiculos(contador);
				}
			} catch (ClientProtocolException e) {
				Log.e(MisOfertasFragment.class.getName(), "Error en Cliente Protocolo:"+e.getMessage());
			} catch (IOException e) {
				Log.e(MisOfertasFragment.class.getName(), "Error IO:"+e.getMessage());
			} catch (JSONException e) {
				Log.e(MisOfertasFragment.class.getName(), "Error Al Construir el Json:"+e.getMessage());
			}

			return respuesta;
		}
		
	}
	
	
	//Envia los datos al formulario de categorias
		private OnItemClickListener clickLista=new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				Oferta v=listaVehiculos.get(arg2);

/*				Fragment fragment=new CategoriasFragment();
				Bundle bundle=new Bundle(4);
				bundle.putString("txtPlaca", v.getPlaca());
				bundle.putString("vin", v.getVin());
				bundle.putString("tipo", v.getTipoDoc());
				bundle.putString("doc", v.getCedula());
				fragment.setArguments(bundle);			

				
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
*/				
				i= new Intent(contexto, CtrlDetalle.class);
				
//				Log.d("PAGO ANTES DE ENVIAR", String.valueOf(v.getPago()));
				Comunicador.setObject(v);
				
/*				bund = new Bundle();

				bund.putString("txtPlaca", v.getPlaca());
				bund.putString("vin", v.getVin());
				bund.putString("tipo", v.getTipoDoc());
				bund.putString("doc", v.getCedula());
				bund.putInt("pago", v.getPago());
				i.putExtras(bund);
*/
				startActivity(i);
			}
		};
		
		private void reportarEmail() {
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_address)});
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.email_text_general));
			startActivity(Intent.createChooser(emailIntent, getString(R.string.email_intent)));
		}

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
			super.onCreateContextMenu(menu, v, menuInfo);
			
			
// ojo			getActivity().getMenuInflater().inflate(R.menu.categorias,menu);
//			menu.setHeaderTitle(R.string.choose);
		}

/*		@Override
		public boolean onContextItemSelected(MenuItem item) {
			AdapterContextMenuInfo info=(AdapterContextMenuInfo) item.getMenuInfo();
			int itemId = item.getItemId();
			switch (itemId) {
				case R.id.menu_eliminar:
//					Vehiculo vehiculoBorrar=listaVehiculos.get((int) info.id);
					Log.d("ID LISTA",String.valueOf(info.id));
				//Ver informacion del elemento
				break;
			}

			return true;
		}
*/
}
