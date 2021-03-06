package com.wishihab.widefend;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FindingIP extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private TextView tvJson;
    private EditText edip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_ip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edip = (EditText) findViewById(R.id.edit_ipin);
        tvJson = (TextView) findViewById(R.id.textJsonHere);
        Button btfind = (Button) findViewById(R.id.but_findip);

        btfind.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new JSONTask().execute("http://ip-api.com/json/"+edip.getText());
                }
            });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public class JSONTask extends AsyncTask<String,String,String> {

        protected String doInBackground(String... params){
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try{
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line="";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String jsonAPI = buffer.toString();

                JSONObject parentObject = new JSONObject(jsonAPI);
                String APIas = parentObject.getString("as");
                String APIcity = parentObject.getString("city");
                String APIcountry = parentObject.getString("country");
                double APIlat = parentObject.getDouble("lat");
                double APIlon = parentObject.getDouble("lon");
                String APIisp = parentObject.getString("isp");
                String APIorg = parentObject.getString("org");
                String APIquery = parentObject.getString("query");
                String APIrname = parentObject.getString("regionName");
                String APIstatus = parentObject.getString("status");
                String APItimezone = parentObject.getString("timezone");


                return  "AsKnow : " + APIas + "\nISP : " + APIisp + "\nCity : " + APIcity +
                        "\nCountry : " + APIcountry + "\nOrg : " + APIorg + "\nIP : "
                        + APIquery + "\nRegion Name : " + APIrname + "\nTimeZone : "+ APItimezone
                        +"\nlat : "+ APIlat + ",lon " + APIlon + "\nStatus : " + APIstatus;

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }finally {
                if(connection !=null){
                    connection.disconnect();
                }
                try{
                    if(reader !=null){
                        reader.close();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(String result){
            super.onPostExecute(result);
            tvJson.setText(result);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareDes = "WIDefend - App to monitoring your device from trojan and unknown connection. Download github.com/wishihab";
            intent.putExtra(Intent.EXTRA_TEXT,shareDes);
            startActivity(Intent.createChooser(intent, "Share with"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
