import aima.search.framework.GoalTest;

public class EnergiaGoalTest implements GoalTest {
	public boolean isGoalState(Object aState) {
		EnergiaBoard estat = (EnergiaBoard) aState;
		return (estat.isGoalState());
	}
}