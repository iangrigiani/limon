package com.android.nacho.probando;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private Randomize random;

    // Cantidad de bloques que hay que tocar para pasar el nivel
    private static final int cantidadBloquesATocar = 7;
    // Contador de bloques tocados en el nivel
    private int cantidadBloquesTocados = 0;
    // Cantidad de veces que se debe tocar el bloque
    private int cantidadToquesPorBloque = 0;
    // Contador de veces que se toco el bloque
    private int toquesAcumulados = 0;
    // Identificador del bloque que debe tocarse
    private int numBloque;
    // Cantidad maxima de toques para un bloque
    private int toquesMax = 4;
    // Cantidad de bloques del nivel
    private int cantidadBloquesNivel = 4;
    // Tiempo inicial del juego
    private long tiempoInicial=0;
    // Tiempo final del juego
    private long tiempoFinal=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        random = new Randomize();
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


    public void clickBoton1(View arg0) {
        tocoBloque(1);
    }

    public void clickBoton2(View arg0) {
        tocoBloque(2);
    }


    public void clickBoton3(View arg0) {
        tocoBloque(3);
    }


    public void clickBoton4(View arg0) {
        tocoBloque(4);
    }


    private void ganaste() {
        tiempoFinal = System.nanoTime() - tiempoInicial;
        TextView texto = (TextView) findViewById(R.id.lToques);
        texto.setText("GANASTE!!");

        TextView texto2 = (TextView) findViewById(R.id.lBloque);
        Double tiempo = tiempoFinal / 1000000000.0;
        texto2.setText("Tiempo estimado: " + String.valueOf(tiempo).substring(0, 5) + " seg");
    }

    private void perdiste() {
        TextView texto = (TextView) findViewById(R.id.lToques);
        texto.setText("PERDISTE!!");

        TextView texto2 = (TextView) findViewById(R.id.lBloque);
        texto2.setText("");
    }

    private void tocoBloque(int bloqueTocado) {
        if (numBloque == bloqueTocado) {
            toquesAcumulados++;
            if (toquesAcumulados == cantidadToquesPorBloque) {
                cantidadBloquesTocados++;

                if (cantidadBloquesTocados == cantidadBloquesATocar) {
                    ganaste();
                    iniciar();
                } else {
                    accion();
                    toquesAcumulados = 0;
                }
            }
        } else {
            perdiste();
            iniciar();
        }
    }

    public void setCondiciones(View arg0) {
        iniciar();
        accion();
    }


    private void iniciar() {
        toquesAcumulados = 0;
        tiempoInicial = System.nanoTime();
    }
    private void accion() {


        cantidadToquesPorBloque = random.getRandomNumber(toquesMax);
        numBloque = random.getRandomNumber(cantidadBloquesNivel);
        TextView texto = (TextView) findViewById(R.id.lToques);
        texto.setText("Cantidad de toques: " + String.valueOf(cantidadToquesPorBloque));

        TextView texto2 = (TextView) findViewById(R.id.lBloque);
        texto2.setText("Numero de bloque: " + String.valueOf(numBloque));
    }
}
