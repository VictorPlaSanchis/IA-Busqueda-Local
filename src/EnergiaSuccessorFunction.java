import java.util.ArrayList;
import java.util.List;

import IA.Energia.Cliente;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class EnergiaSuccessorFunction implements SuccessorFunction {

	public List getSuccessors(Object aState) {

		double numMillored = 0;
		double numWorses = 0;

		double DIST_MAX_SWAP = Double.MAX_VALUE;
		double DIST_MAX_MOVE = Double.MAX_VALUE;

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
		if(BusquedaLocal.operadorEscollit == 0) {
			DIST_MAX_SWAP = 5.0;
			DIST_MAX_MOVE = 35.0;
			wantSwap = true; wantMove = true;
		}
		if(BusquedaLocal.operadorEscollit ==1) {wantSwap = true; wantMove = false;}
		if(BusquedaLocal.operadorEscollit ==2) {wantSwap = false; wantMove = true;}

		if(wantSwap) {
			for (int i = 0; i < nGarantizados; ++i) {
				for (int j = 0; j < nGarantizados; ++j) {
					if (board.calculaDistancia(true, i,board.getGarantizados().get(j)) > DIST_MAX_SWAP ||
							board.calculaDistancia(true, j,board.getGarantizados().get(i)) > DIST_MAX_SWAP) continue;
					if (i == j) continue;
					if (board.canSwapCliente(clientesG.get(i), clientesG.get(j), board.getGarantizados().get(i), board.getGarantizados().get(j))) {
						//EnergiaBoard newBoard = new EnergiaBoard(board.getClientesGarantizados(),board.getClientesNGarantizados(),board.getGarantizados(),board.getNGarantizados(),board.getEnergiaPendiente());
						EnergiaBoard newBoard = new EnergiaBoard (board);
						newBoard.swapCliente(clientesG.get(i), clientesG.get(j), i, j, board.getGarantizados().get(i), board.getGarantizados().get(j));
						//if(newBoard.getEnergiaPendiente(board.getGarantizados().get(i)) < 0.0 || newBoard.getEnergiaPendiente(board.getGarantizados().get(j)) < 0.0 ) {
						//	continue;
						//}
						successors.add(new Successor("SWAP ACTION", newBoard));
						//if(EnergiaBoard.getHeuristicFunction(BusquedaLocal.heuristicaEscollida).getHeuristicValue(newBoard) > EnergiaBoard.getHeuristicFunction(BusquedaLocal.heuristicaEscollida).getHeuristicValue(board)) {
						//	numMillored++;
						//} else numWorses++;
					}
				}
				for (int j = 0; j < nNoGarantizados; ++j) {
					//if (board.calculaDistancia(true, i,false,j) > DIST_MAX_SWAP) continue;
					if (board.calculaDistancia(true, i,board.getGarantizados().get(j)) > DIST_MAX_SWAP ||
							board.calculaDistancia(false, j,board.getGarantizados().get(i)) > DIST_MAX_SWAP) continue;
					if (board.canSwapCliente(clientesG.get(i), clientesNoG.get(j), board.getGarantizados().get(i), board.getNGarantizados().get(j))) {
						//EnergiaBoard newBoard = new EnergiaBoard(board.getClientesGarantizados(),board.getClientesNGarantizados(),board.getGarantizados(),board.getNGarantizados(),board.getEnergiaPendiente());
						EnergiaBoard newBoard = new EnergiaBoard (board);
						newBoard.swapCliente(clientesG.get(i), clientesNoG.get(j), i, j, board.getGarantizados().get(i), board.getNGarantizados().get(j));
						if(board.getGarantizados().get(i) == -1 || board.getGarantizados().get(j) == -1) continue;
						//if(newBoard.getEnergiaPendiente(board.getGarantizados().get(i)) < 0.0 || newBoard.getEnergiaPendiente(board.getGarantizados().get(j)) < 0.0 ) {
						//	continue;
						//}
						successors.add(new Successor("SWAP ACTION", newBoard));
						//if(EnergiaBoard.getHeuristicFunction(BusquedaLocal.heuristicaEscollida).getHeuristicValue(newBoard) > EnergiaBoard.getHeuristicFunction(BusquedaLocal.heuristicaEscollida).getHeuristicValue(board)) {
						//	numMillored++;
						//} else numWorses++;
					}
				}

			}

			for (int i = 0; i < nNoGarantizados; ++i) {
				for (int j = 0; j < nNoGarantizados; ++j) {
					//if (board.calculaDistancia(false, i,false,j) > DIST_MAX_SWAP) continue;
					if (board.calculaDistancia(false, i,board.getGarantizados().get(j)) > DIST_MAX_SWAP ||
							board.calculaDistancia(false, j,board.getGarantizados().get(i)) > DIST_MAX_SWAP) continue;
					if (i == j) continue;
					if (board.canSwapCliente(clientesNoG.get(i), clientesNoG.get(j), board.getNGarantizados().get(i), board.getNGarantizados().get(j))) {
						//EnergiaBoard newBoard = new EnergiaBoard(board.getClientesGarantizados(),board.getClientesNGarantizados(),board.getGarantizados(),board.getNGarantizados(),board.getEnergiaPendiente());
						EnergiaBoard newBoard = new EnergiaBoard (board);
						newBoard.swapCliente(clientesNoG.get(i), clientesNoG.get(j), i, j, board.getNGarantizados().get(i), board.getNGarantizados().get(j));
						//if(newBoard.getEnergiaPendiente(board.getGarantizados().get(i)) < 0.0 || newBoard.getEnergiaPendiente(board.getGarantizados().get(j)) < 0.0 ) {
						//	continue;
						//}
						successors.add(new Successor("SWAP ACTION", newBoard));
						//if(EnergiaBoard.getHeuristicFunction(BusquedaLocal.heuristicaEscollida).getHeuristicValue(newBoard) > EnergiaBoard.getHeuristicFunction(BusquedaLocal.heuristicaEscollida).getHeuristicValue(board)) {
						//	numMillored++;
						//} else numWorses++;
					}
				}
			}
		}

		if(wantMove) {
			//Aplicant l'operador de move para clientes garantizados
			for (int i = 0; i < nGarantizados; ++i) {
				for (int j = 0; j < board.getnCentrales(); ++j) {
					//Pasamos a la funcion move: Cliente, index cliente e index central
					if(board.calculaDistancia(true,i,j) > DIST_MAX_MOVE) continue;
					if (board.canMoveClient(clientesG.get(i), i, j, actualEnergiaPendiente.get(j))) {
						EnergiaBoard newBoard = new EnergiaBoard (board);
						newBoard.moveClient(clientesG.get(i), i, j);
						//if(EnergiaBoard.getHeuristicFunction(BusquedaLocal.heuristicaEscollida).getHeuristicValue(newBoard) > EnergiaBoard.getHeuristicFunction(BusquedaLocal.heuristicaEscollida).getHeuristicValue(board)) {
						//	numMillored++;
						//} else numWorses++;
						successors.add(new Successor("MOVE ACTION", newBoard));
					}
				}
			}
			//Aplicant l'operador de move para clientes no garantizados
			for (int i = 0; i < nNoGarantizados; ++i) {
				for (int j = -1; j < board.getnCentrales(); ++j) {
					if(j != -1 && board.calculaDistancia(false,i,j) > DIST_MAX_MOVE) continue;
					//Pasamos a la funcion move: Cliente, index cliente e index central
					double energia;
					if(j == -1) energia = 0.0;
					else energia = actualEnergiaPendiente.get(j);
					if (board.canMoveClient(clientesNoG.get(i), i, j, energia)) {
						//EnergiaBoard newBoard = new EnergiaBoard(board.getClientesGarantizados(),board.getClientesNGarantizados(),board.getGarantizados(),board.getNGarantizados(),board.getEnergiaPendiente());
						EnergiaBoard newBoard = new EnergiaBoard (board);
						newBoard.moveClient(clientesNoG.get(i), i, j);
						//if(EnergiaBoard.getHeuristicFunction(BusquedaLocal.heuristicaEscollida).getHeuristicValue(newBoard) > EnergiaBoard.getHeuristicFunction(BusquedaLocal.heuristicaEscollida).getHeuristicValue(board)) {
						//	numMillored++;
						//} else numWorses++;
						successors.add(new Successor("MOVE ACTION", newBoard));
					}
				}

			}
		}
		//double per = (((1.0*numMillored)/successors.size())*100);
		//double per2 = (((1.0*numWorses)/successors.size())*100);
		//System.out.println(per + ", " + per2 + ": (" + (per + per2) + ")");
		System.out.println(EnergiaBoard.getHeuristicFunction(BusquedaLocal.heuristicaEscollida).getHeuristicValue(board));
		System.out.println("Succesors: " + successors.size()  + ", Numero Garantitzats: " + board.numeroAssignatsGarantitzats() + ", Numero NO Garantitzats: " + board.numeroAssignatsNoGarantitzats());
		return successors;
	}

}
