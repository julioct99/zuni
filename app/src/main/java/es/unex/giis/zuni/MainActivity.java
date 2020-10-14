package es.unex.giis.zuni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    double latt, longt;

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        TextView texto = findViewById(R.id.texto);
        String respuesta = "";
        double lat=0, lng=0;

        try {
            String webService = "https://geocode.xyz/Puerto%20hurraco?json=1";
            URL url = new URL(webService);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            // System.out.println(conn.getResponseCode());

            if (conn.getResponseCode() == 200)
            {


                InputStream instream = conn.getInputStream();
                String result = convertStreamToString(instream);


                JSONObject obj = new JSONObject(result);

                latt = Double.parseDouble(obj.getString("latt"));
                longt = Double.parseDouble(obj.getString("longt"));

                String webService1 = "https://api.openweathermap.org/data/2.5/weather?lat="+ latt+ "&lon="+longt+"&appid=55ab2d28aad932680b93bf96e8e44f6e";
                URL url1 = new URL(webService1);
                HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
                conn1.setRequestMethod("GET");
                conn1.setRequestProperty("Accept", "application/json");

                if (conn1.getResponseCode() == 200) {


                    InputStream instream1 = conn1.getInputStream();
                    String result1 = convertStreamToString(instream1);
                    texto.setText(result1);

                }
                else{
                    texto.setText("mal");
                }


            }



           // System.out.println("lat: " + lat);
           // System.out.println("lng: " + lng);


        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.openweathermap.org/data/2.5/weather?q="+ciudad+"&appid=55ab2d28aad932680b93bf96e8e44f6e")).build();
//        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
//                .thenApply(HttpResponse::body)
//                .thenAccept(Prueba::copia)
//                .join();




    }
}