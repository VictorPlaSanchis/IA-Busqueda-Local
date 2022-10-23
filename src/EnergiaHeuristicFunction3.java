import aima.search.framework.HeuristicFunction;



/**
 * Clase para la representación del problema IA-Energia
 * */
public class EnergiaHeuristicFunction3 implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        //HEURISTICA 2: MINIMIZAR EL CUADRADO DE LA POTENCIA PENDIENTE DE ASIGNAR DE LAS CENTRALES ACTIVAS
        EnergiaBoard estat = (EnergiaBoard) state;
        return (Math.pow(estat.calculaPotenciaRemanente(),2));
    }

}

