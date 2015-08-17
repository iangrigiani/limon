package com.android.nacho.probando.utils;

import com.android.nacho.probando.Bloque;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nacho on 16/08/15.
 */
public class XmlParser {

    List<Bloque> bloques;
    private Bloque bloque;
    private String text;
    private int cantidadBloquesATocar;
    private int toquesMax;
    private int columnasGrilla;
    private int filasGrilla;

    public XmlParser() {
        bloques = new ArrayList<Bloque>();
    }

    public List<Bloque> getBloques() {
        return bloques;
    }

    public int getCantidadBloquesATocar() {
        return cantidadBloquesATocar;
    }

    public int getToquesMax() {
        return toquesMax;
    }

    public int getColumnasGrilla() {
        return columnasGrilla;
    }

    public int getFilasGrilla() {
        return filasGrilla;
    }

    public List<Bloque> parsear(InputStream is) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:

                        if (tagname.equalsIgnoreCase("bloque")) {
                            bloque = new Bloque();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("cantidadBloquesATocar")) {
                            cantidadBloquesATocar = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("toquesMax")) {
                            toquesMax = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("columnasGrilla")) {
                            columnasGrilla = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("filasGrilla")) {
                            filasGrilla = Integer.parseInt(text);
                        } else if (tagname.equalsIgnoreCase("bloque")) {
                            bloques.add(bloque);
                        } else if (tagname.equalsIgnoreCase("id")) {
                            bloque.setId(Integer.parseInt(text));
                        } else if (tagname.equalsIgnoreCase("posicionX")) {
                            bloque.setPosicionX(Integer.parseInt(text));
                    } else if (tagname.equalsIgnoreCase("posicionY")) {
                            bloque.setPosicionY(Integer.parseInt(text));
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bloques;

    }
}
