
import java.sql.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import IA.Energia.Central;
import IA.Energia.Centrales;
import IA.Energia.Cliente;
import IA.Energia.Clientes;

/**
 *
 * @author Maria Montalvo y Victor Pla
 */


/**
 * Clase para la representación del problema IA-Energia
 * */
public class EnergiaBoard {
    //Numero de centrales electricas
    private static int nCentrales;

    //Numero de clientes garantizados
    private static int nGarantizados;

    //Numero de clientes no garantizados
    private static int nNoGarantizados;

    //ArrayList para almacenar las centrales
    private static Centrales centrales;

    //ArrayList para almacenar los clientes
    private static Clientes clientes;

    private static Clientes clientesG;

    private static Clientes clientesNoG;

    private static ArrayList<Double> energiaPendiente;

    private static ArrayList<Integer> asignacionG;
    private static ArrayList<Integer> asignacionNG;

    private Random random;



    /** Crea una nueva instancia de EnergiaBoard */
    public EnergiaBoard() {
        centrales = (Centrales) new ArrayList<Central>();
        clientes = (Clientes) new ArrayList<Cliente>();

        //Separamos clientes garantizados de no garantizados
        for (Cliente cliente : clientes) {
            if (cliente.getTipo() == Cliente.NOGARANTIZADO) clientesNoG.add(cliente);
            else clientesG.add(cliente);
        }

        nGarantizados = clientesG.size();
        nNoGarantizados = clientesNoG.size();

        nCentrales = centrales.size();
        energiaPendiente = new ArrayList<Double>(nCentrales);

        for(int i = 0; i<nCentrales; ++i){
            energiaPendiente.set(i, centrales.get(i).getProduccion());
        }
        asignacionG = new ArrayList<Integer>(nGarantizados+nNoGarantizados);
    }

    public void generarEstadoInicial(int opt){
        switch (opt){
            //Caso 0: colocar clientes en una central mientras la potencia remanente sea > 0
            case 0:
                int indexCentral = 0;
                double energiaActualizada;
                int i = 0;
                while(i<nGarantizados && indexCentral<nCentrales){
                    if(energiaPendiente.get(indexCentral)>=clientesG.get(i).getConsumo()){
                        asignacionG.set(i,indexCentral);
                        energiaActualizada = energiaPendiente.get(indexCentral) - clientesG.get(i).getConsumo();
                        energiaPendiente.set(indexCentral, energiaActualizada);
                        ++i;
                    }
                    else ++indexCentral;
                }
                if(indexCentral==nCentrales){
                    while(i<nGarantizados){
                        asignacionG.set(i,-1);
                        ++i;
                    }
                    i = 0;
                    while(i<nNoGarantizados){
                        asignacionNG.set(i,-1);
                    }
                }

                else{
                    i=0;
                    while(i<nNoGarantizados && indexCentral<nCentrales){
                        if(energiaPendiente.get(indexCentral)>=clientesNoG.get(i).getConsumo()){
                            asignacionNG.set(i,indexCentral);
                            energiaActualizada = energiaPendiente.get(indexCentral) - clientesNoG.get(i).getConsumo();
                            energiaPendiente.set(indexCentral, energiaActualizada);
                            ++i;
                        }
                        else ++indexCentral;
                    }

                    if(indexCentral==nCentrales){
                        while(i<nNoGarantizados){
                            asignacionNG.set(i,-1);
                        }
                    }

                }
            //Caso 1: Asignacion random de clientes a centrales
            case 1:



        }
    }

}

