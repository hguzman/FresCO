package com.guzmapp.fresco.modelos;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;


public class CustomHttpClient {
	public static final int HTTP_TIMEOUT=30 * 1000;
	private static HttpClient miHttpCliente;
	
	private static HttpClient obtenerHttpCliente(){
		if (miHttpCliente==null){
			miHttpCliente= new DefaultHttpClient();
			final HttpParams par= miHttpCliente.getParams();
			HttpConnectionParams.setConnectionTimeout(par, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(par, HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(par, HTTP_TIMEOUT);			
		}
		return miHttpCliente;
	}
	
	public static String ejecutarPost(String url, List<NameValuePair> lista) throws ClientProtocolException, IOException{
		HttpClient cliente=obtenerHttpCliente();
		HttpPost post= new HttpPost(url);
		UrlEncodedFormEntity formEntity= new UrlEncodedFormEntity(lista);
		post.setEntity(formEntity);
		HttpResponse respuesta=cliente.execute(post);
		HttpEntity entidad=respuesta.getEntity();
		String respString= EntityUtils.toString(entidad);
		return respString;
	}

	public static String ejecutarGet(String url) throws ClientProtocolException, IOException{
		HttpClient cliente=obtenerHttpCliente();
		HttpGet get=new HttpGet();
		
		URI pagina;
		try {
			pagina = new URI(url);
			get.setURI(pagina);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		HttpResponse respuesta=cliente.execute(get);
		HttpEntity entidad=respuesta.getEntity();
		String respString= EntityUtils.toString(entidad);
		return respString;
	}
	
}
