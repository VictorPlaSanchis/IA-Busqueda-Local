import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import IA.Energia.Cliente;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class EnergiaSuccessorFunctionSA implements SuccessorFunction {

    public List getSuccessors(Object aState) {
        //ARRAY DE SUCCESSORS
        ArrayList<Successor> successors = new ArrayList<>();

        //ESTAT
        EnergiaBoard board = (EnergiaBoard) aState;
        successors.add(new Successor("Random",getRandomSuccessor(board)));

        return successors;
    }

    public EnergiaBoard getRandomSuccessor(EnergiaBoard board) {

        EnergiaBoard successor = new EnergiaBoard(board);

        double DIST_MAX_SWAP = Double.MAX_VALUE;
        double DIST_MAX_MOVE = Double.MAX_VALUE;
        if(BusquedaLocal.operadorEscollit==0) {
            DIST_MAX_SWAP = 5.0;
            DIST_MAX_MOVE = 35.0;
        }

        int nGarantizados = board.getGarantizados().size();
        int nNoGarantizados = board.getNGarantizados().size();

        ArrayList<Cliente> clientesG = board.getClientesGarantizados();
        ArrayList<Cliente> clientesNoG = board.getClientesNGarantizados();

        ArrayList<Double> actualEnergiaPendiente = new ArrayList<>();
        for(int central = 0; central<board.getnCentrales(); central++) {
            actualEnergiaPendiente.add(board.getEnergiaPendiente(central));
        }

        Random random = new Random();
        boolean successorFound = false;
        while(!successorFound) {
            int operator = BusquedaLocal.operadorEscollit; // 0 = both, 1 = swap, 2 = move
            if(operator==0) {
                operator = random.nextInt(3);
            }

            if(operator == 1) {
                System.out.println("SWAP");
                int indexClient1;
                int indexClient2;
                boolean client1Garantizado = random.nextInt(3) == 1;
                boolean client2Garantizado = random.nextInt(3) == 1;
                if(client1Garantizado) indexClient1 = random.nextInt(nGarantizados);
                else indexClient1 = random.nextInt(nNoGarantizados);
                if(client2Garantizado) indexClient2 = random.nextInt(nGarantizados);
                else indexClient2 = random.nextInt(nNoGarantizados);

                EnergiaBoard newBoard = new EnergiaBoard(board);

                Cliente cliente1;
                Cliente cliente2;
                int central1;
                int central2;

                if(client1Garantizado) {
                    central1 = newBoard.getGarantizados().get(indexClient1);
                    cliente1 = clientesG.get(indexClient1);
                }
                else {
                    central1 = newBoard.getNGarantizados().get(indexClient1);
                    cliente1 = clientesNoG.get(indexClient1);
                }
                if(client2Garantizado) {
                    central2 = newBoard.getGarantizados().get(indexClient2);
                    cliente2 = clientesG.get(indexClient2);
                }
                else {
                    central2 = newBoard.getNGarantizados().get(indexClient2);
                    cliente2 = clientesNoG.get(indexClient2);
                }

                // DIFERENTS
                if(indexClient1 == indexClient2) continue;
                // PODA DISTANCIA
                if (newBoard.calculaDistancia(client1Garantizado, indexClient1,central1) > DIST_MAX_SWAP ||
                        newBoard.calculaDistancia(client2Garantizado, indexClient2,central2) > DIST_MAX_SWAP) continue;
                // CAN SWAP

                if (newBoard.canSwapCliente(cliente1, cliente2, central1, central2)) {
                    successorFound = true;
                    newBoard.swapCliente(cliente1,cliente2,indexClient1,indexClient2,central1,central2);
                    successor = newBoard;
                } else continue;

            } else if (operator == 2) {

                EnergiaBoard newBoard = new EnergiaBoard(board);

                int indexClient;
                int indexCentral;
                boolean client1Garantizado = random.nextInt(3) == 1;
                if(client1Garantizado) {
                    indexClient = random.nextInt(nGarantizados);
                    indexCentral = newBoard.getGarantizados().get(indexClient);
                }
                else {
                    indexClient = random.nextInt(nNoGarantizados);
                    indexCentral = newBoard.getNGarantizados().get(indexClient);
                }

                if(board.calculaDistancia(client1Garantizado,indexClient,indexCentral) > DIST_MAX_MOVE) continue;
                double energia;
                if(indexCentral == -1) energia = 0.0;
                else energia = actualEnergiaPendiente.get(indexCentral);
                if(client1Garantizado && indexCentral == -1) continue;
                if (board.canMoveClient(clientesG.get(indexClient), indexClient, indexCentral, energia)) {
                    successorFound = true;
                    newBoard.moveClient(clientesG.get(indexClient), indexClient, indexCentral);
                    successor = newBoard;
                }else {
                    continue;
                }
            } else System.out.println("INVALID OPERATOR");
        }
        return successor;
    }

}
