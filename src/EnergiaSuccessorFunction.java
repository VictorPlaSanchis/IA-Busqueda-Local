import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import IA.Energia.Cliente;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import static java.util.List.copyOf;

public class EnergiaSuccessorFunction implements SuccessorFunction {

	static int option;

	public List getSuccessors(Object aState) {
		ArrayList<Successor> successors = new ArrayList<>();
		EnergiaBoard board = (EnergiaBoard) aState;

		int nGarantizados = board.getGarantizados().size();
		int nNoGarantizados = board.getNGarantizados().size();

		ArrayList<Cliente> clientesG = board.getClientesGarantizados();
		ArrayList<Cliente> clientesNoG = board.getClientesNGarantizados();

		ArrayList<Double> actualEnergiaPendiente = new ArrayList<>();
		for(int central = 0; central<board.getnCentrales(); central++) {
			actualEnergiaPendiente.add(board.getEnergiaPendiente(central));
		}

		//Aplicant l'operador de swap
		boolean wantSwap = false; boolean wantMove = false;
		if(option==0) {wantSwap = true; wantMove = true;}
		if(option==1) {wantSwap = true; wantMove = false;}
		if(option==2) {wantSwap = false; wantMove = true;}


		if(wantSwap) {
			for (int i = 0; i < nGarantizados; ++i) {
				for (int j = 0; j < nGarantizados; ++j) {
					if (i == j) continue;
					if (board.canSwapCliente(clientesG.get(i), clientesG.get(j), board.getGarantizados().get(i), board.getGarantizados().get(j))) {
						//EnergiaBoard newBoard = new EnergiaBoard(board.getClientesGarantizados(),board.getClientesNGarantizados(),board.getGarantizados(),board.getNGarantizados(),board.getEnergiaPendiente());
						EnergiaBoard newBoard = new EnergiaBoard (board);
						newBoard.swapCliente(clientesG.get(i), clientesG.get(j), i, j, board.getGarantizados().get(i), board.getGarantizados().get(j));
						successors.add(new Successor("SWAP ACTION", newBoard));
					}
				}
				for (int j = 0; j < nNoGarantizados; ++j) {
					if (board.canSwapCliente(clientesG.get(i), clientesNoG.get(j), board.getGarantizados().get(i), board.getNGarantizados().get(j))) {
						//EnergiaBoard newBoard = new EnergiaBoard(board.getClientesGarantizados(),board.getClientesNGarantizados(),board.getGarantizados(),board.getNGarantizados(),board.getEnergiaPendiente());
						EnergiaBoard newBoard = new EnergiaBoard (board);
						newBoard.swapCliente(clientesG.get(i), clientesNoG.get(j), i, j, board.getGarantizados().get(i), board.getNGarantizados().get(j));
						successors.add(new Successor("SWAP ACTION", newBoard));
					}
				}

			}

			for (int i = 0; i < nNoGarantizados; ++i) {
				for (int j = 0; j < nNoGarantizados; ++j) {
					if (i == j) continue;
					if (board.canSwapCliente(clientesG.get(i), clientesG.get(j), board.getGarantizados().get(i), board.getGarantizados().get(j))) {
						//EnergiaBoard newBoard = new EnergiaBoard(board.getClientesGarantizados(),board.getClientesNGarantizados(),board.getGarantizados(),board.getNGarantizados(),board.getEnergiaPendiente());
						EnergiaBoard newBoard = new EnergiaBoard (board);
						newBoard.swapCliente(clientesG.get(i), clientesG.get(j), i, j, board.getGarantizados().get(i), board.getGarantizados().get(j));
						successors.add(new Successor("SWAP ACTION", newBoard));
					}
				}
			}
		}

		if(wantMove) {
			//Aplicant l'operador de move para clientes garantizados
			for (int i = 0; i < nGarantizados; ++i) {
				for (int j = 0; j < board.getnCentrales(); ++j) {
					//Pasamos a la funcion move: Cliente, index cliente e index central
					Integer indexCentral = board.getGarantizados().get(i);
					double energia;
					if(indexCentral == -1) energia = 0.0;
					else energia = actualEnergiaPendiente.get(indexCentral);
					if (board.canMoveClient(clientesG.get(i), i, indexCentral, energia)) {
						//EnergiaBoard newBoard = new EnergiaBoard(board.getClientesGarantizados(),board.getClientesNGarantizados(),board.getGarantizados(),board.getNGarantizados(),board.getEnergiaPendiente());
						EnergiaBoard newBoard = new EnergiaBoard (board);
						newBoard.moveClient(clientesG.get(i), i, j);
						successors.add(new Successor("MOVE ACTION", newBoard));
					}
				}
			}
			//Aplicant l'operador de move para clientes no garantizados
			for (int i = 0; i < nNoGarantizados; ++i) {
				for (int j = 0; j < board.getnCentrales(); ++j) {
					//Pasamos a la funcion move: Cliente, index cliente e index central
					Integer indexCentral = board.getNGarantizados().get(i);
					double energia;
					if(indexCentral == -1) energia = 0.0;
					else energia = actualEnergiaPendiente.get(indexCentral);
					if (board.canMoveClient(clientesNoG.get(i), i, indexCentral, energia)) {
						//EnergiaBoard newBoard = new EnergiaBoard(board.getClientesGarantizados(),board.getClientesNGarantizados(),board.getGarantizados(),board.getNGarantizados(),board.getEnergiaPendiente());
						EnergiaBoard newBoard = new EnergiaBoard (board);
						newBoard.moveClient(clientesNoG.get(i), i, j);
						successors.add(new Successor("MOVE ACTION", newBoard));
					}
				}
			}
		}


		return successors;
	}

}
