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

    private String ACTIVITY_NAME ="WeatherForcast";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forcast);
        ProgressBar pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
        ForcastQuery ab=new ForcastQuery();
        ab.execute();
        Log.i(ACTIVITY_NAME,"Started onCreate");
    }

    private class ForcastQuery extends AsyncTask<String, Integer, String> {
        String CurrentTemperature;
        String MaxTemperature;
        String MinTemperature;
        Bitmap image;
        private String urlString = "https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=52091cd5c32fd307bfe1c8d48ed8a7b4&mode=xml&units=metric";

        @Override
        protected String doInBackground(String... strings) {
            Log.i(ACTIVITY_NAME,"Entered do in background");
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
                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    Log.i(ACTIVITY_NAME,"Looping");
                    if(parser.getEventType() ==XmlPullParser.START_TAG){
                        if(parser.getName().equals("temperature")) {

                            String value = "";
                            String min = "";
                            String max = "";
                            CurrentTemperature = parser.getAttributeValue(null, "value");
                            publishProgress(25);
                            MinTemperature = parser.getAttributeValue(null, "min");
                            publishProgress(50);
                            MaxTemperature = parser.getAttributeValue(null, "max");
                            publishProgress(75);
                        }
                        else if(parser.getName().equals("weather")){
                            String imagefile = parser.getAttributeValue(null,"icon")+".png";
                            Log.i(ACTIVITY_NAME,"Looking for "+imagefile);
                            Bitmap bm;
                            if(fileExistance(imagefile)){
                                FileInputStream fis = null;
                                try {    fis = openFileInput(imagefile);   }
                                catch (FileNotFoundException e) {    e.printStackTrace();  }
                                image = BitmapFactory.decodeStream(fis);
                                Log.i(ACTIVITY_NAME,"Found image, "+imagefile+" in storage");
                            } else {


                                String urlIcon = "https://openweathermap.org/img/w/" + imagefile;


                                image = WeatherForcast.getImage(urlIcon);
                                FileOutputStream outputStream = openFileOutput(imagefile, Context.MODE_PRIVATE);
                                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();
                                Log.i(ACTIVITY_NAME,"placed image, "+imagefile+" in storage");
                            }
                        }

                    }
                    Log.i(ACTIVITY_NAME,"Loop Again");
                    parser.next();

                }




            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            finally{
                conn.disconnect();
            }

            return "Done";
        }
        @Override
        public void onProgressUpdate(Integer ...value){

            ProgressBar pb = findViewById(R.id.progressBar);
            pb.setProgress(value[0]);
            pb.setVisibility(View.VISIBLE);
        }

        public void onPostExecute(String Result) {
            TextView min = findViewById(R.id.MinTemp);
            TextView max = findViewById(R.id.MaxTemp);
            TextView temp = findViewById(R.id.TemperatureDisplay);
            ImageView Img = findViewById(R.id.imageView3);

            Img.setImageBitmap(image);
            min.setText(MinTemperature);
            max.setText(MaxTemperature);
            temp.setText(CurrentTemperature);
            ProgressBar pb = findViewById(R.id.progressBar);
            pb.setVisibility(View.VISIBLE);
        }
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }
        
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
