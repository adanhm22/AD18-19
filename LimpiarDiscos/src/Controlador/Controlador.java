/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.io.File;
import java.util.List;

/**
 *
 * @author Adán
 */
public class Controlador {
    private int contador;
    public long espacioLibre (int unidad){
        
        File raiz = new File(File.listRoots()[unidad].getAbsolutePath());
        return raiz.getFreeSpace()/1024/1024/1024;
    }

    public int borrarDirectoriosVacios(int unidad) {
       File raiz = new File(File.listRoots()[unidad].getAbsolutePath());
       this.contador=0;
       return this.borrado(raiz);
    }
    
    private int borrado (File padre){
        for (File listFile : padre.listFiles()) {
            if(listFile.isDirectory()){
                File[] lista = listFile.listFiles();
                if(lista.length==0){
                    listFile.delete();
                    contador++;
                }else{
                    this.borrado(listFile);
                }
            }
        }
        return this.contador;
    }

    public void borrarArchivos(List<String> selectedValuesList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
