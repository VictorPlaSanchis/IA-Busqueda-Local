import aima.search.framework.HeuristicFunction;



/**
 * Clase para la representación del problema IA-Energia
 * */
public class EnergiaHeuristicFunction4 implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        //HEURISTICA 2: MINIMIZAR EL LOGARITMO DE LA SUMA DEL COSTE MAS LA INDEMNIZACION
        EnergiaBoard estat = (EnergiaBoard) state;
        return -(Math.log(estat.calculaCosteMasIndemnizacion()));
    }

}

