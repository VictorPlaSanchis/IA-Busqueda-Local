
import java.sql.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import IA.Energia.Central;
import IA.Energia.Centrales;
import IA.Energia.Cliente;
import IA.Energia.Clientes;
import aima.search.framework.HeuristicFunction;
import aima.search.framework.SuccessorFunction;

/**
 *
 * @author Maria Montalvo y Victor Pla
 */


/**
 * Clase para la representación del problema IA-Energia
 * */
public class EnergiaBoard implements Cloneable{

    //Numero de centrales electricas
    private static int nCentrales;
    //Numero de clientes garantizados
    private static int nGarantizados;
    //Numero de clientes no garantizados
    private static int nNoGarantizados;
    //ArrayList para almacenar las centrales
    private static Centrales centrales;
    //ArrayList para almacenar los clientes
    private static Clientes clientes;
    //ArrayList para almacenar los clientes garantizados
    private static ArrayList<Cliente> clientesG;
    //ArrayList para almacenar los clientes no garantizados
    private static ArrayList<Cliente> clientesNoG;
    //Creadora de succesors
    static EnergiaSuccessorFunction energiaSuccessorFunction;
    //Evaluadora de estado final
    static EnergiaGoalTest energiaGoalTest;
    //Heuristic functions
    static ArrayList<HeuristicFunction> heuristicFunctions;
    //ArrayList para almacenar la energia pendiente de asignar de la central iésima
    //private ArrayList<Double> energiaPendiente;
    //ArrayList para almacenar la asignación de central al cliente garantizado iésimo
    private ArrayList<Integer> asignacionG;
    //ArrayList para almacenar la asignación de central al cliente no garantizado iésimo
    private ArrayList<Integer> asignacionNG;
    private static Random random;

    /** Crea una nueva instancia de EnergiaBoard */

    //CONSTRUCTORAS
    public EnergiaBoard(){};
    public EnergiaBoard(int[] cent, int centralesSeed, int ncl, double[] propc, double propg, int clientesSeed) {
        try{
            centrales =  new Centrales(cent, centralesSeed);
            clientes = new Clientes(ncl, propc, propg, clientesSeed);
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        //Separamos clientes garantizados de no garantizados
        clientesG = new ArrayList<>();
        clientesNoG = new ArrayList<>();
        for (Cliente cliente : clientes) {
            if (cliente.getContrato() == Cliente.NOGARANTIZADO) clientesNoG.add(cliente);
            else clientesG.add(cliente);
        }

        nGarantizados = clientesG.size();
        nNoGarantizados = clientesNoG.size();

        nCentrales = centrales.size();
        asignacionG = new ArrayList<Integer>();
        asignacionNG = new ArrayList<Integer>();
        random = new Random();
        energiaSuccessorFunction = new EnergiaSuccessorFunction();
        energiaGoalTest = new EnergiaGoalTest();
        heuristicFunctions = new ArrayList<>();
        heuristicFunctions.add(new EnergiaHeuristicFunction1());
        heuristicFunctions.add(new EnergiaHeuristicFunction2());
        heuristicFunctions.add(new EnergiaHeuristicFunction3());
        heuristicFunctions.add(new EnergiaHeuristicFunction4());
        heuristicFunctions.add(new EnergiaHeuristicFunction5());
        heuristicFunctions.add(new EnergiaHeuristicFunction6());
        heuristicFunctions.add(new EnergiaHeuristicFunction7());
    }

    public EnergiaBoard (EnergiaBoard parent){
        asignacionG = new ArrayList<>(parent.getGarantizados());
        asignacionNG = new ArrayList<>(parent.getNGarantizados());
    }

    //GETTERS

    public int numeroAssignatsNoGarantitzats() {
        int clientsAsignats = 0;
        for(Integer central : getNGarantizados()) {
            if(central >= 0) clientsAsignats++;
        }
        return clientsAsignats;
    }

    public int numeroAssignatsGarantitzats() {
        int clientsAsignats = 0;
        for(Integer central : this.asignacionG) {
            if(central >= 0) clientsAsignats++;
        }
        return clientsAsignats;
    }

    public int getnCentrales() {
        return nCentrales;
    }

    public double getEnergiaPendiente(int indexCentral) {
        double produccionTotal=centrales.get(indexCentral).getProduccion();
        for(int i=0; i<asignacionG.size(); ++i){
            if(asignacionG.get(i)==indexCentral) produccionTotal-=calculaProduccionDistancia(clientesG.get(i),centrales.get(indexCentral));
        }
        for(int i=0; i<asignacionNG.size(); ++i){
            if(asignacionNG.get(i)==indexCentral) produccionTotal-=calculaProduccionDistancia(clientesNoG.get(i),centrales.get(indexCentral));
        }

        return produccionTotal;
    }
    public ArrayList<Integer> getGarantizados() { return asignacionG;}
    public ArrayList<Integer> getNGarantizados() { return asignacionNG;}
    public ArrayList<Cliente> getClientesGarantizados() { return clientesG;}
    public ArrayList<Cliente> getClientesNGarantizados() { return clientesNoG;}
    public SuccessorFunction getSuccessorFunction() {
        SuccessorFunction successorFunctionSelected = energiaSuccessorFunction;
        return successorFunctionSelected;
    }
    public HeuristicFunction getHeuristicFunction(int index) {
        HeuristicFunction heuristicFunctionSelected = heuristicFunctions.get(index);
        // TODO: Es podria fer una seleccio intelligent sobre quina heuristica agafar...
        return heuristicFunctionSelected;
    }

    //CALCULOS
    private double calculaCoste(int indexCentral){
        //La central no ha producido
        if(getEnergiaPendiente(indexCentral)==centrales.get(indexCentral).getProduccion()){
            return switch (centrales.get(indexCentral).getTipo()) {
                case Central.CENTRALA -> 15000.0;
                case Central.CENTRALB -> 5000.0;
                case Central.CENTRALC -> 1500.0;
                default -> 0.0;
            };
        }
        else{
            return switch (centrales.get(indexCentral).getTipo()) {
                case Central.CENTRALA -> centrales.get(indexCentral).getProduccion()*50 + 20000;
                case Central.CENTRALB -> centrales.get(indexCentral).getProduccion()*80 + 10000;
                case Central.CENTRALC -> centrales.get(indexCentral).getProduccion()*150 + 5000;
                default -> 0.0;
            };
        }
    }
    private double calculaIngreso(int indexCentral){
        double ingresos = 0.0;
        for(int i =0; i<nGarantizados; ++i) {
            if(asignacionG.get(i)==indexCentral){
                switch (clientesG.get(i).getTipo()) {
                    case Cliente.CLIENTEXG -> ingresos += 400.0*clientesG.get(i).getConsumo();
                    case Cliente.CLIENTEMG -> ingresos += 500.0*clientesG.get(i).getConsumo();
                    case Cliente.CLIENTEG -> ingresos += 600.0*clientesG.get(i).getConsumo();
                    default -> ingresos += 0.0;
                }
            }
        }

        for(int i =0; i<nNoGarantizados; ++i) {
            if(asignacionNG.get(i)==indexCentral){
                switch (clientesNoG.get(i).getTipo()) {
                    case Cliente.CLIENTEXG -> ingresos += 200.0*clientesNoG.get(i).getConsumo();
                    case Cliente.CLIENTEMG -> ingresos += 400.0*clientesNoG.get(i).getConsumo();
                    case Cliente.CLIENTEG -> ingresos += 500.0*clientesNoG.get(i).getConsumo();
                    default -> ingresos += 0.0;
                }
            }
        }

        return ingresos;
    }
    private double calculaIndemnizacion(){
        for(int i =0; i<nGarantizados; ++i){
            if(asignacionG.get(i)==-1) return Double.MAX_VALUE;
        }
        double indemnizacion=0.0;
        for(int i =0; i<nNoGarantizados; ++i){
            if(asignacionNG.get(i)==-1) indemnizacion+=clientesNoG.get(i).getConsumo()*50.0;
        }
        return indemnizacion;
    }
    public double calculaBeneficios() {
        double ingreso=0.0;
        double coste=0.0;

        double indemnizacion = calculaIndemnizacion();
        if(indemnizacion==Double.MAX_VALUE) return -indemnizacion;

        for(int i=0; i<nCentrales; ++i){
            ingreso += calculaIngreso(i);
            coste += calculaCoste(i);
        }
        return ingreso - coste - indemnizacion;
    }
    private double calculaProduccionDistancia(boolean garantitzat,int cliente, int central) {

        double consumo = 0.0;
        double distancia = 0.0;

        if(garantitzat) {
            consumo = EnergiaBoard.clientesG.get(cliente).getConsumo();
            distancia = calculaDistancia(garantitzat, cliente, central);
        }

        if(distancia <= 10) return consumo;
        else if(distancia <= 25) return consumo * (0.1 + 1.0);
        else if(distancia <= 50) return consumo * (0.2 + 1.0);
        else if(distancia <= 75) return consumo * (0.4 + 1.0);
        else return consumo * (0.6 + 1.0);
    }
    private double calculaProduccionDistancia(Cliente cliente, Central central) {
        double consumo = cliente.getConsumo();
        double distancia = calculaDistancia(cliente,central);

        if(distancia <= 10) return consumo;
        else if(distancia <= 25) return consumo * (0.1 + 1.0);
        else if(distancia <= 50) return consumo * (0.2 + 1.0);
        else if(distancia <= 75) return consumo * (0.4 + 1.0);
        else return consumo * (0.6 + 1.0);
    }

    public double calculaDistancia(boolean garantitzat, int cliente, boolean garantitzat2, int cliente2){
        double clienteCoordX, cliente2CoordX;
        double clienteCoordY, cliente2CoordY;
        if(garantitzat) {
            if(garantitzat2) {
                Cliente clienteObj = EnergiaBoard.clientesG.get(cliente);
                Cliente clienteObj2 = EnergiaBoard.clientesG.get(cliente2);
                clienteCoordX = clienteObj.getCoordX();
                clienteCoordY = clienteObj.getCoordY();
                cliente2CoordX = clienteObj2.getCoordX();
                cliente2CoordY = clienteObj2.getCoordY();
            } else {
                Cliente clienteObj = EnergiaBoard.clientesG.get(cliente);
                Cliente clienteObj2 = EnergiaBoard.clientesNoG.get(cliente2);
                clienteCoordX = clienteObj.getCoordX();
                clienteCoordY = clienteObj.getCoordY();
                cliente2CoordX = clienteObj2.getCoordX();
                cliente2CoordY = clienteObj2.getCoordY();
            }

        } else {
            if(garantitzat2) {
                Cliente clienteObj = EnergiaBoard.clientesNoG.get(cliente);
                Cliente clienteObj2 = EnergiaBoard.clientesG.get(cliente2);
                clienteCoordX = clienteObj.getCoordX();
                clienteCoordY = clienteObj.getCoordY();
                cliente2CoordX = clienteObj2.getCoordX();
                cliente2CoordY = clienteObj2.getCoordY();
            } else {
                Cliente clienteObj = EnergiaBoard.clientesNoG.get(cliente);
                Cliente clienteObj2 = EnergiaBoard.clientesNoG.get(cliente2);
                clienteCoordX = clienteObj.getCoordX();
                clienteCoordY = clienteObj.getCoordY();
                cliente2CoordX = clienteObj2.getCoordX();
                cliente2CoordY = clienteObj2.getCoordY();
            }
        }
        return Math.sqrt(
                Math.pow(clienteCoordX-cliente2CoordX,2)+
                        Math.pow(clienteCoordY-cliente2CoordY,2)
        );
    }

    public double calculaDistancia(boolean garantitzat, int cliente, int central){
        Central centralObj = EnergiaBoard.centrales.get(central);
        double clienteCoordX, centralCoordX = centralObj.getCoordX();
        double clienteCoordY, centralCoordY = centralObj.getCoordY();
        if(garantitzat) {
            Cliente clienteObj = EnergiaBoard.clientesG.get(cliente);
            clienteCoordX = clienteObj.getCoordX();
            clienteCoordY = clienteObj.getCoordY();
        } else {
            Cliente clienteObj = EnergiaBoard.clientesNoG.get(cliente);
            clienteCoordX = clienteObj.getCoordX();
            clienteCoordY = clienteObj.getCoordY();
        }
        return Math.sqrt(
                Math.pow(clienteCoordX-centralCoordX,2)+
                        Math.pow(clienteCoordY-centralCoordY,2)
        );
    }
    private double calculaDistancia(Cliente cliente, Central central){
        return Math.sqrt(
                Math.pow(cliente.getCoordX()-central.getCoordX(),2)+
                        Math.pow(cliente.getCoordY()-central.getCoordY(),2)
        );
    }
    public double calculaPotenciaRemanente() {
        double potencia = 0.0;
        for (int i = 0; i < nCentrales; ++i) {
            potencia += getEnergiaPendiente(i);
        }
        return potencia;
    }
    /*public double calculaCosteTransporte() {
        double coste = 0.0;
        for (int i = 0; i < nGarantizados; ++i) {
            if (asignacionG.get(i) != -1) {
                coste += calculaProduccionDistancia(clientesG.get(i), centrales.get(asignacionG.get(i)));
            }
        }
        for (int i = 0; i < nNoGarantizados; ++i) {
            if (asignacionNG.get(i) != -1) {
                coste += calculaProduccionDistancia(clientesNoG.get(i), centrales.get(asignacionNG.get(i)));
            }
        }
        return coste;
    }*/
    public double calculaCosteMasIndemnizacion(){
        double coste=0.0;
        for(int i=0; i<nCentrales; ++i){
            coste += calculaCoste(i);
        }
        return coste + calculaIndemnizacion();
    }
    public double calculaEnergiaPerdida(){
        double energiaPerdida=0.0;
        for(int i=0; i<nGarantizados;++i){
            double consumoCliente = clientesG.get(i).getConsumo();
            if(asignacionG.get(i)!=-1){
                double produccionReal = calculaProduccionDistancia(clientesG.get(i),centrales.get(asignacionG.get(i)));
                energiaPerdida+=produccionReal-consumoCliente;
            }
        }

        for(int i=0; i<nNoGarantizados;++i){
            double consumoCliente = clientesNoG.get(i).getConsumo();
            if(asignacionNG.get(i)!=-1){
                double produccionReal = calculaProduccionDistancia(clientesNoG.get(i),centrales.get(asignacionNG.get(i)));
                energiaPerdida+=produccionReal-consumoCliente;
            }
        }

        return energiaPerdida;
    }


    //ESTADO INICIAL
    private void generaEstadoInicial0(){
        double energiaActualizada;
        int i = 0;
        int indexCentral = 0;
        while(i<nGarantizados && indexCentral<nCentrales){
            double produccionCliente = calculaProduccionDistancia(clientesG.get(i),centrales.get(indexCentral));
            if(getEnergiaPendiente(indexCentral)>=produccionCliente){
                asignacionG.add(indexCentral);
                ++i;
            }
            else ++indexCentral;
        }
        if(indexCentral==nCentrales){
            while(i<nGarantizados){
                asignacionG.add(-1);
                ++i;
            }
            i = 0;
            while(i<nNoGarantizados){
                asignacionNG.add(-1);
                i++;
            }
        }
        else {
            i=0;
            while(i<nNoGarantizados && indexCentral<nCentrales){
                double produccionCliente = calculaProduccionDistancia(clientesNoG.get(i),centrales.get(indexCentral));
                if(getEnergiaPendiente(indexCentral)>=produccionCliente){
                    asignacionNG.add(indexCentral);
                    ++i;
                }
                else ++indexCentral;
            }

            if(indexCentral==nCentrales){
                while(i<nNoGarantizados){
                    asignacionNG.add(-1);
                    i++;
                }
            }
        }


    }
    private void generaEstadoInicial1(){
        for(Cliente cliente : clientesG){
            int nuevaCentral = random.nextInt(0,nCentrales-1);
            int iteraciones = 0;
            double consumoCliente = calculaProduccionDistancia(cliente, centrales.get(nuevaCentral));
            while(getEnergiaPendiente(nuevaCentral)<consumoCliente) {
                nuevaCentral = random.nextInt(nCentrales);
                consumoCliente = calculaProduccionDistancia(cliente, centrales.get(nuevaCentral));
                if(iteraciones > nCentrales * 3) {
                    nuevaCentral = -1;
                    break;
                }
                iteraciones++;
            }
            asignacionG.add(nuevaCentral);
        }
        for(Cliente cliente : clientesNoG){
            int nuevaCentral = random.nextInt(nCentrales);
            int iteraciones = 0;
            double consumoCliente = calculaProduccionDistancia(cliente, centrales.get(nuevaCentral));
            while(getEnergiaPendiente(nuevaCentral)<consumoCliente) {
                nuevaCentral = random.nextInt(nCentrales);
                consumoCliente = calculaProduccionDistancia(cliente, centrales.get(nuevaCentral));
                if(iteraciones > nCentrales) {
                    nuevaCentral = -1;
                    break;
                }
                iteraciones++;
            }
            asignacionNG.add(nuevaCentral);
        }
    }
    private void generaEstadoInicial2(){
        for(int i=0; i<nGarantizados; ++i){
            int indexCentralCercana=-1;
            double distanciaCercana=Double.MAX_VALUE;
            for(int j=0; j<nCentrales; ++j){
                if(distanciaCercana>calculaDistancia(clientesG.get(i),centrales.get(j))
                        && getEnergiaPendiente(j)>=calculaProduccionDistancia(clientesG.get(i),centrales.get(j))){
                    distanciaCercana = calculaDistancia(clientesG.get(i),centrales.get(j));
                    indexCentralCercana = j;
                }
            }

            asignacionG.add(indexCentralCercana);

        }

        for(int i=0; i<nNoGarantizados; ++i){
            int indexCentralCercana=-1;
            double distanciaCercana=Double.MAX_VALUE;
            for(int j=0; j<nCentrales; ++j){
                if(distanciaCercana>calculaDistancia(clientesNoG.get(i),centrales.get(j))
                        && getEnergiaPendiente(j)>=calculaProduccionDistancia(clientesNoG.get(i),centrales.get(j))){
                    distanciaCercana = calculaDistancia(clientesNoG.get(i),centrales.get(j));
                    indexCentralCercana = j;
                }
            }
            asignacionNG.add(indexCentralCercana);
        }
    }
    public void generarEstadoInicial(int opt){
        switch (opt){
            //Caso 0: colocar clientes en una central mientras la potencia remanente sea > 0
            case 0:
                generaEstadoInicial0();
                break;
            //Caso 1: Asignacion random de clientes a centrales
            case 1:
                generaEstadoInicial1();
                break;
            //Caso 2: Asignacion a la central más cercana
            case 2:
                generaEstadoInicial2();
                break;

        }
    }

    //OPERADORES
    public boolean canMoveClient(Cliente cl1, int indexCliente, int indexCentral, double energiaPendienteCentral){
        if (cl1.getContrato()==Cliente.GARANTIZADO)
            return (getGarantizados().get(indexCliente) != indexCentral) && (calculaProduccionDistancia(cl1, centrales.get(indexCentral)) <= energiaPendienteCentral);
        if(cl1.getContrato()==Cliente.NOGARANTIZADO)
            return indexCentral == -1 || ((getNGarantizados().get(indexCliente) != indexCentral) && (calculaProduccionDistancia(cl1, centrales.get(indexCentral)) <= energiaPendienteCentral));
        return false;
    }
    public void moveClient(Cliente cl1, int indexCliente, int indexCentral){
        if (cl1.getContrato()==Cliente.GARANTIZADO ) {
            asignacionG.set(indexCliente, indexCentral);
        }
        else {
            asignacionNG.set(indexCliente, indexCentral);
        }
    }
    public boolean canSwapCliente(Cliente cl1, Cliente cl2, int indexCentral1, int indexCentral2){

        Central c1 = indexCentral1==-1 ? null : centrales.get(indexCentral1);
        Central c2 = indexCentral2==-1 ? null : centrales.get(indexCentral2);

        double energiaP1 = indexCentral1==-1  ? Double.MAX_VALUE : getEnergiaPendiente(indexCentral1);
        double energiaP2 = indexCentral2==-1  ? Double.MAX_VALUE : getEnergiaPendiente(indexCentral2);

        double consumoC1 = c1==null ? 0.0 : calculaProduccionDistancia(cl1,c1);
        double consumoC2 = c2==null ? 0.0 : calculaProduccionDistancia(cl2,c2);
        double consumoC1toNewC2 = c2==null ? 0.0 : calculaProduccionDistancia(cl1,c2);
        double consumoC2toNewC1 = c1==null ? 0.0 : calculaProduccionDistancia(cl2,c1);

        double energiaP1Limpia = energiaP1 + consumoC1;
        double energiaP2Limpia = energiaP2 + consumoC2;

        boolean swap = (energiaP1Limpia - consumoC2toNewC1)>=0.0 && (energiaP2Limpia - consumoC1toNewC2)>=0.0;
        swap = swap && !((indexCentral1==-1 && cl2.getContrato()==Cliente.GARANTIZADO) || (indexCentral2==-1 && cl1.getContrato()==Cliente.GARANTIZADO));
        return swap;
    }
    public void swapCliente(Cliente cl1, Cliente cl2, int indexCliente1, int indexCliente2, int indexCentral1, int indexCentral2) {
        if (cl1.getContrato() == Cliente.GARANTIZADO) {
            if (cl2.getContrato() == Cliente.GARANTIZADO) {
                asignacionG.set(indexCliente1, indexCentral2);
                asignacionG.set(indexCliente2, indexCentral1);
            } else {
                asignacionG.set(indexCliente1, indexCentral2);
                asignacionNG.set(indexCliente2, indexCentral1);
            }
        }
        else {
            if (cl2.getContrato() == Cliente.GARANTIZADO) {
                asignacionNG.set(indexCliente1, indexCentral2);
                asignacionG.set(indexCliente2, indexCentral1);
            } else {
                asignacionNG.set(indexCliente1, indexCentral2);
                asignacionNG.set(indexCliente2, indexCentral1);
            }
        }
    }

    double calculaCentralOptima(int indexCentral) {
        return 1.0 / Math.pow(this.centrales.get(indexCentral).getProduccion() - this.getEnergiaPendiente(indexCentral),2);
    }

    //PRINTS
    public void printPanicMethod(){
        System.out.println(asignacionG);

    }
}

