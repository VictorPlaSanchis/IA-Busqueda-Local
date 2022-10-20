import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import IA.Energia.Cliente;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class EnergiaSuccessorFunctionSA implements SuccessorFunction {

    static int option;
    static int heuristica;


    public List getSuccessors(Object aState) {

        //ARRAY DE SUCCESSORS
        ArrayList<Successor> successors = new ArrayList<>();

        //ESTAT
        EnergiaBoard board = (EnergiaBoard) aState;
        int nGarantizados = board.getGarantizados().size();
        int nNoGarantizados = board.getNGarantizados().size();
        ArrayList<Cliente> clientesG = board.getClientesGarantizados();
        ArrayList<Cliente> clientesNoG = board.getClientesNGarantizados();
        ArrayList<Double> actualEnergiaPendiente = new ArrayList<>();
        for(int central = 0; central<board.getnCentrales(); central++) {
            actualEnergiaPendiente.add(board.getEnergiaPendiente(central));
        }


        //SA

        Random r = new Random();

        //Energia: calidad de la solucion f(n)
        double energiaActual = board.getHeuristicFunction(heuristica).getHeuristicValue(board);

        //Temperatura: parametro de control
        //Los valores de la temperatura y de como decrementa esta se escogen mediante experimentacion
        double temperatura = 5;
        int clienteG = (int) (Math.random()* board.getGarantizados().size());
        int clienteNG = (int) (Math.random()* board.getNGarantizados().size());
        //Mientras la temperatura no sea 0
        while (temperatura>0){
            //Para un numero prefijado de iteraciones hacer
            for (int i=0; i< board.getnCentrales()*3;++i){
                //Introducir algo random
                int rd = r.nextInt(clienteG);
                int rd2 = r.nextInt(clienteNG);
                EnergiaBoard newBoard = new EnergiaBoard(board);
                //FALTA APLICAR OPERADORES AQUI PARA GENERAR EL NEWBOARD
                double energiaSucesor = board.getHeuristicFunction(heuristica).getHeuristicValue(board);
                //Si es mejor lo cogemos
                if(energiaSucesor>energiaActual) successors.add(new Successor("BETTER SUCCESSOR", newBoard));
                //Si no es mejor lo cogemos con cierta probabilidad
                else{
                    double incrementoEnergia = energiaSucesor - energiaActual;
                    double probabilidad = Math.pow(Math.E,incrementoEnergia/temperatura);
                    if(probabilidad>0) successors.add(new Successor("RANDOM SUCCESSOR", newBoard));
                }
                temperatura-=0-01;

            }
        }

        return successors;
    }

}
