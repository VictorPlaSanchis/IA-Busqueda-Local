import IA.Energia.Central;
import IA.Energia.Centrales;
import IA.Energia.Cliente;
import IA.Energia.Clientes;

import java.util.ArrayList;
import java.util.Random;
import aima.search.framework.HeuristicFunction;


/**
 * Clase para la representación del problema IA-Energia
 * */
public class EnergiaHeuristicFunction1 implements HeuristicFunction {
    //HEURISTICA 1: MINIMIZAR EL BENEFICIO (INGRESOS-COSTES-INDEMNIZACION) TOTAL DE LAS CENTRALES
    public double getHeuristicValue(Object state) {
        EnergiaBoard estat = (EnergiaBoard) state;
        return estat.calculaBeneficios();
    }


}

