/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

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

    public int borrarDirectoriosVacios(String padre){
        File raiz = new File(padre);
        this.contador=0;
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

    public List<File> listarDirectoriosTamanio(File raiz, int tamanio) {

        List<File> archivos = new ArrayList<>();
        return this.listarDirectoriosTamanio(raiz, tamanio, archivos);
    }

    public int borrarArchivos(List<File> selectedValuesList) {
    int numeroBorrados = 0;
        for (File file : selectedValuesList) {
            if(file.delete())
                numeroBorrados++;
        }
        return numeroBorrados;
    }
    
    
    private List<File> listarDirectoriosTamanio(File raiz, int tamanio, List archivos) {

        for (File list : raiz.listFiles()) {

            if (list != null) {
                if (list.isDirectory()) {
                    File[] lista = list.listFiles();
                    if (lista != null) {
                        if (lista.length > 0) {
                            this.listarDirectoriosTamanio(list, tamanio, archivos);
                        }
                    }
                } else {
                    if (list.length() >= (tamanio * 1024F * 1024F * 1024F)) {
                        archivos.add(list);
                    }
                }
            }

        }

        return archivos;
    }
}
