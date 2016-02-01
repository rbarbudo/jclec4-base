package net.sf.jclec.util.opt;

import java.util.List;

import net.sf.jclec.ITool;
import net.sf.jclec.IIndividual;

/**
 * Individuals optimizer.
 * 
 * @author Amelia Zafra
 * @author Sebastian Ventura
 *
 * @param <I> Type of individuals to optimize
 */

public interface IOptimizer extends ITool
{
	/**
	 * Optimize individuals set.
	 * 
	 * @param inds Individuals to optimize
	 * 
	 * @return Optimized individuals
	 */
	
	public List<IIndividual> optimize(List<IIndividual> inds);
	
	/**
	 * Optimize one individual
	 * 
	 * @param ind Individual to optimize
	 * 
	 * @return Optimized individual
	 */
	
	public IIndividual optimize(IIndividual ind);
}