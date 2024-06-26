import aima.search.framework.HeuristicFunction;



/**
 * Clase para la representación del problema IA-Energia
 * */
public class EnergiaHeuristicFunction5 implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        //HEURISTICA 2: MINIMIZAR EL CUADRADO DE LA SUMA DEL COSTE MAS LA INDEMNIZACION
        EnergiaBoard estat = (EnergiaBoard) state;
        return -(Math.pow(estat.calculaCosteMasIndemnizacion(),2));
    }

}

