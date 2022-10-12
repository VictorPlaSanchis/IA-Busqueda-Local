import aima.search.framework.HeuristicFunction;


/**
 * Clase para la representación del problema IA-Energia
 * */
public class EnergiaHeuristicFunction6 implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        //HEURISTICA 2: MINIMIZAR LA ENERGIA QUE SE PIERDE
        EnergiaBoard estat = (EnergiaBoard) state;
        //System.out.println(-(Math.log((estat.calculaEnergiaPerdida()))));
        return -(Math.log((estat.calculaEnergiaPerdida())));
    }

}

