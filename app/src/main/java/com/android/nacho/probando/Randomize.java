package com.android.nacho.probando;

import java.util.Collection;
import java.util.Random;

/**
 * Created by nacho on 24/05/15.
 */
public class Randomize {
    Random r = new Random();




    public Collection<Integer> getRandomList() {
        return null;
    }

    public String getDummyString() {
        return "Es un string de prueba";
    }

    public int getRandomNumber(int max) {
        return r.nextInt(max)+1;
    }
}
