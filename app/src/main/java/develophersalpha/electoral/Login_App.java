package develophersalpha.electoral;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class Login_App extends AppCompatActivity {
    RequestQueue requestQueue; //Para la utilizacion de Volley
    RelativeLayout Cargando;
    EditText textuser,textpass;
    Button btnlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_app);
        textuser = (EditText)findViewById(R.id.edtUser);
        textpass = (EditText)findViewById(R.id.edtPassword);
        btnlogin = (Button)findViewById(R.id.btnLogin);

        Cargando = (RelativeLayout)findViewById(R.id.Layout_Cargando);
        requestQueue = Volley.newRequestQueue(getApplicationContext()); //Libreria de Volley

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!textuser.getText().toString().isEmpty() && !textpass.getText().toString().isEmpty()){
                    //startActivity(new Intent().setClass(Login_App.this,Inicio_App.class));
                    //finish();
                    VerificarLogin verificar = new VerificarLogin();
                    verificar.execute();

                }else{
                    Toast.makeText(getApplicationContext(),"Ingresa los datos que se indican",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private class VerificarLogin extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            Cargando.setVisibility(View.VISIBLE); //Mostramos la precarga
        }
        public void onPostExecute(Void unused) {
        }

        @Override
        protected Void doInBackground(Void... params) {
            StringRequest request = new StringRequest(Request.Method.POST, getString(R.string.master_login), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(!response.equals("null")){
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            String clave = jsonArray.getJSONObject(0).getString("clave");
                            String casilla = jsonArray.getJSONObject(0).getString("casilla");
                            Intent inicio = new Intent().setClass(Login_App.this,Inicio_App.class);
                            inicio.putExtra("clave",clave);
                            inicio.putExtra("casilla",casilla);
                            inicio.putExtra("tabla",textuser.getText().toString());
                            String status="0";

                            if(!jsonArray.getJSONObject(0).getString("pan").matches("0")){
                                status = "1";
                            }

                            inicio.putExtra("status",status);
                            startActivity(inicio);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Cargando.setVisibility(View.INVISIBLE);
                        Toast.makeText(Login_App.this,"EL usuario o contraseña no son correctos.",Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Cargando.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login_App.this,"Verifique que este conectado a internet",Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("user", textuser.getText().toString());
                    parameters.put("pass", textpass.getText().toString());
                    return parameters;
                }
            };
            requestQueue.add(request);
            return null;
        }
    }
}
