package com.guzmapp.fresco.controladores;

import com.guzmapp.fresco.R;
import com.guzmapp.fresco.entidades.Comunicador;
import com.guzmapp.fresco.entidades.Oferta;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CtrlDetalle extends Activity {
	private Oferta informacion;
	private TextView placa;
	private TextView desc;
	private TextView fecha;
	private TextView titulo;
	private TextView categoria;
	private ImageView imagen;
	private Bundle bl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalle);
		Object objeto=Comunicador.getObject();
		informacion=(Oferta) objeto;
		
		placa=(TextView)findViewById(R.id.txtPlaca);
		desc=(TextView)findViewById(R.id.txtDescripcion);
		imagen=(ImageView)findViewById(R.id.imagen);
		fecha=(TextView)findViewById(R.id.txtFecha);
		titulo=(TextView)findViewById(R.id.txtTitulo);
		categoria=(TextView)findViewById(R.id.txtCategoria);
		
//		imagen.setImageBitmap(informacion.getFoto());
		placa.setText(informacion.getEntidadNombre());
		desc.setText(informacion.getOportunidad());
		fecha.setText(informacion.getFechaVencimiento().substring(0, 10));
		titulo.setText(informacion.getUbicacion());

//		bl= this.getIntent().getExtras();
		categoria.setText(informacion.getUrlFuente());
		
	}
}
