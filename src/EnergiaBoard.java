
import java.sql.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import IA.Energia.Central;
import IA.Energia.Centrales;
import IA.Energia.Cliente;
import IA.Energia.Clientes;

/**
 *
 * @author Maria Montalvo y Victor Pla
 */


/**
 * Clase para la representación del problema IA-Energia
 * */
public class EnergiaBoard {

    public static void main(String[] args){
        EnergiaBoard eb = new EnergiaBoard(new int[] {10,10,10}, 0, 100, new double[]{0.20,0.40,0.40}, 0.45, 0);
        eb.generarEstadoInicial(0);
        System.out.println("uwu");
    }

    //Numero de centrales electricas
    private static int nCentrales;

    //Numero de clientes garantizados
    private static int nGarantizados;

    //Numero de clientes no garantizados
    private static int nNoGarantizados;

    //ArrayList para almacenar las centrales
    private static Centrales centrales;

    private static int centralesSeed;

    private static int[] tiposCentral;

    //ArrayList para almacenar los clientes
    private static Clientes clientes;

    private static int nClientes;

    private static double[] propClientes;

    private static double propGarant;

    private static int clientesSeed;

    private static ArrayList<Cliente> clientesG;

    private static ArrayList<Cliente> clientesNoG;

    private static ArrayList<Double> energiaPendiente;

    private static ArrayList<Integer> asignacionG;
    private static ArrayList<Integer> asignacionNG;

    private Random random;

    public EnergiaBoard(EnergiaBoard estado){
        nCentrales = estado.nCentrales;
        nGarantizados = estado.nGarantizados;
        nNoGarantizados = estado.nNoGarantizados;
        try {
            centrales = new Centrales(estado.tiposCentral, estado.centralesSeed);
            clientes = new Clientes(estado.nClientes, estado.propClientes, estado.propGarant, estado.clientesSeed);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        clientesG = new ArrayList<Cliente>(estado.clientesG);
        clientesNoG = new ArrayList<Cliente>(estado.clientesNoG);
        energiaPendiente = new ArrayList<Double>(estado.energiaPendiente);
        asignacionG = new ArrayList<Integer>(estado.asignacionG);
        asignacionNG = new ArrayList<Integer>(estado.asignacionNG);
    }

    /** Crea una nueva instancia de EnergiaBoard */
    public EnergiaBoard(int[] cent, int centralesSeed, int ncl, double[] propc, double propg, int clientesSeed) {
        this.centralesSeed = centralesSeed;
        this.tiposCentral = cent;
        this.nClientes = ncl;
        this.propClientes = propc;
        this.propGarant = propg;
        this.clientesSeed = clientesSeed;
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
            if (cliente.getTipo() == Cliente.NOGARANTIZADO) clientesNoG.add(cliente);
            else clientesG.add(cliente);
        }

        nGarantizados = clientesG.size();
        nNoGarantizados = clientesNoG.size();

        nCentrales = centrales.size();
        energiaPendiente = new ArrayList<Double>();
        for(int i = 0; i<nCentrales; ++i){
            energiaPendiente.add(centrales.get(i).getProduccion());
        }
        asignacionG = new ArrayList<Integer>();
        asignacionNG = new ArrayList<Integer>();
        random = new Random();
    }

    private double calculaCoste(int indexCentral){
        //La central no ha producido
        if(energiaPendiente.get(indexCentral)==centrales.get(indexCentral).getProduccion()){
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
        for(int i=0; i<nCentrales; ++i){
            ingreso += calculaIngreso(i);
            coste += calculaCoste(i);
        }
        return ingreso - coste - calculaIndemnizacion();
    }

    private double calculaProduccionDistancia(Cliente cliente, Central central) {
        double consumo = cliente.getConsumo();
        double distancia = Math.sqrt(
                Math.pow(cliente.getCoordX()-central.getCoordX(),2)+
                Math.pow(cliente.getCoordY()-central.getCoordY(),2)
        );

        if(distancia <= 10) return consumo;
        else if(distancia <= 25) return consumo * (0.1 + 1.0);
        else if(distancia <= 50) return consumo * (0.2 + 1.0);
        else if(distancia <= 75) return consumo * (0.4 + 1.0);
        else return consumo * (0.6 + 1.0);
    }

    public void generarEstadoInicial(int opt){
        switch (opt){
            //Caso 0: colocar clientes en una central mientras la potencia remanente sea > 0
            case 0:
                double energiaActualizada;
                int i = 0;
                int indexCentral = 0;
                while(i<nGarantizados && indexCentral<nCentrales){
                    double produccionCliente = calculaProduccionDistancia(clientesG.get(i),centrales.get(indexCentral));
                    if(energiaPendiente.get(indexCentral)>=produccionCliente){
                        asignacionG.add(indexCentral);
                        energiaActualizada = energiaPendiente.get(indexCentral) - produccionCliente;
                        energiaPendiente.set(indexCentral, energiaActualizada);
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
                        if(energiaPendiente.get(indexCentral)>=produccionCliente){
                            asignacionNG.add(indexCentral);
                            energiaActualizada = energiaPendiente.get(indexCentral) - produccionCliente;
                            energiaPendiente.set(indexCentral, energiaActualizada);
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
                break;
            //Caso 1: Asignacion random de clientes a centrales
            case 1:
                for(Cliente cliente : clientesG){
                    int nuevaCentral = random.nextInt(nCentrales);
                    int iteraciones = 0;
                    double consumoCliente = calculaProduccionDistancia(cliente, centrales.get(nuevaCentral));
                    while(energiaPendiente.get(nuevaCentral)<consumoCliente) {
                        nuevaCentral = random.nextInt(nCentrales);
                        consumoCliente = calculaProduccionDistancia(cliente, centrales.get(nuevaCentral));
                        if(iteraciones > nCentrales * 2) {
                            nuevaCentral = -1;
                            break;
                        }
                        iteraciones++;
                    }
                    energiaPendiente.set(nuevaCentral, energiaPendiente.get(nuevaCentral) - consumoCliente);
                    asignacionG.add(nuevaCentral);
                }
                for(Cliente cliente : clientesNoG){
                    int nuevaCentral = random.nextInt(nCentrales);
                    int iteraciones = 0;
                    double consumoCliente = calculaProduccionDistancia(cliente, centrales.get(nuevaCentral));
                    while(energiaPendiente.get(nuevaCentral)<consumoCliente) {
                        nuevaCentral = random.nextInt(nCentrales);
                        consumoCliente = calculaProduccionDistancia(cliente, centrales.get(nuevaCentral));
                        if(iteraciones > nCentrales * 2) {
                            nuevaCentral = -1;
                            break;
                        }
                        iteraciones++;
                    }
                    energiaPendiente.set(nuevaCentral, energiaPendiente.get(nuevaCentral) - consumoCliente);
                    asignacionNG.add(nuevaCentral);
                }
                //System.out.println(asignacionG);
                //System.out.println(asignacionNG);
                //System.out.println(asignacionG.size());
                //System.out.println(asignacionNG.si);
                //System.out.println(clientesG);
                //System.out.println(clientesNoG);
                break;

        }
        System.out.println("Generacion estado inicial done!");
    }

    public boolean moveClient(Cliente cl1, int indexCliente, int indexCentral){
        Central cent = centrales.get(indexCentral);
        if (cl1.getTipo()==Cliente.GARANTIZADO) {
            //Se puede cambiar el cliente a la nueva central
            if(calculaProduccionDistancia(cl1,cent)<=energiaPendiente.get(indexCentral)) {
                asignacionG.set(indexCliente, indexCentral);
                return true;
            }
        }
        else{
            if(calculaProduccionDistancia(cl1,cent)<=energiaPendiente.get(indexCentral)) {
                asignacionNG.set(indexCliente, indexCentral);
                return true;
            }
        }
        //No se puede cambiar el cliente a la nueva central
        return false;
    }

    public boolean swapCliente(Cliente cl1, Cliente cl2, int indexCliente1, int indexCliente2, int indexCentral1, int indexCentral2){
        Central c1 = centrales.get(indexCentral1);
        Central c2 = centrales.get(indexCentral2);

        double energiaP1 = energiaPendiente.get(indexCentral1);
        double energiaP2 = energiaPendiente.get(indexCentral2);
        double consumoC1 = calculaProduccionDistancia(cl1,c1);
        double consumoC2 = calculaProduccionDistancia(cl2,c2);

        boolean swap = (energiaP2+consumoC2)>=consumoC1 && (energiaP1+consumoC1)>=consumoC2;

        if(swap){
            if(cl1.getTipo()==Cliente.GARANTIZADO){
                if(cl2.getTipo()==Cliente.GARANTIZADO){
                    asignacionG.set(indexCliente1, indexCentral2);
                    asignacionG.set(indexCliente2, indexCentral1);
                }
                else{
                    asignacionG.set(indexCliente1, indexCentral2);
                    asignacionNG.set(indexCliente2, indexCentral1);
                }
            }
            else{
                if(cl2.getTipo()==Cliente.GARANTIZADO){
                    asignacionNG.set(indexCliente1, indexCentral2);
                    asignacionG.set(indexCliente2, indexCentral1);
                }
                else{
                    asignacionNG.set(indexCliente1, indexCentral2);
                    asignacionNG.set(indexCliente2, indexCentral1);
                }
            }
            return true;
        }
        return false;

    }
    public boolean isGoalState() {
        return false;
    }

    public double calculaLogPotenciaRemanente() {
        double potencia = 0.0;
        for (int i = 0; i < nGarantizados; ++i) {
            potencia += energiaPendiente.get(asignacionG.get(i));
        }
        for (int i = 0; i < nNoGarantizados; ++i) {
            potencia += energiaPendiente.get(asignacionG.get(i));
        }
        return Math.log(potencia);
    }

    public double calculaPowPotenciaRemanente() {
        double potencia = 0.0;
        for (int i = 0; i < nGarantizados; ++i) {
            potencia += energiaPendiente.get(asignacionG.get(i));
        }
        for (int i = 0; i < nNoGarantizados; ++i) {
            potencia += energiaPendiente.get(asignacionG.get(i));
        }
        return Math.pow(potencia, 2);
    }

    public double calculaCosteTransporte() {
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
    }

    public double calculaLogCosteIndemnización(){
        double coste=0.0;
        for(int i=0; i<nCentrales; ++i){
            coste += calculaCoste(i);
        }
        return Math.log(coste + this.calculaIndemnizacion());
    }

    public double calculaPowCosteIndemnización(){
        double coste=0.0;
        for(int i=0; i<nCentrales; ++i){
            coste += calculaCoste(i);
        }
        return Math.pow((coste + this.calculaIndemnizacion()), 2);
    }

}

