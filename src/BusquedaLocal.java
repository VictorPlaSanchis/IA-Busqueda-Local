import IA.Centrals.CentralsJFrame;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.framework.SuccessorFunction;
import aima.search.informed.DepthLimitedSearch;
import aima.search.informed.HillClimbingSearch;

import java.util.*;


public class BusquedaLocal {

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }

    private static void hillClimbing(EnergiaBoard board, int opcio, Object s){
        try {
            long start_time = System.nanoTime();
            //Problem problem = new Problem(board, new P1Successor(), new P1Goal(), new P1Heuristica());
            Problem problem = new Problem(board, (SuccessorFunction) s, new EnergiaGoalTest(), new EnergiaHeuristicFunction1());
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);
            long end_time = System.nanoTime();
            double diff = (end_time-start_time) / 1e6;

            //System.out.println();
            //printActions(agent.getActions());
            //printInstrumentation(agent.getInstrumentation());

            EnergiaBoard sol = (EnergiaBoard) search.getGoalState();
            System.out.println("Temps: " + (diff/1e3) + " segons.");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){

        EnergiaBoard board = new EnergiaBoard(
                new int[]{5, 10, 15},
                1,
                1000,
                new double[]{0.25,0.30,0.45},
                0.75,
                0 );
        board.generarEstadoInicial(1);

        System.out.println("Starting being uwu...");
        try{
            Problem problem = new Problem(
                    (EnergiaBoard)board,
                    board.getSuccessorFunction(),
                    board.energiaGoalTest,
                    board.getHeuristicFunction(4)
            );
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        }
        catch (Exception e) {
            e.printStackTrace();
        };


        /*
        uwu.generarEstadoInicial(1)

        System.out.println(uwu.calculaBeneficios());
        */

        System.out.println("uwu...");

    }
}
