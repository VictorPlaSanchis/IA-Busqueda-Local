import aima.search.framework.HeuristicFunction;

import java.util.*;


/**
 * Clase para la representación del problema IA-Energia
 * */
public class EnergiaHeuristicFunction7 implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        //HEURISTICA 2: MINIMIZAR LA ENERGIA QUE SE PIERDE
        EnergiaBoard estat = (EnergiaBoard) state;

        double A = estat.calculaBeneficios();
        double B = estat.calculaEnergiaPerdida();
        double C = estat.calculaPotenciaRemanente();
        double pA = -1.0 / 100.0;
        double pB = -1.0 / 0.01;
        double pC = 0.0;

        int penalitazcio = 0;
        if(estat.numeroAssignatsGarantitzats() > 750.0) penalitazcio = 1;
        else penalitazcio = 0;
        return -((A*pA) + (B*pB) + (C*pC)) * (penalitazcio);
    }

}

