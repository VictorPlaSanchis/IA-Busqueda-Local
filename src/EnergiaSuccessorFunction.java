import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import IA.Energia.Cliente;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import static java.util.List.copyOf;

public class EnergiaSuccessorFunction implements SuccessorFunction {

	public List getSuccessors(Object aState) {
		ArrayList<Successor> successors = new ArrayList<>();
		EnergiaBoard board = (EnergiaBoard) aState;

		int nGarantizados = board.getGarantizados().size();
		int nNoGarantizados = board.getNGarantizados().size();

		ArrayList<Cliente> clientesG = board.getClientesGarantizados();
		ArrayList<Cliente> clientesNoG = board.getClientesNGarantizados();

		//Aplicant l'operador de swap

		for (int i = 0; i < nGarantizados; ++i) {
			for (int j = 0; j < nGarantizados; ++j) {
				if (i == j) continue;
				if (board.canSwapCliente(clientesG.get(i), clientesG.get(j), board.getGarantizados().get(i), board.getGarantizados().get(j))) {
					EnergiaBoard newBoard = (EnergiaBoard) board.copyOf(board);
					newBoard.swapCliente(clientesG.get(i), clientesG.get(j), i, j, board.getGarantizados().get(i), board.getGarantizados().get(j));
					successors.add(new Successor("SWAP ACTION", newBoard));
				}
			}
			for (int j = 0; j < nNoGarantizados; ++j) {
				if (board.canSwapCliente(clientesG.get(i), clientesNoG.get(j), board.getGarantizados().get(i), board.getNGarantizados().get(j))) {
					EnergiaBoard newBoard = (EnergiaBoard) board.copyOf(board);
					newBoard.swapCliente(clientesG.get(i), clientesNoG.get(j), i, j, board.getGarantizados().get(i), board.getNGarantizados().get(j));
					successors.add(new Successor("SWAP ACTION", newBoard));
				}
			}

		}

		for (int i = 0; i < nNoGarantizados; ++i) {
			for (int j = 0; j < nNoGarantizados; ++j) {
				if (i == j) continue;
				if (board.canSwapCliente(clientesG.get(i), clientesG.get(j), board.getGarantizados().get(i), board.getGarantizados().get(j))) {
					EnergiaBoard newBoard = (EnergiaBoard) board.copyOf(board);
					newBoard.swapCliente(clientesG.get(i), clientesG.get(j), i, j, board.getGarantizados().get(i), board.getGarantizados().get(j));
					successors.add(new Successor("SWAP ACTION", newBoard));
				}
			}
		}


		//Aplicant l'operador de move para clientes garantizados
		for (int i = 0; i < nGarantizados; ++i) {
			for (int j = 0; j < board.getnCentrales(); ++j) {
				//Pasamos a la funcion move: Cliente, index cliente e index central
				if (board.canMoveClient(clientesG.get(i), j, board.getGarantizados().get(i))) {
					EnergiaBoard newBoard = (EnergiaBoard) board.copyOf(board);
					newBoard.moveClient(clientesG.get(i), i, board.getGarantizados().get(i));
					successors.add(new Successor("MOVE ACTION", newBoard));
				}
			}
		}
		//Aplicant l'operador de move para clientes no garantizados
		for (int i = 0; i < nNoGarantizados; ++i) {
			for (int j = 0; j < board.getnCentrales(); ++j) {
				//Pasamos a la funcion move: Cliente, index cliente e index central
				if (board.canMoveClient(clientesNoG.get(i), j, board.getNGarantizados().get(i))) {
					EnergiaBoard newBoard = (EnergiaBoard) board.copyOf(board);
					newBoard.moveClient(clientesNoG.get(i), i, board.getNGarantizados().get(i));
					successors.add(new Successor("MOVE ACTION", newBoard));
				}
			}
		}

		return successors;
	}

}
