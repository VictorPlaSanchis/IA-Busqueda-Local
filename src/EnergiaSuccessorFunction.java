import java.util.ArrayList;
import java.util.List;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

public class EnergiaSuccessorFunction implements SuccessorFunction {

	public List getSuccessors(Object aState) {
		ArrayList<Successor> retVal = new ArrayList<>();
		EnergiaBoard estat = (EnergiaBoard) aState;


		return retVal;
	}

}
