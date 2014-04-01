package com.guzmapp.fresco.modelos;

import static com.guzmapp.fresco.modelos.CommonUtilities.TAG;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class GcmUtilities {
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private Context oContexto;
	private Activity oActividad;
	
	public GcmUtilities(Context contexto, Activity actividad) {
		oContexto=contexto;
		oActividad=actividad;
	}
	
	public Boolean checkPlayServices(){
		int resultCode= GooglePlayServicesUtil.isGooglePlayServicesAvailable(oContexto);
		if (resultCode != ConnectionResult.SUCCESS){
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
				GooglePlayServicesUtil.getErrorDialog(resultCode, oActividad, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			}else{
		        Log.i(TAG, "Dispositivo no soportado.");
		        oActividad.finish();
			}
			return false;
		}
		return true;
	}

}
