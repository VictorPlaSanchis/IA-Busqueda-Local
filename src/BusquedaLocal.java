import IA.Centrals.CentralsJFrame;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.framework.SuccessorFunction;
import aima.search.informed.DepthLimitedSearch;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import aima.util.Pair;

import java.io.FileWriter;
import java.util.Scanner;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors

import java.util.*;


public class BusquedaLocal {

    public static double beneficiInicial = 0.0;
    public static int heuristicaEscollida = 0;
    public static int operadorEscollit = 0;
    private static ArrayList<Integer> defaultParams = new ArrayList<>(
            Arrays.asList(
                                0,                                  // -a: algorisme (0: HillClimbing, 1: Simulated Annealing)
                    0,   // -h: heuristica (minimitzar lenergia perduda)
                                5,                                  // -nCt1: numero centrals tipus A
                                10,                                 // -nCt2: numero centrals tipus B
                                25,                                 // -nCt3: numero centrals tipus C
                    1234,   // -CtSeed: SEED de centrals
                                1000,                                   // -nCl: numero de clients
                                25,                                 // -pCl1: proporcio clients tipus XG
                                30,                                 // -pCl2: proporcio clients tipus MG
                                45,                                 // -pCl3: proporcio clients tipus G
                                75,                                 // -pG: proporcio de clients Garantitzats
                    1234,   // -ClSeed: SEED de clients
                    3,   // -EI: Estat Inicial
                    0,   // -sw: SWAP or MOVE or BOTH (0: both, 1: swap, 2: move)
                                0, // -idexe: identificador dexecucio (per experiments amb parametres iguals)
                    // PARAMETRES SA
                        40000,  // -steps: paramatre de SA, steps de lalgorisme
                        7,  // -k: paramtre de SA
                        1000,  // -lamb: paramatre de SA
                        10   // -stiter: paramatre de SA
            )
    );
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
        put("-sw", 13);
        put("-idexe", 14);
        put("-steps", 15);
        put("-k", 16);
        put("-lamb", 17);
        put("-stiter", 18);
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
            //System.out.println(params);
            return params;
        }
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
            pasos = property;
        }
    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }

    static String pasos;

    private static EnergiaBoard hillClimbing(EnergiaBoard board, int heuristicParam){
        try {
            Problem problem = new Problem(board, board.getSuccessorFunction(), new EnergiaGoalTest(), board.getHeuristicFunction(heuristicParam));
            Search search = new HillClimbingSearch();
            SearchAgent searchAgent = new SearchAgent(problem,search);
            //printActions(searchAgent.getActions());
            System.out.println("Using HC");
            printInstrumentation(searchAgent.getInstrumentation());
            return (EnergiaBoard) search.getGoalState();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static EnergiaBoard simulatedAnnealing(EnergiaBoard board, int stiter, int k, double lamb, int heuristicParam) {
        try {
            Problem problem = new Problem(board, board.getSuccessorFunctionSA(), new EnergiaGoalTest(), board.getHeuristicFunction(heuristicParam));
            Search search = new SimulatedAnnealingSearch(params.get(paramsTranslator.get("-steps")),stiter,k,1.0/lamb);
            System.out.println("Using SA");
            SearchAgent agent = new SearchAgent(problem, search);
            //printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
            return (EnergiaBoard) search.getGoalState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static ArrayList<Integer> params;
    public static void main(String[] args) throws IOException {

        params = readParams(args);

        operadorEscollit = params.get(paramsTranslator.get("-sw"));
        heuristicaEscollida = params.get(paramsTranslator.get("-h"));

        EnergiaBoard board = new EnergiaBoard(
                new int[]{params.get(paramsTranslator.get("-nCt1")), params.get(paramsTranslator.get("-nCt2")), params.get(paramsTranslator.get("-nCt3"))},
                params.get(paramsTranslator.get("-CtSeed")),
                params.get(paramsTranslator.get("-nCl")),
                new double[]{params.get(paramsTranslator.get("-pCl1")) / 100.0,params.get(paramsTranslator.get("-pCl2")) / 100.0,params.get(paramsTranslator.get("-pCl3")) / 100.0},
                params.get(paramsTranslator.get("-pG")) / 100.0,
                params.get(paramsTranslator.get("-ClSeed")));
        board.generarEstadoInicial(params.get(paramsTranslator.get("-EI")));
        beneficiInicial = board.calculaBeneficios();
        double bei = board.calculaBeneficios();
        System.out.println(board.getGarantizados().size());
        System.out.println("Beneficios estado inicial:" + bei);
        try{

            long start_time = System.nanoTime();
            EnergiaBoard solution = null;
            // HillClimbing
            if(params.get(paramsTranslator.get("-a")) == 0) {
                solution = hillClimbing(
                        board,
                        params.get(paramsTranslator.get("-h"))
                );
            }
            // SimulatedAnnealing
            else if(params.get(paramsTranslator.get("-a")) == 1) {
                solution = simulatedAnnealing(
                        board,
                        params.get(paramsTranslator.get("-stiter")),
                        params.get(paramsTranslator.get("-k")),
                        params.get(paramsTranslator.get("-lamb")),
                        params.get(paramsTranslator.get("-h"))
                );
            }
            long end_time = System.nanoTime();
            double diff = (end_time-start_time) / 1e6;
            System.out.println("Temps: " + (diff/1e3) + " segons.");
            assert solution != null; // recomenacio de IntellIJ, entenc que fa una excepcio si solution == null !
            System.out.println("Beneficis de la solucio: " + solution.calculaBeneficios());
            System.out.println("Ganancia de beneficios: " + (solution.calculaBeneficios() - bei));

            int clientsAsignats = solution.numeroAssignatsGarantitzats() + solution.numeroAssignatsNoGarantitzats();
            System.out.println("Clients servits: " + clientsAsignats);

            FileWriter myWriter = new FileWriter(params.get(paramsTranslator.get("-nCl"))+".txt");
            myWriter.write((diff/1e3)+"\n");
            myWriter.write(solution.calculaBeneficios()+"\n");
            myWriter.write(pasos + "\n");
            myWriter.write(clientsAsignats+" " );
            myWriter.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
