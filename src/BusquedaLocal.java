import IA.Centrals.CentralsJFrame;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.framework.SuccessorFunction;
import aima.search.informed.DepthLimitedSearch;
import aima.search.informed.HillClimbingSearch;
import aima.util.Pair;

import java.util.*;


public class BusquedaLocal {

    private static ArrayList<Integer> defaultParams = new ArrayList<>(Arrays.asList(0,5,5,10,15,1,1000,25,30,45,75,0,1));
    private static HashMap<String, Integer> paramsTranslator = new HashMap<String, Integer>() {{
        put("-a", 0);
        put("-h", 1);
        put("-nCt1", 2);
        put("-nCt2", 3);
        put("-nCt3", 4);
        put("-CtSeed", 5);
        put("-nCl", 6);
        put("-pCl1", 7);
        put("-pCl2", 8);
        put("-pCl3", 9);
        put("-pG", 10);
        put("-ClSeed", 11);
        put("-EI", 12);
    }};
    /*
    * Param1 : "-a" : 0(HillClimbing) 1(SimulatedAnnealing)
    * Param2 : "-h" : i(HeuristicFunction_i)
    * Param3 : ...
    */
    private static ArrayList<Integer> readParams(String[] args) {
        ArrayList<Integer> params = defaultParams;
        if(args.length % 2 != 0) return params;
        else {
            for(int i=0; i<args.length; i += 2) {
                params.set(paramsTranslator.get(args[i]), Integer.valueOf(args[i+1]));
            }
            System.out.println(params);
            return params;
        }
    }

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

    private static EnergiaBoard hillClimbing(EnergiaBoard board, int heuristicParam){
        try {
            Problem problem = new Problem(board, board.getSuccessorFunction(), new EnergiaGoalTest(), board.getHeuristicFunction(heuristicParam));
            Search search = new HillClimbingSearch();
            SearchAgent searchAgent = new SearchAgent(problem,search);
            printActions(searchAgent.getActions());
            printInstrumentation(searchAgent.getInstrumentation());
            return (EnergiaBoard) search.getGoalState();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args){


        ArrayList<Integer> params = readParams(args);

        EnergiaBoard board = new EnergiaBoard(
                new int[]{params.get(paramsTranslator.get("-nCt1")), params.get(paramsTranslator.get("-nCt2")), params.get(paramsTranslator.get("-nCt3"))},
                params.get(paramsTranslator.get("-CtSeed")),
                params.get(paramsTranslator.get("-nCl")),
                new double[]{params.get(paramsTranslator.get("-pCl1")) / 100.0,params.get(paramsTranslator.get("-pCl2")) / 100.0,params.get(paramsTranslator.get("-pCl3")) / 100.0},
                params.get(paramsTranslator.get("-pG")) / 100.0,
                params.get(paramsTranslator.get("-ClSeed")));
        board.generarEstadoInicial(params.get(paramsTranslator.get("-EI")));

        try{
            Problem problem = new Problem(
                    (EnergiaBoard)board,
                    board.getSuccessorFunction(),
                    board.energiaGoalTest,
                    board.getHeuristicFunction(5)
            );

            long start_time = System.nanoTime();
            EnergiaBoard solution = null;
            // HillClimbing
            if(params.get(paramsTranslator.get("-a")) == 0) {
                solution = hillClimbing(board,params.get(paramsTranslator.get("-h")));
            }
            // SimulatedAnnealing
            else if(params.get(paramsTranslator.get("-a")) == 1) {
                solution = hillClimbing(board,params.get(paramsTranslator.get("-h")));
            }
            long end_time = System.nanoTime();
            double diff = (end_time-start_time) / 1e6;
            System.out.println("Temps: " + (diff/1e3) + " segons.");
            System.out.println("Beneficis de la solucio: " + solution.calculaBeneficios());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
