package com.guzmapp.fresco.modelos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;



public final class CommonUtilities {
	// DIrecci??n Web en la cual esta la pagina
  public static final String SERVER_URL = ""; 
  // Google project id
  public static final String SENDER_ID = ""; 

  /**
   * Tag used on log messages.
   */
  public static final String TAG = "GCMCars-Ok";
  
  public static String getDatePhone()

  {

      Calendar cal = new GregorianCalendar();

      Date date = cal.getTime();

      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

      String formatteDate = df.format(date);

      return formatteDate;

  }
  
  public static String eliminarEspacios(String cadenaConEspacios) {
	    StringTokenizer tokenizer = new StringTokenizer(cadenaConEspacios);
	    StringBuilder cadenaSinEspacios = new StringBuilder();
	 
	    while(tokenizer.hasMoreTokens()){
	        cadenaSinEspacios.append(tokenizer.nextToken());
	    }
	 
	    return cadenaSinEspacios.toString();
	}
}
