/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testml;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestML {
    
    private static int numeroTotalNucleos;
    private static long numSiguente;
    private static final List listaPrimosCirculares = new ArrayList();
    public static final List listaThreadFinalizados = new ArrayList();
    private static boolean casoExcepcion = true;
    
    private static boolean esNumeroPosible(String numero){ // Posibles seran aquellos numeros que no incluyan digitos pares o el 5
        boolean esPosible = true;
        for (int i=0 ; i<numero.length() ; i++){
            char c = numero.charAt(i);
            if ((c!='1') && (c!='3') && (c!='7') && (c!='9')){
                esPosible = false;
                break;
            }
        }
        return esPosible;
    }
    
    public synchronized static long obtenerNumSiguienteSegunTabla(){
        long res = numSiguente;
        // Calculo numero primo siguiente
        if (!casoExcepcion){
            boolean esPosible = false;
            while (!esPosible){
                int cat = (int)(numSiguente % 30);
                switch (cat){
                    case 1:
                    case 23:{ numSiguente += 6; break; }
                    case 7:
                    case 13:
                    case 19:{ numSiguente += 4; break; }
                    case 11:
                    case 17:
                    case 29:{ numSiguente += 2; break; }
                }
                esPosible = esNumeroPosible(""+numSiguente);
            }
        }else{
            switch ((int)numSiguente){
                case 2:{ numSiguente=3; break; }
                case 3:{ numSiguente=5; break; }
                case 5:{ numSiguente=7; casoExcepcion=false; break; }
            }
        }
        return res;
    }
    
    public synchronized static void guardarNumeroPrimoCircular(long numero){
        listaPrimosCirculares.add(numero);
    }
    
    public static void finalizarThread(int idThread){
        synchronized(listaThreadFinalizados){
            listaThreadFinalizados.add(idThread);
            if (listaThreadFinalizados.size() == numeroTotalNucleos){
                listaThreadFinalizados.notify();
            }
        }
    }

    public static void main(String[] args){

        System.out.println("TEST ML");
        System.out.println("El programa creara tantos hilos de ejecucion como nuclos posea el procesador");
        
        // Inicializo variables
        numSiguente = 2; // Primer numero primo
        numeroTotalNucleos = Runtime.getRuntime().availableProcessors();
        Hilo[] hilos = new Hilo[numeroTotalNucleos];
        long tiempoInicial, tiempoFinal;
        
        // Capturo tiempo inicial
        tiempoInicial = System.currentTimeMillis();

        // Creo  y ejecuto los thread 
        for (int idNucleo=0 ; idNucleo<numeroTotalNucleos ; idNucleo++){
            hilos[idNucleo] = new Hilo(idNucleo);
            hilos[idNucleo].start();
        }
        
        // Espero que todos los thread finalicen
        synchronized(listaThreadFinalizados){
            try {
                listaThreadFinalizados.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(TestML.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // Capturo tiempo final
        tiempoFinal = System.currentTimeMillis();
        
        // Muestro Resultados
        System.out.println("________ RESULTADOS ________");
        System.out.println("TIEMPO: "+ (tiempoFinal - tiempoInicial) +" milisegundos");
        System.out.println("CANTIDAD: "+listaPrimosCirculares.size()+" números");
        System.out.print("PRIMOS CIRCULARES: ");
        for (int i=0 ; i<listaPrimosCirculares.size() ; i++){
            System.out.print(listaPrimosCirculares.get(i)+",");
        }
        System.out.println("");

    }
}
