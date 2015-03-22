package com.guzmapp.fresco.adaptadores;

import java.util.List;

import com.guzmapp.fresco.R;
import com.guzmapp.fresco.entidades.Oferta;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
//import android.widget.ImageView;
import android.widget.TextView;

/**
 * Crea el adaptador que se asigna a la lista
 * @author Ing. Henry Guzman
 * @version 1.0.1 2014-02-16
 * @since 2014-02-16
 */
public class ItemOfertaAdapter extends BaseAdapter {
	private Context contexto;
	private List<Oferta> items;
	
	public ItemOfertaAdapter(Context a, List<Oferta> vs) {
		this.contexto=a;
		this.items=vs;
	}

	@Override
	public int getCount() {
		if (items!=null){
			return items.size();
		}else{
			return 0;			
		}
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Oferta oferta=(Oferta)getItem(position);
		if (convertView==null){
			convertView=View.inflate(this.contexto, R.layout.lista_ofertas, null);
		}
		TextView entidad=(TextView)convertView.findViewById(R.id.txtEntidad);
		TextView ubicacion=(TextView)convertView.findViewById(R.id.txtUbicacion);
		TextView edad=(TextView)convertView.findViewById(R.id.txtEdad);
//		TextView bien=(TextView)convertView.findViewById(R.id.txtBien);
		TextView fecha=(TextView)convertView.findViewById(R.id.txtFecha);
//		ImageView noti=(ImageView)convertView.findViewById(R.id.imgNotificacion);
		
		entidad.setText(oferta.getEntidadNombre());
		ubicacion.setText(oferta.getUrlFuente());
		edad.setText(oferta.getEdadObjetivo());				
		fecha.setText(oferta.getFechaVencimiento());
		
/*		if (veh.getVehiculoNotificado().equals(veh.getPlaca())){
			noti.setVisibility(View.VISIBLE);
			fecha.setVisibility(View.VISIBLE);
		}else{
			noti.setVisibility(View.INVISIBLE);
			fecha.setVisibility(View.INVISIBLE);
		}
*/			
		return convertView;
	}

}
