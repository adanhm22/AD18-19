/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Interfaces.BarraProgreso;
import Interfaces.PantallaPrincipal;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adán
 */
public class Controlador {

    private int contador;

    public long espacioLibre(int unidad) {

        File raiz = new File(File.listRoots()[unidad].getAbsolutePath());
        return raiz.getFreeSpace() / 1024 / 1024 / 1024;
    }

    public int borrarDirectoriosVacios(String unidad) {
        File raiz = new File(unidad);
        this.contador = 0;
        return this.borrarDirectoriosVacios(raiz);
    }

    private int borrarDirectoriosVacios(File padre) {
        for (File listFile : padre.listFiles()) {
            if (listFile.isDirectory()) {
                File[] lista = listFile.listFiles();
                if (lista.length == 0) {
                    if (listFile.delete()) {
                        contador++;
                    }
                } else {
                    this.borrarDirectoriosVacios(listFile);
                }
            }
        }
        return this.contador;
    }

    public List<File> borrarDirectoriosTamanio(String unidad, int tamanio) {
        File raiz = new File(unidad);
        List<File> archivos = new ArrayList<>();
        return this.borrarDirectoriosTamanio(raiz, tamanio, archivos);
    }

    /* public void borrarArchivos(List<String> selectedValuesList) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
    private List<File> borrarDirectoriosTamanio(File raiz, int tamanio, List archivos) {
        /* BarraProgreso bp = new BarraProgreso(null, true, raiz.listFiles().length);
        Thread hilo = new Thread(bp);
        hilo.start();*/
        for (File list : raiz.listFiles()) {

            if (list != null) {
                if (list.isDirectory()) {
                    File[] lista = list.listFiles();
                    if (lista != null) {
                        if (lista.length > 0) {
                            this.borrarDirectoriosTamanio(list, tamanio, archivos);
                        }
                    }
                } else {
                    if (list.length() >= (tamanio * 1024 * 1024 * 1024)) {
                        archivos.add(list);
                    }
                }
            }
               // hilo.notify();
            
            
        }

        return archivos;
    }
}
