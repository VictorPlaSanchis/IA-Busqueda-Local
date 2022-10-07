import aima.search.framework.HeuristicFunction;

/**
 *
 * @author Houda El Fezzak Bekkouri
 */


/**
 * Clase para la representación del problema IA-Energia
 * */
public class EnergiaHeuristicFunction5 implements HeuristicFunction {

    public double getHeuristicValue(Object state) {
        EnergiaBoard estat = (EnergiaBoard) state;

        return estat.calculaPowCosteIndemnización();
    }

}
