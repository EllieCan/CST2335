package com.example.algonquin.androidlabs;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
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
import java.net.URL;

public class WeatherForecast extends Activity {
    public static final String ACTIVITY_NAME = "WeatherForecast";

    private ProgressBar myProgressBar;
    private TextView min;
    private TextView max;
    private TextView current;
    private ImageView icon;
    private static String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "oncreate() is called");
        setContentView(R.layout.activity_weather_forecast);

        min = findViewById(R.id.min_temperature);
        max = findViewById(R.id.max_temperature);
        current = findViewById(R.id.current_temperature);
        myProgressBar = findViewById(R.id.progressBar);
        icon = findViewById(R.id.current_weather_imageView);
        new ForecastQuery().execute();
    }

    public class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String minTemp;
        private String maxTemp;
        private String curtTemp;
        private Bitmap curtWeather;

        @Override
        protected String doInBackground(String... strings) {
            try{
                Log.i("doinbackground", "do in the background is called");
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = parser.getName();
                    Log.i("tagname", "tag name is: " +name);

                    if (name.equals("temperature")) {
                        minTemp = parser.getAttributeValue(null, "min");
                        Log.i("doinbackgroundmin", "minimum: " + minTemp);
                        publishProgress(25);
                        maxTemp = parser.getAttributeValue(null, "max");
                        Log.i("doinbackgroundmax", "maximum: " +maxTemp);
                        publishProgress(50);
                        curtTemp = parser.getAttributeValue(null, "value");
                        Log.i("doinbackgroundcurrent", "current temp: " +curtTemp);
                        publishProgress(75);
                    }

                    if (name.equals("weather")) {
                        String iconName = parser.getAttributeValue(null, "icon");
                        if(fileExistance(iconName + ".png")){
                            Log.i(iconName + ".png", "The current weather image is already downloaded");
                            FileInputStream fis = null;
                            try {
                                fis = openFileInput(iconName + ".png");
                                curtWeather = BitmapFactory.decodeStream(fis);
                                publishProgress(100);
                            } catch (FileNotFoundException e) {
                                if(fis != null){
                                    fis.close();
                                }
                                e.printStackTrace();
                            }
                        }else {
                            String imageURL = "http://openweathermap.org/img/w/" + iconName + ".png";
                            curtWeather  = getImage(imageURL);
                            publishProgress(100);
                            FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                            curtWeather.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                        }
                    }


                }

            }catch (IOException | XmlPullParserException e){
                Log.i("xmlerror", "Parse xml error");

            }

            return null;
        }
        Bitmap getImage(String image) throws IOException {
            URL imageURL = new URL(image);
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) imageURL.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else{
                    return null;
                }
            }catch (Exception e) {
                    return null;
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            myProgressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            min.setText(minTemp);
            max.setText(maxTemp);
            current.setText(curtTemp);
            icon.setImageBitmap(curtWeather);
            myProgressBar.setVisibility(View.VISIBLE);
        }
    }
}
