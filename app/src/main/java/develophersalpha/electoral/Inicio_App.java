package develophersalpha.electoral;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableRow;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Inicio_App extends AppCompatActivity {
    EditText[] puntos;
    RequestQueue requestQueue; //Para la utilizacion de Volley
    RelativeLayout Cargando;
    String clave,tabla;
    TextView estado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_app);
        puntos = new EditText[]{
                findViewById(R.id.edtPan),
                findViewById(R.id.edtPri),
                findViewById(R.id.edtPrd),
                findViewById(R.id.edtPt),
                findViewById(R.id.edtVerde),
                findViewById(R.id.edtMc),
                findViewById(R.id.edtAlianza),
                findViewById(R.id.edtMorena)};
        requestQueue = Volley.newRequestQueue(getApplicationContext()); //Libreria de Volley
        Cargando = (RelativeLayout)findViewById(R.id.Layout_Cargando);
        estado = (TextView)findViewById(R.id.infoStatus);

        Intent intent = getIntent();
        TextView casilla = (TextView)findViewById(R.id.infoCasilla);
        String status = "0";
        clave = intent.getStringExtra("clave");
        tabla = intent.getStringExtra("tabla");
        status = intent.getStringExtra("status");
        casilla.setText(intent.getStringExtra("casilla").toString());
        if(status.equals("1")){
            for(int i=0;i<puntos.length;i++){
                puntos[i].setEnabled(false);
                puntos[i].setVisibility(View.INVISIBLE);
            }
            estado.setText("Validado");
            estado.setTextColor(Color.parseColor("#66BB6A"));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_guardar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.button_guardar) {
            int check=0;
            for (int i=0; i<puntos.length;i++){
                if(puntos[i].getText().toString().isEmpty()){
                    check++;
                }
            }
            if(check == 0){
                showLoginDialog();
            }else{
                Toast.makeText(getApplicationContext(),"Todos los campos no deben estar vacios",Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLoginDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Verificación de envio");
        LayoutInflater inflater = LayoutInflater.from(this);
        View validacion = inflater.inflate(R.layout.dialog_terminos,null);
        final CheckBox validar = (CheckBox) validacion.findViewById(R.id.chkValidacion);
        dialog.setView(validacion);

        dialog.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(validar.isChecked()){
                    EnviarPuntos enviar = new EnviarPuntos();
                    enviar.execute();
                }else{
                    Toast.makeText(getApplicationContext(),"Por favor acepta los terminos",Toast.LENGTH_SHORT).show();
                }
           }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }

    private class EnviarPuntos extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            Cargando.setVisibility(View.VISIBLE); //Mostramos la precarga
        }
        public void onPostExecute(Void unused) {
        }

        @Override
        protected Void doInBackground(Void... params) {
            StringRequest request = new StringRequest(Request.Method.POST, getString(R.string.actualizar_datos), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.matches("edit-save")){
                        Toast.makeText(Inicio_App.this,"Se ha guardado con exito",Toast.LENGTH_SHORT).show();
                        for (int i=0;i<puntos.length;i++){
                            puntos[i].setEnabled(false);
                        }
                        //enviar.setVisibility(View.INVISIBLE);
                        estado.setText("Validado");
                        estado.setTextColor(Color.parseColor("#66BB6A"));
                        Cargando.setVisibility(View.INVISIBLE);
                    }else{
                        Cargando.setVisibility(View.INVISIBLE);
                        Toast.makeText(Inicio_App.this,"Tuvimos un problema al enviar los datos, intenta de nuevo",Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Cargando.setVisibility(View.INVISIBLE);
                    Toast.makeText(Inicio_App.this,"Verifique que este conectado a internet",Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("clave", clave);
                    parameters.put("pan", puntos[0].getText().toString());
                    parameters.put("pri", puntos[1].getText().toString());
                    parameters.put("prd", puntos[2].getText().toString());
                    parameters.put("pt", puntos[3].getText().toString());
                    parameters.put("verde", puntos[4].getText().toString());
                    parameters.put("mc", puntos[5].getText().toString());
                    parameters.put("alianza", puntos[6].getText().toString());
                    parameters.put("morena", puntos[7].getText().toString());
                    parameters.put("tabla", tabla);


                    return parameters;
                }
            };
            requestQueue.add(request);
            return null;
        }
    }
}
