package kennedy.com.Pigeonper;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kennedy.com.Pigeonper.Entidades.HistorialPerformance;
import kennedy.com.Pigeonper.Entidades.Palomas;


public class Ag{

    private List<boolean[]> listaDeCromosomasSeleccionados=null;
    private List<boolean[]>listaDeCromosomasCruzado=null;
    private List<boolean[]>listaDeCromosomasMutados=null;
    private List<boolean[]>listaDeCromosomasDepurados=null;
    public ArrayList<Palomas>listaGanadora;
    public List<boolean[]> listaDeCromosomas=null;


    public boolean[] cromosomaGanador;
    public float fitnessGanador=0;

    double rangeMinRandom = 0.0f;
    double rangeMaxRandom = 1.0f;

    public static final int CANT_DE_INDIVIDUOS_A_SELECCIONAR_EN_CROMOSOMA = 10; //CANTIDAD DE TRUEs
    private int POBLACION_A_EVALUAR;
    private static final int CANT_DE_INDIVIDUOS_SELECCIONADOS_PARA_TORNEO = 2;//SIEMPRE PAR
    private static int CANT_DE_ITERACIONES = 500; //800 funciona ok, 500  tamnbien
    private static final double PROBA_DE_CRUCE = 0.98f;
    private ArrayList<Palomas> listaDePalomas;
    private ArrayList<HistorialPerformance> historialPerformance;
    public ProgressDialog progressDialog;

    ////////////////////////CONSTRUCTOR
    public Ag(){}

    public Ag(ArrayList<Palomas> listaDePalomas, ArrayList<HistorialPerformance> historialPerformance, double distancia, Context context) {

        //////////////////////////////////////TAMAÑO DE LA POBLACION AUTOMATICA

        int N = listaDePalomas.size();
        int e = 5; // error de estimacion maximo aceptado, en porcentaje
        float z = parametroEstadistico(e);
        int p = 50; //probabilidad que ocurra el evento, en porcentaje
        int q = 50; // probabilidad de que no ocurra el evento, en porcentaje

        float primerValor = N * (z * z) * ((float) p / 100) * ((float) q / 100);
        float segundoValor = ((float) e / 100 * (float) e / 100) * (N - 1) + (z * z) * (float) p / 100 * (float) q / 100;

        POBLACION_A_EVALUAR = (int) (primerValor / segundoValor);

        if (POBLACION_A_EVALUAR % 2 != 0) POBLACION_A_EVALUAR = POBLACION_A_EVALUAR - 1;
        ///////////////////////////////////////////////////////////////////////////////////////////////

        this.listaDePalomas = listaDePalomas;
        this.historialPerformance = historialPerformance;

        Random r = new Random();
        listaGanadora = new ArrayList<>();

        if (listaDePalomas.size() >= CANT_DE_INDIVIDUOS_A_SELECCIONAR_EN_CROMOSOMA) {

            List<boolean[]> listaDeCromosomas = generarPoblacionInicial(listaDePalomas);
            ValoresFitness vfInicial = evaluacionDelFitness(listaDeCromosomas, listaDePalomas, historialPerformance, distancia);//Guardo los valores que devolvio la funcion evaluacionDelFitnes en la clase vfInicial
            //System.out.println("POBLACION A EVALUAR: "+POBLACION_A_EVALUAR + "  ***** LISTA DE CROMO: "+listaDeCromosomas.size());

/*
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Ejecutando Algoritmo Genetico");
            progressDialog.setMessage("Aguarde a que termine de ejecutar el A.G.");
            progressDialog.show();
*/

            int iteraciones = 0;
            while (iteraciones < CANT_DE_ITERACIONES) {

                listaDeCromosomasSeleccionados = seleccion(vfInicial, listaDeCromosomas);
                ValoresFitness vfSeleccion = evaluacionDelFitness(listaDeCromosomasSeleccionados, listaDePalomas, historialPerformance, distancia);

    //////////////////////      CRUCE
                double createdRanNum = rangeMinRandom + (rangeMaxRandom - rangeMinRandom) * r.nextDouble();
                if (createdRanNum < PROBA_DE_CRUCE) {      //CRUZA
                    //System.out.println("CRUZA PERRITO MALVADO "+createdRanNum);
                    //System.out.println(listaDeCromosomasSeleccionados.size());
                    listaDeCromosomasCruzado = crossOver(listaDeCromosomasSeleccionados);
                    //System.out.println("CRUZO");
                } else {
                    //System.out.println("NO CRUZA PERRITO MALVADO "+createdRanNum);
                    listaDeCromosomasCruzado = listaDeCromosomasSeleccionados;
                    // System.out.println("NO CRUZO");
                }
                ValoresFitness vfCruza = evaluacionDelFitness(listaDeCromosomasCruzado, listaDePalomas, historialPerformance, distancia);

    //////////////////////      MUTACION
                listaDeCromosomasMutados = mutacion(listaDeCromosomasCruzado);
                ValoresFitness vfMutados = evaluacionDelFitness(listaDeCromosomasMutados, listaDePalomas, historialPerformance, distancia);

    ///////////////////////     DEPURACION
                listaDeCromosomasDepurados = validarRestriccionCantSeleccion(listaDeCromosomasMutados);
                ValoresFitness vfDepurados = evaluacionDelFitness(listaDeCromosomasDepurados, listaDePalomas, historialPerformance, distancia);

                compruebaFitnessOptimo(vfDepurados, fitnessGanador, cromosomaGanador);

                System.out.println("************************* Mejor fitness: " + fitnessGanador);
                //for(int i=0; i<cromosomaGanador.length; i++) System.out.println("Cromosoma ganador: "+cromosomaGanador[i]);

                //Se cargan para la nueva vuelta de busqueda
                listaDeCromosomas = listaDeCromosomasMutados;
                vfInicial = vfMutados;

                iteraciones++;
            }/////FIN WHILE PRINCIPAL

            decodificarCromosomaGanador(listaDePalomas, cromosomaGanador);
            //for(int i=0; i<listaGanadora.size() ; i++) System.out.println(listaGanadora.get(i).getNumero()+", "+listaGanadora.get(i).getRaza()+", "+listaGanadora.get(i).getEdad());
            //for(int i=0; i<listaGanadora.size() ; i++) listadofinal.add(i,listaGanadora.get(i));

        }else listaGanadora=listaDePalomas;// NOO REMPLAZAR POR LA LISTA ANTEIOR

        // progressDialog.dismiss();

    }// FIN DEL CONSTRUCTOR
///////////////////////////////////////////////////////////////////////////////////////////////////////////

    private float parametroEstadistico(int e) {

        int[] porError={20,10,9,8,7,6,5};
        float[] z = {(float) 1.28,(float) 1.65,(float) 1.69,(float) 1.75,(float) 1.81,(float) 1.88,(float) 1.96 };
        float valor=0;

        for(int i = 0 ; i < porError.length ; i++){
            if(e == porError[i]) {
                valor = z[i];
            }
        }
        return valor;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<Palomas> getListaGanadora(){
        return listaGanadora;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void decodificarCromosomaGanador(ArrayList<Palomas> listaDePalomas, boolean[]cromosomaGanador) {

        for(int i=0 ; i<cromosomaGanador.length ; i++){
            if (cromosomaGanador[i]==true){
                listaGanadora.add(listaDePalomas.get(i));
            }
        }

    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private List<boolean[]> validarRestriccionCantSeleccion(List<boolean[]> listaDeCromosomasMutados) {

        List<boolean[]> listaDepurada=listaDeCromosomasMutados;
        Random rand=new Random();

        for(int i=0 ; i< listaDepurada.size() ; i++){
            boolean[] cromosomaTemp;

            while(cantidadDePalomas(listaDepurada.get(i)) != CANT_DE_INDIVIDUOS_A_SELECCIONAR_EN_CROMOSOMA){

                int numRandom=rand.nextInt(listaDeCromosomasMutados.get(0).length);

                if(cantidadDePalomas(listaDepurada.get(i)) > CANT_DE_INDIVIDUOS_A_SELECCIONAR_EN_CROMOSOMA){
                    cromosomaTemp=listaDepurada.get(i);
                    if(cromosomaTemp[numRandom]==true) cromosomaTemp[numRandom]=false;
                    listaDepurada.set(i,cromosomaTemp);
                }

                if(cantidadDePalomas(listaDepurada.get(i)) < CANT_DE_INDIVIDUOS_A_SELECCIONAR_EN_CROMOSOMA){
                    cromosomaTemp=listaDepurada.get(i);
                    if(cromosomaTemp[numRandom]==false) cromosomaTemp[numRandom]=true;
                    listaDepurada.set(i,cromosomaTemp);
                }

            }

            //////////////////////////////////////////////////////////////
           /* System.out.println("poblacion normalizada ->");
            for(int j=0 ; j<listaDepurada.get(0).length ; j++){
                System.out.println("Depurados: "+listaDepurada.get(i)[j]);
            }*/

        }//CIERRE DEL FOR

        return listaDepurada;

    }


// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void compruebaFitnessOptimo(ValoresFitness vfMutados, float fitnessGanador, boolean[] cromosomaGanador){


        for(int i=0; i<vfMutados.getFitnessPorCromosoma().length;i++){

            if(vfMutados.getFitnessPorCromosoma()[i] > fitnessGanador){

                fitnessGanador=vfMutados.getFitnessPorCromosoma()[i];
                cromosomaGanador=listaDeCromosomasMutados.get(i);
            }

        }
        this.fitnessGanador=fitnessGanador;
        this.cromosomaGanador=cromosomaGanador;

    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private List<boolean[]> mutacion(List<boolean[]> listaDeCromosomasCruzado) {

        List<boolean[]>listaMutada=new ArrayList<>(listaDeCromosomasCruzado.size());
        Integer[]puntosDeMutacion= new Integer[listaDeCromosomasCruzado.size()];

        cargaVectorDePuntosDeCruce(puntosDeMutacion);

        listaMutada=listaDeCromosomasCruzado;

        for (int i=0; i<listaDeCromosomasCruzado.size();i++){
            //System.out.println("PUNTO A MUTAR: "+puntosDeMutacion[i]);
            for(int j=0; j<listaDeCromosomasCruzado.get(0).length;j++){
                if(j==puntosDeMutacion[i]){
                    if(listaMutada.get(i)[j]==true){
                        listaMutada.get(i)[j]=false;
                    }else listaMutada.get(i)[j]=true;
                }
            }

           // System.out.println("Impresion Lista MUTADA");
           // for(int j=0 ; j<listaMutada.get(0).length ;  j++) System.out.println(listaMutada.get(i)[j]);

        }//FIN FOR I


        return listaMutada;
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private List<boolean[]> crossOver(List<boolean[]> listaDeCromosomasSeleccionados) {

        List<boolean[]>listaDeCromosomasCruzado = new ArrayList<boolean[]>(listaDeCromosomasSeleccionados.size());
        //List<boolean[]>listaDeCromosomasCruzado = null;
        Integer[] parejaDeCruces=new Integer[listaDeCromosomasSeleccionados.size()];
        Integer[] puntoDeCruce=new Integer[listaDeCromosomasSeleccionados.size()];

        cargaVectorDeParejas(parejaDeCruces); //CARGA EL VECTOR DE pAREJAS DEL CRUCE
        cargaVectorDePuntosDeCruce(puntoDeCruce); // CARGA EL VECTOR DE PUNTOS DE CRUCE (SOLO UN PUNTO)

////////////COMIENZA EL CRUCE

        for(int i=0; i<listaDeCromosomasSeleccionados.size(); i++){

            int largoDeCromosoma=listaDeCromosomasSeleccionados.get(0).length;
            boolean[]temp=new boolean[largoDeCromosoma];

            for (int j=0; j<largoDeCromosoma;j++){

                if(j<puntoDeCruce[i]) {//VER ANTES ERA (<=)
                    temp[j] = (listaDeCromosomasSeleccionados.get(i))[j];
                }else{
                    temp[j] = listaDeCromosomasSeleccionados.get(parejaDeCruces[i])[j];
                }
            }//CIERRE FOR J

            listaDeCromosomasCruzado.add(i,temp);

            //System.out.println("\npunto de cruce: "+puntoDeCruce[i]);
            //for (int j=0; j<largoDeCromosoma;j++) System.out.println(" temp cuando es menor al punto de cruce: "+listaDeCromosomasCruzado.get(i)[j]);

        }//CIERRE FOR I
/////////////// FIN DEL CRUCE

        return listaDeCromosomasCruzado;
    }

///////////////////////////////////////////////////////////////////////////////////////////

    private void cargaVectorDePuntosDeCruce(Integer[] puntoDeCruce) {
        Random rand = new Random();
        int puntoDeCruceTEMP =rand.nextInt((listaDeCromosomasSeleccionados.get(0).length));//ANTES SE RESTABA 1 AL LARGO// RAND NO  INCLUYE EL extremo maximo

        for(int i=0; i<listaDeCromosomasSeleccionados.size() ;i++){
            puntoDeCruce[i]=puntoDeCruceTEMP;
            puntoDeCruceTEMP =rand.nextInt((listaDeCromosomasSeleccionados.get(0).length));//ANTES SE RESTABA 1 AL LARGO// RAND NO  INCLUYE EL extremo maximo
        }


    }

/////////////////////////////////////////////////////////////////////////////////////////////

    private void cargaVectorDeParejas(Integer[] parejaDeCruces) {
        int parejaTEMP=0;
        Random rand = new Random();

        for(int i=0 ; i<listaDeCromosomasSeleccionados.size(); i++){
            System.out.println(listaDeCromosomasSeleccionados.size());
            parejaTEMP=rand.nextInt(listaDeCromosomasSeleccionados.size());
            //System.out.println("I= "+i+" parejaTEMP= "+parejaTEMP);

            if(parejaDeCruces[i]==null){

                while(parejaTEMP==i || parejaDeCruces[parejaTEMP] != null){

                    parejaTEMP=rand.nextInt(listaDeCromosomasSeleccionados.size());
                    //System.out.println("Pareja posible     " + parejaTEMP);

                }

                parejaDeCruces[i]=parejaTEMP;
                parejaDeCruces[parejaTEMP]=i;


            }else continue;

            //System.out.println("Se genero el numero de pareja: "+parejaTEMP+" para i= "+i);

        } //CIERRE FOR

        //for(int i=0 ; i<listaDeCromosomasSeleccionados.size(); i++) System.out.println("LA PAREJA ES: "+ parejaDeCruces[i]);
    }

/////////////////////////////////////////////////////////////////////////////////

    private List<boolean[]> generarPoblacionInicial(List c) {

        List<boolean[]> poblacion = new ArrayList<>();//Array donde se agregan la poblacion valida
        Random rand = new Random();

        poblacion.clear();//limpio la lista

        while (poblacion.size()  < POBLACION_A_EVALUAR){

            boolean [] cromosoma = new boolean[(c.size())];
            inicializaCromosomaEnCeros(cromosoma);

            while (cantidadDePalomas(cromosoma) < CANT_DE_INDIVIDUOS_A_SELECCIONAR_EN_CROMOSOMA){

                int numRandom=rand.nextInt(c.size()-1);
                cromosoma[numRandom] = true;

            }

            poblacion.add(cromosoma);
            //IMPRESION DE POBLAICION
            for(int j=0 ; j<cromosoma.length;j++){
              //  System.out.print(cromosoma[j]+", ");
            }
           // System.out.println("");
            //FIN DE LA IMPRESINO
        }

        return poblacion;
    }

///////////////////////////////////////////////////////////////////////////////////////////////

    private void inicializaCromosomaEnCeros(boolean[] cromosoma) {

        for (int i=0; i<cromosoma.length;i++){
            cromosoma[i]=false;
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////

    private int cantidadDePalomas(boolean[] cromosoma) {

        int contadorDeUnos=0;

        for (int i=0;i<cromosoma.length;i++){
            if (cromosoma[i]==true){
                contadorDeUnos++;
            }//SI TIENE UN UNO
        }//FIN RECORRE CROMOSOMA

        return contadorDeUnos;

    }


////////////////////////////////////////////////////////////////////////////////////

    private ValoresFitness evaluacionDelFitness(List<boolean[]> listaDeCromosomas, ArrayList<Palomas> listaDePalomas,ArrayList<HistorialPerformance> historialPerformance, double distancia) {

        ValoresFitness vf;
        double kilometrosDelPuntoDeSuelta=distancia;
        float[] fitnessIndividual = new float[listaDePalomas.size()];
        float[] fitnessPorCromosoma= new float[listaDeCromosomas.size()];
        float fitnessTotal = 0;
        float acumulado=0;

        for(int i=0; i < listaDeCromosomas.size() ; i++) {
            boolean[] cromosomaI = listaDeCromosomas.get(i);
            ArrayList<Float> velProm ;
            ArrayList<Float> distAcum;
            float AcumFitnessIndividual=0;
            int contador;
            int idPaloma;

            for (int j = 0; j < cromosomaI.length; j++) {
                if (cromosomaI[j] == true) {
                    idPaloma=listaDePalomas.get(j).getId_Paloma();
                    velProm = new ArrayList<>();
                    distAcum = new ArrayList<>();
                    contador=0;

                    //LLENADO DE VECTORES DISTANCIA Y VELOCIDAD
                    for(int h = 0; h< historialPerformance.size(); h++){    ////////////////////////// AGREGAR EL ELSE SINO TIENE HISTORIAL DEL PERFORMANCE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                        if(historialPerformance.get(h).getIdPaloma()==idPaloma){

                            if (distAcum.size() == 0) {
                                distAcum.add((float) (historialPerformance.get(h).getDistancia()));
                                velProm.add((float) historialPerformance.get(h).getVelocidad());
                                acumulado=(float) historialPerformance.get(h).getVelocidad();
                                contador++;

                            }else{
                                contador++;
                                distAcum.add((float) (historialPerformance.get(h).getDistancia() + distAcum.get(distAcum.size()-1)));
                                acumulado=acumulado + (float) historialPerformance.get(h).getVelocidad();
                                velProm.add((float) (acumulado / contador) );
                            }
                        }
                    }//FIN LLENADO DE VECTORES     H

//////////////////////////////////////////////////////////////////////////////

                /*//IMPRESION DE VECTORES BIEN DETALLADO LOS PRINT
                    System.out.println("PALOMA: "+idPaloma);
                    System.out.println("velProm[] \t\t\t distAcu[]");
                        for (int t = 0; t < velProm.size(); t++) {
                            System.out.println("   " + velProm.get(t) + "\t\t\t" + "   " + distAcum.get(t));
                        }
                   */
/////////////////////////////////////////////////////////////////////////////

                    //BUSQUEDA MEJOR FITNES INDIVIDUAL VECTOR DE VELOCIDAD PROM
                    for(int h=0; h<distAcum.size();h++){
                        if(distAcum.get(h) <= kilometrosDelPuntoDeSuelta){
                            //NO HAGO NADA
                        }else{
                            fitnessIndividual[j] = velProm.get(h-1);
                            //System.out.println("Para la distancia de: "+kilometrosDelPuntoDeSuelta + " tiene una vel prom de: " +fitnessIndividual[j]);
                            break;
                        }
                    }//FIN MEJOr VELOCIDAD PROM     H-2

                AcumFitnessIndividual = AcumFitnessIndividual + fitnessIndividual[j];
                }//FIN SI EL ALELO ES TRUE
            }//fin for de alelos    J

           // System.out.println("Acumulacion del fitnes por cromosoma: "+AcumFitnessIndividual);
            fitnessPorCromosoma[i] = AcumFitnessIndividual / CANT_DE_INDIVIDUOS_A_SELECCIONAR_EN_CROMOSOMA;
            //System.out.println("fitness por cromosoma: "+fitnessPorCromosoma[i]);
            fitnessTotal=fitnessTotal+fitnessPorCromosoma[i];
        }//fin for de cromosomas    I

        vf= new ValoresFitness(fitnessPorCromosoma,fitnessTotal);
        return vf;
    }


    //////////////////////////////////////////////////////////////////////////////////

    private List<boolean[]> seleccion(ValoresFitness vf, List<boolean[]> listaDeCromosomas) {

        float[] valoresFitnesPorCromosoma=vf.getFitnessPorCromosoma();
        List<boolean[]> listadeCromosomasSeleccionados=listaDeCromosomas;

        //seleccion por torneo, se seleccionan las parejas inversas en el vector por ej el primero con el ultimo, el segundo con el anteultimo...etc.

        for(int i=0;i<listaDeCromosomas.size()/CANT_DE_INDIVIDUOS_SELECCIONADOS_PARA_TORNEO;i++){

            //System.out.println("Compara: "+valoresFitnesPorCromosoma[i]+" Con "+valoresFitnesPorCromosoma[(valoresFitnesPorCromosoma.length-1)-i]);

            if(valoresFitnesPorCromosoma[i]<=valoresFitnesPorCromosoma[(valoresFitnesPorCromosoma.length-1)-i]){

                //System.out.println("El mayor es: "+valoresFitnesPorCromosoma[(valoresFitnesPorCromosoma.length-1)-i]);
                listadeCromosomasSeleccionados.set(i,listaDeCromosomas.get((valoresFitnesPorCromosoma.length-1)-i));

            }else{
               // System.out.println("El mayor es: "+ valoresFitnesPorCromosoma[i]);
                listadeCromosomasSeleccionados.set((valoresFitnesPorCromosoma.length-1)-i,listaDeCromosomas.get(i));
            }

        }//FIN DEL FOR


        //IMPRIME CROMOSOMAS DESPUES DE SELECCIONAR Y REEMPLAZARLOS
        for(int i=0;i<listaDeCromosomas.size();i++){
            boolean[] temp= listaDeCromosomas.get(i);

            /*for(int j=0;j<temp.length;j++){
                System.out.print(temp[j]+" ,");
            }
            System.out.println("");
             */
        }
        //FIN IMPRESION

        return listadeCromosomasSeleccionados; //devuelve la lista de cromosomas seleccionados y duplicados
    }//FIN SELECCION

// /////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
