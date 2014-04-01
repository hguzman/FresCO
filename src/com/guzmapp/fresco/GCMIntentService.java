package com.guzmapp.fresco;

import com.guzmapp.fresco.controladores.CtrlMenu;
import com.guzmapp.fresco.modelos.CommonUtilities;
import com.guzmapp.fresco.modelos.Preferencias;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class GCMIntentService extends IntentService {
	private static final int NOTIF_ALERTA_ID = 1;
	private Preferencias pref;
	
	public GCMIntentService() {
		super("GCMIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		//Recibe el Mensaje
		String messageType = gcm.getMessageType(intent);
        Bundle extras = intent.getExtras();
        pref=new Preferencias(getApplicationContext());
        
        if (!extras.isEmpty()){  
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)){
            		pref.registrarNotificacion(extras.getString("placa"),CommonUtilities.getDatePhone());
                    mostrarNotification(extras.getString("product"));
            }
        }
        
        GCMBroadcastReceiver.completeWakefulIntent(intent);
	}
	
	private void mostrarNotification(String msg){
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); 

		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)  
                .setSmallIcon(R.drawable.ic_logocarsoknotifi)  
                .setContentTitle("FresCO")  
                .setContentText(msg);

		Intent notIntent =  new Intent(this, CtrlMenu.class);    
		PendingIntent contIntent = PendingIntent.getActivity(this, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT); 
				
        mBuilder.setContentIntent(contIntent);
        mBuilder.setAutoCancel(true);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        mBuilder.setVibrate(pattern);        
		mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());

//		Notification noti = mBuilder.build();
//        noti.flags |= Notification.FLAG_AUTO_CANCEL;
		 // Play default notification sound
//        noti.defaults |= Notification.DEFAULT_SOUND;
        
        // Vibrate if vibrate is enabled
//        noti.defaults |= Notification.DEFAULT_VIBRATE;		

	}
	
}
