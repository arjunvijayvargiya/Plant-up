package org.bitspilani.plantup.plantup;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class NewProject extends ActionBarActivity {
    String[] presidents = {
            "Small",
            "Medium",
            "Large",
            "Very Large",
            "RoofTop"
    };
    Spinner s1;
    EditText et1,et2,et3;
    String str1,str2,str3;
    Button b;
    TextView tv1,tv2;
    DataBaseHelper myDbHelper;
    private String url1 = "http://api.openweathermap.org/data/2.5/weather?q=";
    int auto = 1;
    String[] employees = new String[2000];
    String[] phn=new String[2000];
    private HandleJSON obj;
    String Temp,Humid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        s1 = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,presidents);
        s1.setAdapter(adapter);
        myDbHelper = new DataBaseHelper(this);
        et1= (EditText) findViewById(R.id.projnameet);
        et2= (EditText) findViewById(R.id.cityet);
        et3= (EditText) findViewById(R.id.budgetet);
        tv1=(TextView)findViewById(R.id.finalresult);
        tv2=(TextView)findViewById(R.id.prefinalresult);
        //et4=(EditText)findViewById(R.id.result);
        str1=et1.getText().toString();
        str2=et2.getText().toString();
        str3=et3.getText().toString();

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                /*index = s1.getSelectedItemPosition();
                name.setText(employees[index]);
                phone.setText(phn[index]);*/
    	/*Toast.makeText(getBaseContext(),
    	"You have selected item : " + phn[index],
    	Toast.LENGTH_SHORT).show();*/


            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        try {

            myDbHelper.createDataBase();
            //Toast.makeText(getBaseContext(), "hey your server has accessed",Toast.LENGTH_LONG).show();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }
        try {

            myDbHelper.openDataBase();
            Toast.makeText(getBaseContext(), "superb",Toast.LENGTH_LONG).show();
        }catch(SQLException sqle){
            Toast.makeText(getBaseContext(), "not working",Toast.LENGTH_LONG).show();
            throw sqle;

        }
        b=(Button)findViewById(R.id.submitbutton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetContact();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void GetContact() {
        //---get a contact---

        Cursor c = myDbHelper.getContact(2);
        if (c.moveToFirst())
            DisplayContact(c);
        else
            Toast.makeText(this, "No contact found", Toast.LENGTH_LONG).show();
      //  myDbHelper.close();
    }
    public void DisplayContact(Cursor c)
    {
       /* Toast.makeText(this,
                "id: " + c.getString(0) + "\n" +
                        "Name: " + c.getString(1) + "\n" +
                        "Email: " + c.getString(2),
                Toast.LENGTH_LONG).show();*/
        String str=""+c.getString(0)+"NAME:"+c.getString(1)+"\nTemperature"+c.getString(2)+"\nprocedure"+c.getString(3)+"\nRegion"+c.getString(4)+"\nArea"+c.getString(5)+"\nBudget"+c.getString(6);
       // et4.setScroller(new Scroller(getApplicationContext()));
        //et4.setMaxLines(30);
        //et4.setVerticalScrollBarEnabled(true);
        //et4.setMovementMethod(new ScrollingMovementMethod());
        isOnline();
        String url = et2.getText().toString();
        if(url.equals(""))
        {
            Toast.makeText(getApplicationContext(),"city name is NULL",Toast.LENGTH_LONG).show();
        }
        else {
            String finalUrl = url1 + url;
            try {
                //country.setText(finalUrl);
                obj = new HandleJSON(finalUrl);
                obj.fetchJSON();

                while (obj.parsingComplete) ;
                //country.setText(obj.getCountry());
                float tempo = Float.parseFloat(obj.getTemperature());
                Temp = tempo - 273.15 + "deg cel";
                //humidity.setText(obj.getHumidity()+"%");
                float info = Float.parseFloat(obj.getHumidity());
                if (info > 30.0 && info < 60.0)
                    // tv.setText("Normal Humidity Level detected");
                    Humid = "Normal Humidity Level detected";
                else if (info >= 60.0)
                    //tv.setText("Excess Humidity Level detected");
                    Humid = "Excess Humidity Level detected";
                else
                    //tv.setText("Less Humidity Level detected");
                    Humid = "Excess Humidity Level detected";
                //pressure.setText(obj.getPressure()+"bar");
            } catch (Exception ex) {
                tv2.setText("city name is incorrect");
            }

            tv2.setText(Temp + "\n" + Humid);
            tv1.setText(str);
        }
    }
   /* public void GetContacts() {
        //--get all contacts---

        Cursor c = myDbHelper.getAllContacts();
        if (c.moveToFirst())
        {
            do {employees[i]=c.getString(1);
                phn[i]=c.getString(3);
                i++;
                //DisplayContact(c);

            } while (c.moveToNext());
        }
        myDbHelper.close();
    }*/
   public void isOnline() {
       ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo netInfo = cm.getActiveNetworkInfo();
       if (netInfo != null && netInfo.isConnectedOrConnecting()) {
           Toast.makeText(getApplicationContext(),"Internet Connectivity Present", Toast.LENGTH_LONG).show();
       }
       else {
           Toast.makeText(getApplicationContext(), "Internet Connectivity not Present", Toast.LENGTH_LONG).show();
           Intent i=new Intent(NewProject.this,MainActivity.class);
           startActivity(i);
       }
     }
}
