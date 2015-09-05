package net.sf.jclec;

/**
 * This interface provides the required
 * methods that should implement those
 * individuals which represent solutions
 * for constrained problems. The use of
 * this interface is expected by the
 * constrained versions of the strategies
 * and the corresponding individuals
 * comparators.
 * 
 * <p>HISTORY:
 * <ul>
 *  li>(AR|JRR|SV, 0.1, April 2014)	Initial version.</li>
 * </ul>
 *  
 * @version 1.0 
 *  
 * @author Aurora Ramirez (AR)
 * @author Jose Raul Romero (JRR)
 * @author Sebastian Ventura (SV)
 * 
 * <p>Knowledge Discovery and Intelligent Systems (KDIS) Research Group: 
 * {@link http://www.uco.es/grupos/kdis}
 * 
 * */

public interface IConstrained 
{
	
	/**
	 * The individual is feasible or not.
	 * @return True if the individual is feasible,
	 * false otherwise
	 * */
	public boolean isFeasible();
	
	/**
	 * For infeasible individuals, get the
	 * degree of constraints violation.
	 * @return A double value representing
	 * the degree of infeasibility
	 * */
	public double degreeOfInfeasibility();
}
