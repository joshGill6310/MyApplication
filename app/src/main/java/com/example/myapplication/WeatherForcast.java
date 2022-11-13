package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForcast extends AppCompatActivity {
    private String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
    private String ACTIVITY_NAME ="WeatherForcast";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forcast);
        ProgressBar pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
    }

    private class ForcastQuery extends AsyncTask<String, Integer, String> {
        String CurrentTemperature;
        String MaxTemperature;
        String MinTemperature;
        Bitmap image;

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection conn = null;
            try {
                URL url = new URL(urlString);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                InputStream in = conn.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();
                while (parser.getName() != "Temperature") {
                    parser.next();
                }
                String value = "";
                String min = "";
                String max = "";
                value = parser.getAttributeValue(null, "value");
                publishProgress(25);
                min = parser.getAttributeValue(null, "min");
                publishProgress(50);
                max = parser.getAttributeValue(null, "max");
                publishProgress(75);
                while (parser.getName() != "Weather") parser.next();
                String imagefile = parser.getAttributeValue(null,"icon")+".png";
                Log.i(ACTIVITY_NAME,"Looking for "+imagefile);
                Bitmap bm;
                if(fileExistance(imagefile)){
                    FileInputStream fis = null;
                    try {    fis = openFileInput(imagefile);   }
                    catch (FileNotFoundException e) {    e.printStackTrace();  }
                    bm = BitmapFactory.decodeStream(fis);
                    Log.i(ACTIVITY_NAME,"Found image, "+imagefile+" in storage");
                }
                else {
                    String iconName = parser.getAttributeValue(null, "icon");

                    String urlIcon = "http://openweathermap.org/img/w/" + iconName + ".png";


                    bm = WeatherForcast.getImage(urlIcon);
                    FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Log.i(ACTIVITY_NAME,"placed image, "+imagefile+" in storage");
                }
                WeatherForcast.this.onPostExecute(min,max,value,bm);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        public void onProgressUpdate(Integer ...value){

            ProgressBar pb = findViewById(R.id.progressBar);
            pb.setProgress(value[0]);
            pb.setVisibility(View.VISIBLE);
        }
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }
        
    }


    public void onPostExecute(String minTemp, String maxTemp, String currTemp,Bitmap Image){
        TextView min = findViewById(R.id.MinTemp);
        TextView max = findViewById(R.id.MaxTemp);
        TextView temp = findViewById(R.id.TemperatureDisplay);
        ImageView Img = findViewById(R.id.imageView3);

        Img.setImageBitmap(Image);
        min.setText(minTemp);
        max.setText(maxTemp);
        temp.setText(currTemp);
        ProgressBar pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
    }
    public static Bitmap getImage(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return BitmapFactory.decodeStream(connection.getInputStream());
            } else
                return null;
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    public static Bitmap getImage(String urlString) {
        try {
            URL url = new URL(urlString);
            return getImage(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
