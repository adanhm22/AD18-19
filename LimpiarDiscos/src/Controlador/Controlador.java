/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Interfaces.PantallaPrincipal;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.Utiles;

/**
 *
 * @author Adán
 */
public class Controlador {

    private int contador;

    public float espacioLibre(int unidad) {

        File raiz = new File(File.listRoots()[unidad].getAbsolutePath());
        return raiz.getFreeSpace() / 1024f / 1024f / 1024f;
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
        if(raiz==null)
            throw new IllegalArgumentException("raiz no puede ser nulo");
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
    
    public void comprobarIgualesCarpeta(File padre,Map<File,File> listaBorrar,List<File>... lista) throws IOException{
        if(padre==null|lista==null|listaBorrar==null)
            throw new IllegalArgumentException("Los parametros de entrada no pueden ser nulos");
        if(lista.length>1)
            throw new IllegalArgumentException("no puedes tener mas de 1 lista");
        List<File> lista2;
        if(lista.length==0){
            lista2 = new ArrayList<>();
        }else{
            lista2=lista[0];
        }
            
        
        File[] hijos=padre.listFiles();
        if(hijos!=null)
        for (File hijo : hijos) {
            if(hijo.isDirectory()){
                if(!Files.isSymbolicLink(hijo.toPath()))
                this.comprobarIgualesCarpeta(hijo,listaBorrar,lista2);
            } else{
                if(!lista2.isEmpty()){
                    boolean igual=false;
                    for (File file : lista2) {
                        if(Utiles.compararArchivos(hijo, file)){
                            listaBorrar.put(hijo,file);
                            igual=true;
                        }
                    }
                     if(!igual){
                            lista2.add(hijo);
                     }
                }else{
                    lista2.add(hijo);
                }
                    
            }
        }
    }
    
    
    public Map<File,File> comprobarIgualesMap(Map<File,File> mapa) throws IOException{
        Map<File,File> mapaComprobacion=new HashMap<>();
        for (File file : mapa.keySet()) {
            if(Utiles.compararArchivosIntensivo(file, mapa.get(file)))
                mapaComprobacion.put(file, mapa.get(file));
        }
        return mapaComprobacion;
    }
    
    public File[] LocalizacionesImportantes(){
        String separador = System.lineSeparator();
        File[] ficheros = null;
       // if(separador.equals("\r\n")){
            ficheros = File.listRoots();
       // }else{
            
      //  }
        return ficheros;
    }
}
