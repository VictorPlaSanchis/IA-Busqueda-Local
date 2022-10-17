import aima.search.framework.HeuristicFunction;

import java.util.*;


/**
 * Clase para la representación del problema IA-Energia
 * */
public class EnergiaHeuristicFunction7 implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        //HEURISTICA 2: MINIMIZAR LA ENERGIA QUE SE PIERDE
        EnergiaBoard estat = (EnergiaBoard) state;
        double value = 0.0;
        for(int i=0; i<estat.getnCentrales(); i++) {
            value += estat.calculaCentralOptima(i);
        }
        return -(value);
    }

}

