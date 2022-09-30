/*
 * ConnectatHeuristicFunction1.java
 *
 * Created on 22 de marzo de 2006, 17:18
 */

import java.util.Comparator;
import java.util.ArrayList;

import IA.Connectat.ConnectatBoard;
import aima.search.framework.HeuristicFunction;

public class ConnectatHeuristicFunction1 implements HeuristicFunction  
{
  
  public double getHeuristicValue(Object state) 
  {
		ConnectatBoard CBoard=(ConnectatBoard)state;
		return CBoard.getErrorTotal();						
	}
}

