
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

    private static ArrayList<Cliente> clientesG;

    private static Clientes clientesNoG;

    private static ArrayList<Double> energiaPendiente;

    private static ArrayList<Integer> asignacionG;
    private static ArrayList<Integer> asignacionNG;

    private Random random;



    /** Crea una nueva instancia de EnergiaBoard */
    public EnergiaBoard(int[] cent, int centralesSeed, int ncl, double[] propc, double propg, int clientesSeed) {

        try{
            centrales =  new Centrales(cent, centralesSeed);
            clientes = new Clientes(ncl, propc, propg, clientesSeed);
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        //Separamos clientes garantizados de no garantizados
        for (Cliente cliente : clientes) {
            if (cliente.getTipo() == Cliente.NOGARANTIZADO) clientesNoG.add(cliente);
            else clientesG.add(cliente);
        }

        nGarantizados = clientesG.size();
        nNoGarantizados = clientesNoG.size();

        nCentrales = centrales.size();
        energiaPendiente = new ArrayList<Double>(nCentrales);

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
                        asignacionG.set(i,-1);
                        ++i;
                    }
                    i = 0;
                    while(i<nNoGarantizados){
                        asignacionNG.set(i,-1);
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
                            asignacionNG.set(i,-1);
                        }
                    }
                }
                break;
            //Caso 1: Asignacion random de clientes a centrales
            case 1:



        }
    }

}

