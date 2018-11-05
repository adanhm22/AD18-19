/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;



import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    
    //<editor-fold defaultstate="collapsed" desc="espacio libre">
    /**
     * muestra el espacio libre del volumen
     * @param fichero
     * @return
     */
    public String espacioLibreFormateado (File fichero){
        if(fichero==null)
            throw new IllegalArgumentException("fichero no puede ser vacío");
        String espacio = null;
        float espacioLibre = fichero.getFreeSpace();
        if(espacioLibre%(1024F*1024F*1024F)>=1)
            espacio=new DecimalFormat("#.##").format(espacioLibre/1024F/1024F/1024F)+" Gigas ";
        else if(espacioLibre%(1024F*1024F)>=1)
            espacio=new DecimalFormat("#.##").format(espacioLibre/1024F/1024F)+" Megas ";
        else if(espacioLibre%(1024F)>=1)
            espacio=new DecimalFormat("#.##").format(espacioLibre/1024F)+" KiloBytes ";
        else
            espacio=new DecimalFormat("#.##").format(espacioLibre)+" Bytes";
        return espacio;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="borrado de archivos">
    
    /**
     * borra directorios vacios
     * @param padre
     * @return
     */
    public int borrarDirectoriosVacios(String padre){
        File raiz = new File(padre);
        this.contador=0;
        return this.borrarDirectoriosVacios(raiz);
    }
    
    /**
     * borra directorios vacios
     * @param padre
     * @return
     */
    private int borrarDirectoriosVacios(File padre) {
        for (File listFile : padre.listFiles()) {
            if (listFile.isDirectory()&&!Files.isSymbolicLink(listFile.toPath())) {
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
    
    /**
     * borra directorios o archivos de manera segura (o lo intenta)
     * @param directorio
     * @return
     * @throws IOException
     */
    public int borradoSeguro (File directorio) throws IOException{
        int numBorrados=0;
        this.borradoSeguroRecursivo(directorio, numBorrados);
        return numBorrados;
    }
    
    /**
     * borra directorios o archivos de manera segura (o lo intenta)
     * @param directorio
     * @param numBorrados
     * @return
     * @throws IOException
     */
    private int borradoSeguroRecursivo(File directorio,Integer numBorrados) throws IOException{
        if (!directorio.isDirectory()){
            if(directorio!=null)
                if(Utiles.borradoSeguro(directorio))
                    numBorrados++;
        }else if(directorio.list().length>1){
            for (File listFile : directorio.listFiles()) {
                if(directorio!=null)
                    return this.borradoSeguroRecursivo(listFile, numBorrados);
            }
        }
        
        return numBorrados;
    }
    
    /**
     * borra los archivos
     * @param selectedValuesList
     * @return
     */
    public int borrarArchivos(List<File> selectedValuesList) {
        int numeroBorrados = 0;
        for (File file : selectedValuesList) {
            if(file.delete())
                numeroBorrados++;
        }
        return numeroBorrados;
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="listado de archivos">
    /**
     * lista los archivos de unos determinados gigas
     * @param raiz
     * @param tamanio
     * @return
     */
    public List<File> listarDirectoriosTamanio(File raiz, int tamanio) {
        if(raiz==null)
            throw new IllegalArgumentException("raiz no puede ser nulo");
        List<File> archivos = new ArrayList<>();
        return this.listarDirectoriosTamanio(raiz, tamanio, archivos);
    }
    
    
    
    /**
     * lista los archivos de unos determinados gigas
     * @param raiz
     * @param tamanio
     * @param archivos
     * @return
     */
    private List<File> listarDirectoriosTamanio(File raiz, float tamanio, List archivos) {
        for (File list : raiz.listFiles()) {
            if (list != null) {
                if (list.isDirectory()&&!Files.isSymbolicLink(list.toPath())) {
                    File[] lista = list.listFiles();
                    if (lista != null) {
                        if (lista.length > 0) {
                            this.listarDirectoriosTamanio(list, tamanio, archivos);
                        }
                    }
                } else {
                    if (list.length() >= (tamanio * 1024F * 1024F*1024F)) {
                        archivos.add(list);
                    }
                }
            }
            
        }
        
        return archivos;
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="comprobaciones archivos">
    /**
     * lista los archivos similares en una carpeta recursivamente
     * @param padre
     * @param listaBorrar
     * @param lista
     * @throws IOException
     */
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
    
    /**
     * comprueba si la clave de un mapa es igual a su valor, siendo estos dos archivos
     * @param mapa
     * @return
     * @throws IOException
     */
    public Map<File,File> comprobarIgualesMap(Map<File,File> mapa) throws IOException{
        Map<File,File> mapaComprobacion=new HashMap<>();
        for (File file : mapa.keySet()) {
            if(Utiles.compararArchivosIntensivo(file, mapa.get(file)))
                mapaComprobacion.put(file, mapa.get(file));
        }
        return mapaComprobacion;
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="listar unidades segun sistema operativo">
    /**
     * lista las unidades de tu pc
     * @return
     */
    public File[] listarUnidadesByMario(){
        File[] ficheros = null;
        List<File> ficherosOtrosSistemas;
        //nos dice el sisterma op
        String so = System.getProperty("os.name");
        switch (so.toLowerCase()) {
            //depende el so se hace de una forma distinta
            case "windows":
                ficheros = File.listRoots();
                break;
            case "linux":
                ficherosOtrosSistemas=new ArrayList<>();
                ficheros = this.listarLinux(ficherosOtrosSistemas);
                break;
                
            case "unix":
                //no lo he probado
                ficherosOtrosSistemas=new ArrayList<>();
                ficheros = this.listarLinux(ficherosOtrosSistemas);
                break;
            case "macOs":
                //no lo he probado
                ficherosOtrosSistemas=new ArrayList<>();
                ficheros = this.listarMacOs(ficherosOtrosSistemas);
                break;
                
            default:
                throw new UnsupportedOperationException("No soportado aún");
        }
        
        
        return ficheros;
        
    }
    
    /**
     * lista los ficheros de linux
     * @param listaFicheros
     * @return
     */
    private File[] listarLinux(List<File> listaFicheros){
        listaFicheros.add(File.listRoots()[0]);
        //ficheros en mnt
        File mnt = new File("/mnt");
        if(mnt.exists()){
            File[] montados = mnt.listFiles();
            //comprobamos si hay ficheros en mnt
            if(montados.length!=0)
                //los añadimos
                listaFicheros.addAll(Arrays.asList(montados));
        }
        //ficheros en media
        File media = new File("/media");
        //comprobamos si existe media
        if(media.exists()){
            File usuarios = new File ("/home");
            //listamos las carpetas de /home
            String[] carpetasUsusarios = usuarios.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return new File (dir.getAbsolutePath()+File.pathSeparator+name).isDirectory();
                }
            });
            //comprobamos por cada usuario si hay carpeta en media
            for (File listaMedia : media.listFiles()) {
                boolean estaUsuario=false;
                for (String nombreUsuario : usuarios.list())
                    if(listaMedia.getName().equals(nombreUsuario)&&listaMedia.list().length!=0){
                        //si la hay añadimos todo lo que hay dentro en la lista
                        listaFicheros.addAll(Arrays.asList(listaMedia.listFiles()));
                        estaUsuario=true;
                    }
                //si la carpeta actual no es de un usuario lo añadimos
                if(!estaUsuario&&listaMedia.isDirectory()&&listaMedia.list().length!=0)
                    listaFicheros.add(listaMedia);
            }
            
        }
        
        return  listaFicheros.toArray(new File[listaFicheros.size()]);
    }
    
    /**
     * lista ficheros de mac
     * @param listaFicheros
     * @return
     */
    private File[] listarMacOs (List<File> listaFicheros){
        //añadimos raiz
        listaFicheros.add(File.listRoots()[0]);
        //listamos las carpetas de /Volumes que supuestamente están todos los dispositivos conectados
        File drives = new File("/Volumes");
        if(drives.exists()){
            for (File listFile : drives.listFiles()) {
                if(listFile.list().length>0)
                    listaFicheros.add(listFile);
            }
        }
        return listaFicheros.toArray(new File [listaFicheros.size()]);
    }
//</editor-fold>
    
}
