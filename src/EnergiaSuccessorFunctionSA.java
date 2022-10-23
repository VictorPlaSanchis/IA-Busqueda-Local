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

        int nGarantizados = board.getGarantizados().size();
        int nNoGarantizados = board.getNGarantizados().size();

        ArrayList<Cliente> clientesG = board.getClientesGarantizados();
        ArrayList<Cliente> clientesNoG = board.getClientesNGarantizados();

        Random random = new Random();
        while(true) {
            int operator = BusquedaLocal.operadorEscollit; // 0 = both, 1 = swap, 2 = move
            if(operator==0) {
                operator = random.nextInt(2);
            } else operator--;

            if(operator == 0) {

               // System.out.println("SWAP");

                int indexClient1;
                int indexClient2;
                boolean client1Garantizado = random.nextInt(2) == 1;
                boolean client2Garantizado = random.nextInt(2) == 1;
                if(client1Garantizado) indexClient1 = random.nextInt(nGarantizados);
                else indexClient1 = random.nextInt(nNoGarantizados);
                if(client2Garantizado) indexClient2 = random.nextInt(nGarantizados);
                else indexClient2 = random.nextInt(nNoGarantizados);


                Cliente cliente1;
                Cliente cliente2;
                int central1;
                int central2;

                if(client1Garantizado) {
                    central1 = board.getGarantizados().get(indexClient1);
                    cliente1 = clientesG.get(indexClient1);
                }
                else {
                    central1 = board.getNGarantizados().get(indexClient1);
                    cliente1 = clientesNoG.get(indexClient1);
                }
                if(client2Garantizado) {
                    central2 = board.getGarantizados().get(indexClient2);
                    cliente2 = clientesG.get(indexClient2);
                }
                else {
                    central2 = board.getNGarantizados().get(indexClient2);
                    cliente2 = clientesNoG.get(indexClient2);
                }

                // DIFERENTS
                if(indexClient1 == indexClient2) continue;

                // CAN SWAP

                if (board.canSwapCliente(cliente1, cliente2, central1, central2)) {
                    EnergiaBoard newBoard = new EnergiaBoard(board);
                    newBoard.swapCliente(cliente1,cliente2,indexClient1,indexClient2,central1,central2);
                    return newBoard;
                }

            } else if (operator == 1) {

                //System.out.println("MOVE");

                double energia;
                int indexClient;
                int indexCentral;
                boolean client1Garantizado = random.nextInt(2) == 1;

                if(client1Garantizado) {
                    indexClient = random.nextInt(nGarantizados);
                    indexCentral = random.nextInt(0, board.getnCentrales());
                }
                else {
                    indexClient = random.nextInt(nNoGarantizados);
                    indexCentral = random.nextInt(-1, board.getnCentrales());
                }

                // EXCEPCIO ENERGIA getEnergiaPendiente(-1)...
                if(indexCentral == -1) energia = 0.0;
                else energia = board.getEnergiaPendiente(indexCentral);

                if (board.canMoveClient(clientesG.get(indexClient), indexClient, indexCentral, energia)) {
                    EnergiaBoard newBoard = new EnergiaBoard(board);
                    newBoard.moveClient(clientesG.get(indexClient), indexClient, indexCentral);
                    return newBoard;
                }
            } else System.out.println("INVALID OPERATOR");
        }
    }
}
