import IA.Energia.Cliente;
import aima.search.framework.GoalTest;

public class EnergiaGoalTest implements GoalTest {
	public boolean isGoalState(Object aState) {
		EnergiaBoard estat = (EnergiaBoard) aState;
		boolean goalState = true;
		int n = estat.getGarantizados().size();
		int i = 0;
		while(goalState && i<n) {
			goalState = estat.getGarantizados().get(i) != -1;
			++i;
		}
		//return goalState;
		return false; // sha dimplementar aqui
	}
}