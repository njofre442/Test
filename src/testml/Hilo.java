/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testml;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;

/**
 *
 * @author JOFRE
 */
public class Hilo extends Thread {
    
    private int idHilo;
    private long numeroActual;
    
    public Hilo(int idHilo){
        this.idHilo = idHilo;
        numeroActual = TestML.obtenerNumSiguienteSegunTabla();
    }

    private boolean esPrimoOptimizado(long numero){
        boolean resultado = true;
        if ((numero == 2) || (numero == 3) || (numero == 5)){ // Casos de excepcion (2, 3 y 5)
            resultado = true;
        }else if ((numero%2 == 0) || (numero%3 == 0) || (numero%5 == 0)){
            resultado = false;
        }else{
            double raiz = Math.sqrt(numero);
            long raizEntera = (long)raiz;
            if ((double)raizEntera == raiz){
                resultado = false;
            }else{
                long i=7;
                while (i < raizEntera){
                    if (numero%i == 0){
                        resultado = false;
                        break;
                    }
                    boolean encontrePrimo = false;
                    while (!encontrePrimo){
                        int cat = (int)(i % 30);
                        switch (cat){
                            case 1:
                            case 23:{ i += 6; break; }
                            case 7:
                            case 13:
                            case 19:{ i += 4; break; }
                            case 11:
                            case 17:
                            case 29:{ i += 2; break; }
                        }
                        encontrePrimo = esPrimoOptimizado(i);
                    }
                }
                if (i>=raizEntera){
                    resultado = true;
                }
            }
        }
        return resultado;
    }
    
    private String[] obtenerRotacionesDeDigitos(String numero){
        String[] listaRotaciones = new String[numero.length()];
        for (int i=0 ; i<numero.length() ; i++){
            listaRotaciones[i] = numero.substring(i,numero.length()) + numero.substring(0,i);
        }
        return listaRotaciones;
    }
    
    private void verificoPrimoCircular(){
        boolean resEsPrimoCircular = true;
        String[] rotaciones = obtenerRotacionesDeDigitos(""+numeroActual);
        for (int i=0 ; i<rotaciones.length ; i++){
            if (!esPrimoOptimizado(Integer.parseInt(rotaciones[i]))){
                resEsPrimoCircular = false;
                break;
            }
        }
        if (resEsPrimoCircular){
            TestML.guardarNumeroPrimoCircular(numeroActual);
        }
    }

    public void run(){
        System.out.println("HILO "+idHilo+". Inicia");
        
        while (numeroActual < 1000000){
            verificoPrimoCircular();
            numeroActual = TestML.obtenerNumSiguienteSegunTabla();
        }
        
        System.out.println("HILO "+idHilo+". Finaliza");
        
        TestML.finalizarThread(idHilo);
    }
}
