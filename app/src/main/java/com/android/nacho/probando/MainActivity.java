package com.android.nacho.probando;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.nacho.probando.utils.Randomize;
import com.android.nacho.probando.utils.XmlParser;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends ActionBarActivity {

    private Randomize random;

    // Cantidad de bloques que hay que tocar para pasar el nivel
    private int cantidadBloquesATocar;
    // Contador de bloques tocados en el nivel
    private int cantidadBloquesTocados = 0;
    // Cantidad de veces que se debe tocar el bloque
    private int cantidadToquesPorBloque = 0;
    // Contador de veces que se toco el bloque
    private int toquesAcumulados = 0;
    // Identificador del bloque que debe tocarse
    private int numBloque;
    // Cantidad maxima de toques para un bloque
    private int toquesMax;
    // Cantidad de bloques del nivel
    private int cantidadBloquesNivel;
    // Tiempo inicial del juego
    private long tiempoInicial=0;
    // Tiempo final del juego
    private long tiempoFinal=0;

    // grilla donde van los bloques
    private int columnasGrilla;
    private int filasGrilla;

    // Listado de bloques predefinidos del nivel
    Button botones[];

    String nivel;

    XmlParser parser;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        setContentView(R.layout.activity_main);
        random = new Randomize();

        Bundle b = getIntent().getExtras();
        nivel = b.getString("nivel");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void ganaste() {

        tiempoFinal = System.nanoTime() - tiempoInicial;
        TextView texto = (TextView) findViewById(R.id.textoFooter);

        Double tiempo = tiempoFinal / 1000000000.0;
        texto.setText("GANASTE!! Tiempo estimado: " + String.valueOf(tiempo).substring(0, 5) + " seg");

        finalizar();
    }

    private void perdiste() {
        TextView texto = (TextView) findViewById(R.id.textoFooter);
        texto.setText("PERDISTE!!");
        finalizar();
    }

    private void finalizar() {
        botones[numBloque-1].setText("");
        for (int a = 0; a < cantidadBloquesNivel; a++) {
            botones[a].setEnabled(false);
        }
    }
    private void tocoBloque(int bloqueTocado) {
        if (numBloque == bloqueTocado) {
            toquesAcumulados++;
            if (toquesAcumulados == 1) {
                botones[numBloque-1].setText("");
            }
            if (toquesAcumulados == cantidadToquesPorBloque) {
                cantidadBloquesTocados++;

                if (cantidadBloquesTocados == cantidadBloquesATocar) {
                    ganaste();
                } else {
                    accion();
                    toquesAcumulados = 0;
                }
            }
        } else {
            perdiste();
        }
    }

    private void cargarNivel() {
        AssetManager assetManager = context.getAssets();
        InputStream nivelXml = null;
        try {
            nivelXml = assetManager.open(nivel);
        } catch (IOException e) {
            TextView texto = (TextView) findViewById(R.id.textoFooter);
            texto.setText("Error al cargar el nivel");

            e.printStackTrace();
        }

        parser = new XmlParser();
        ArrayList<Bloque> bloques = (ArrayList<Bloque>)parser.parsear(nivelXml);
        cantidadBloquesATocar = parser.getCantidadBloquesATocar();
        toquesMax = parser.getToquesMax();
        columnasGrilla = parser.getColumnasGrilla();
        filasGrilla = parser.getFilasGrilla();

        cantidadBloquesNivel = bloques.size();
        botones = new Button[cantidadBloquesNivel];

        Iterator<Bloque> itBloque = bloques.iterator();
        int i = 0;

        Bloque bloque;
        GridLayout fondo = (GridLayout) findViewById(R.id.fondoBotones);

        fondo.setColumnCount(columnasGrilla);
        fondo.setRowCount(filasGrilla);

        int anchoBoton = fondo.getWidth() / columnasGrilla;
        int altoBoton = fondo.getHeight() / filasGrilla;

        while (itBloque.hasNext()) {
            bloque = itBloque.next();

            int id = i+1;
            botones[i] = new Button(getApplicationContext());
            botones[i].setId(id);
            botones[i].setTextColor(Color.parseColor("#B0171F"));
            botones[i].setTextSize(20);
            botones[i].setWidth(anchoBoton);
            botones[i].setHeight(altoBoton);

            //botones[i].setLayoutParams(param);
            //botones[i].setMinimumWidth(100);
            //botones[i].setMinWidth(100);

            //botones[i].setText(String.valueOf(i));

            botones[i].setOnClickListener(handleOnClick(botones[i]));
            fondo.addView(botones[i]);

            i++;
        }

    }

    public void setCondiciones(View arg0) {
        cargarNivel();
        iniciar();
        accion();
    }


    private void iniciar() {
        toquesAcumulados = 0;
        cantidadBloquesTocados = 0;
        tiempoInicial = System.nanoTime();

        for (int a = 0; a < cantidadBloquesNivel; a++) {
            botones[a].setEnabled(true);
            botones[a].setText("");
        }

        TextView texto = (TextView) findViewById(R.id.textoFooter);
        texto.setText("");
    }

    private void accion() {
        cantidadToquesPorBloque = random.getRandomNumber(toquesMax);
        numBloque = random.getRandomNumber(cantidadBloquesNivel);


        botones[numBloque-1].setText(String.valueOf(cantidadToquesPorBloque));

    }


    View.OnClickListener handleOnClick(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                tocoBloque(button.getId());
            }
        };
    }

}
