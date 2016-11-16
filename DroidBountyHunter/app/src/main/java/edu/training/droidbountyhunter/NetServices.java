package edu.training.droidbountyhunter;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by Eden on 17/08/2016.
 */
public class NetServices extends AsyncTask <String, Void, Object>{

    private static final String URL_WS1 = "http://201.168.207.210/services/droidbhservices.svc/fugitivos";
    private static final String URL_WS2 = "http://201.168.207.210/services/droidbhservices.svc/atrapados";
    private static final String URL_INSERTLOCATION = "http://201.168.207.210/Services/GoogleMaps.svc/AltaLocation";

    private IOnTaskCompleted listener;
    private Exception exception;

    public NetServices(IOnTaskCompleted listener)
    {
        exception = null;
        this.listener = listener;
    }

    public static String convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try{
            while ((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try{
                is.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public static String connectGET(String purl) throws IOException{
        URL url = new URL(purl);
        URLConnection urlConnection = url.openConnection();
        String sRes = "";
        try{
            HttpURLConnection httpurlconnection = (HttpURLConnection) urlConnection;
            httpurlconnection.setRequestMethod("GET");
            httpurlconnection.setDoOutput(false);
            httpurlconnection.connect();

            if(httpurlconnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream instream = httpurlconnection.getInputStream();
                String result = convertStreamToString(instream);
                Log.w("[CHECK]", result);
                sRes = result;

                instream.close();
            }
        }
        catch (Exception e){
            Log.w("[CHECK]", e.toString());
        }
        return sRes;
    }

    public static String connectPOST(String purl, JSONObject jobject) throws IOException{
        URL url = new URL(purl);
        URLConnection urlConnection = url.openConnection();
        String sRes = "";

        try{
            HttpURLConnection httpurlconnection = (HttpURLConnection) urlConnection;
            httpurlconnection.setRequestMethod("POST");
            httpurlconnection.setRequestProperty("Content-Type", "application/json");
            httpurlconnection.setDoOutput(true);
            httpurlconnection.connect();

            OutputStreamWriter writer = new OutputStreamWriter(httpurlconnection.getOutputStream());

            /*JSONObject jobject = new JSONObject();


            for(int i=0; i < parametros.length; i++) {
                //jobject.put("UDIDString", udid);
                jobject.put(parametros[i][0], parametros[i][1]);
            }*/


            String urlParameters = jobject.toString();
            writer.write(urlParameters);
            writer.flush();

            if(httpurlconnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream instream = httpurlconnection.getInputStream();
                String result = convertStreamToString(instream);
                Log.w("[CHECK]", result);
                sRes = result;

                instream.close();
            }
        }
        catch (Exception e){
            Log.w("[CHECK]", e.toString());
        }
        return sRes;
    }

    @Override
    protected Object doInBackground(String... urls) {
        Object x = null;
        String sResp = "";

        if(urls[0] == "Fugitivos"){
            try{
                sResp = NetServices.connectGET(URL_WS1);

                String[] aFujs = null;

                JSONArray jaData = new JSONArray(sResp);
                aFujs = new String[jaData.length()];
                for(int i=0; i<jaData.length(); i++){
                    JSONObject joFuj = jaData.getJSONObject(i);
                    aFujs[i] = joFuj.getString("name");
                }

                if(aFujs != null){

                    for(int i = 0; i < aFujs.length; i++){
                        Home.oDB.InsertFugitivo(aFujs[i]);
                    }

                }

                x = aFujs;

            }
            catch (Exception e){
                exception = e;
            }
        }

        else if(urls[0] == "Atrapar"){
            try{

                JSONObject obj = new JSONObject();
                obj.put("UDIDString", urls[1]);

                sResp = NetServices.connectPOST(URL_WS2, obj);
                String sMsg = "";

                JSONObject jaData = new JSONObject(sResp);
                sMsg = jaData.getString("mensaje");
                x=sMsg;
            }
            catch (Exception e){
                exception = e;
            }
        }
        else if(urls[0] == "InsertarUbicacion"){
            try{

                JSONObject obj = new JSONObject();
                obj.put("sDevice", urls[1]);
                obj.put("sImagen", urls[2]);
                obj.put("sLatitud", urls[3]);
                obj.put("sLongitud", urls[4]);
                obj.put("sNombreFugitivo", urls[5]);

                sResp = NetServices.connectPOST(URL_INSERTLOCATION, obj);
                String sMsg = "";

                JSONObject jaData = new JSONObject(sResp);
                sMsg = jaData.getString("iIdentificadorLocalizacion");
                x=sMsg;
            }
            catch (Exception ex){
                exception = ex;
            }
        }

        return x;
    }

    protected void onPostExecute(Object feed){
        if(exception == null){
            listener.onTaskCompleted(feed);
        }
        else{
            listener.onTaskError(exception.toString());
        }
    }
}
