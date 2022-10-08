import aima.search.framework.HeuristicFunction;



/**
 * Clase para la representación del problema IA-Energia
 * */
public class EnergiaHeuristicFunction4 implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        EnergiaBoard estat = (EnergiaBoard) state;

        return estat.calculaLogCosteIndemnización();
    }

}

