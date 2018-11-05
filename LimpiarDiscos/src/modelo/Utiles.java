/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 *
 * @author alumnop
 */
public class Utiles {

    /**
     * compara ligeramente dos archivos, intensiva si son archivos pequeños
     *
     * @param fich1
     * @param fich2
     * @return true si son iguales
     * @throws IOException
     */
    public static boolean compararArchivos(File fich1, File fich2) throws IOException {
        try{
        String extension1 = null, extension2 = null;
        if(fich1.getName().contains(".")&&fich2.getName().contains(".")){
        extension1 = fich1.getName().substring(fich1.getName().lastIndexOf("."));
        extension2 = fich2.getName().substring(fich2.getName().lastIndexOf("."));
        }else if(!fich1.getName().contains(".")&&!fich2.getName().contains(".")){
            extension1="";
            extension2="";
        }else
            return false;

        if (fich1.length() == fich2.length()) {
            if (extension1.equals(extension2)) {
                byte[] bytes1 = Files.readAllBytes(fich1.toPath());
                byte[] bytes2 = Files.readAllBytes(fich2.toPath());
                if (bytes1.length >= 30) {
                    if (bytes1[0] == bytes2[0]) {
                        if (bytes1[bytes1.length - 1] == bytes2[bytes2.length - 1]) {
                            int mitad = bytes1.length / 2;
                            if (bytes1[mitad] == bytes2[mitad]) {
                                if (((bytes1[1] + bytes1[2]) - bytes1[3]) == (((bytes2[1] + bytes2[2]) - bytes2[3]))) {
                                    if (bytes1[20] + bytes1[22] == bytes2[20] + bytes2[22]) {
                                        if((bytes1[bytes1.length-22]+bytes1[mitad+5])==(bytes1[bytes2.length-22]+bytes2[mitad+5]))
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < bytes1.length; i++) {
                        if (bytes1[i] != bytes2[i]) {
                            return false;
                        }
                    }
                    return true;
                }

            }
        }
        }catch(java.nio.file.FileSystemException ex){
        }
        return false;
    }
    
    
    /**
     * compara intensivamente dos archivos.
     * @param fich1
     * @param fich2
     * @return true si son iguales.
     * @throws IOException 
     */
    public static boolean compararArchivosIntensivo(File fich1, File fich2) throws IOException {
        if (compararArchivos(fich1, fich2)) {
            byte[] bytes1 = Files.readAllBytes(fich1.toPath());
            byte[] bytes2 = Files.readAllBytes(fich2.toPath());

            if (bytes1.length <= 30) {
                return true;
            }

            for (int i = 0; i < bytes1.length; i++) {
                if (bytes1[i] != bytes2[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    /**
     * cambia un cuarto del archivo para que sea ilegible y despues lo borra
     * @param fichero
     * @return true si ha sido posible borrarlo, false si no
     * @throws IOException 
     */
    public static boolean borradoSeguro(File fichero) throws IOException{
        FileOutputStream fos = new FileOutputStream(fichero);
        byte[] bytee = {1};
        byte[] ficheroBytes = Files.readAllBytes(fichero.toPath());
        if(ficheroBytes.length>0){
        for (int i = 0; i < ficheroBytes.length/4; i++) {
                fos.write(bytee, i, 1);
        }
        return fichero.delete();
        }
        return false;
    }

}
