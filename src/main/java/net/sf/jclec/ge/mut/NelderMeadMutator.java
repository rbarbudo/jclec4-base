package net.sf.jclec.ge.mut;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationRuntimeException;

import net.sf.jclec.IConfigure;
import net.sf.jclec.IPopulation;
import net.sf.jclec.ISystem;
import net.sf.jclec.ITool;
import net.sf.jclec.ge.GEIndividual;
import net.sf.jclec.ge.GEMutator;
import net.sf.jclec.util.opt.IOptimizer;

/**
 * Nelder-Mead mutator for the constant optimization
 * 
 * @author Rafael Barbudo Lunar
 */

public class NelderMeadMutator extends GEMutator implements IConfigure
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -1756869206810217866L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////

	/** Local optimizer */
	
	protected IOptimizer optimizer;
	
	/** Optimization frequency in number of iterations */
	
	protected int frequency;
	
	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 * {@inheritDoc}
	 */
	
	protected void mutateNext() 
	{
		// Genome length
		int gl = species.getGenotypeLength();
		// Individual to mutate
		GEIndividual mutant = 
			(GEIndividual) parentsBuffer.get(parentsCounter);
		// Creates mutant genotype
		int [] mgenome = new int[gl];
		System.arraycopy(mutant.getGenotype(), 0, mgenome, 0, gl);
		// Choose mutation point
		int mp = getMutableLocus();
		// Flip selected point
		flip(mgenome, mp);
		mutant.setGenotype(mgenome);
		
		/*SyntaxTree phenotype = mutant.getPhenotype();
		System.out.println("antes de optimizar");
		for(int i=0; i<phenotype.size(); i++)
			if(phenotype.getNode(i).getSymbol().equals("cte"))
				System.out.print(((Constant)((TerminalNode)phenotype.getNode(i)).getCode()).getValue()+" ");
		System.out.println();*/
		
		// Optimize individual
		GEIndividual optimizado = (GEIndividual) optimizer.optimize(mutant);
		
		/*phenotype = optimizado.getPhenotype();
		System.out.println("despues de optimizar");
		for(int i=0; i<phenotype.size(); i++)
			if(phenotype.getNode(i).getSymbol().equals("cte"))
				System.out.print(((Constant)((TerminalNode)phenotype.getNode(i)).getCode()).getValue()+" ");
		System.out.println();*/
		
		// Returns mutant
		sonsBuffer.add(optimizado);
	}
	
	/**
	 * Configuration method for the optimizer
	 * 
	 * @param settings Set of parameters needed to configure the optimizer
	 */
	
	@SuppressWarnings("unchecked")
	public void configure(Configuration configuration) {

		try {
			// Get the local optimizer
			String optimizerName = configuration.getString("optimizer[@type]");			
			// Optimizer class
			Class<? extends IOptimizer> optimizerClass = 
				(Class<? extends IOptimizer>) Class.forName(optimizerName);
			// Optimizer instance
			IOptimizer optimizer = optimizerClass.newInstance();
			// Configure species
			if (optimizer instanceof IConfigure) {
				((IConfigure) optimizer).configure(configuration.subset("optimizer"));
			}
			// Set the optimizer
			this.optimizer = optimizer;
		} 
		catch (ClassNotFoundException e) {
			throw new ConfigurationRuntimeException("Illegal optimizer classname");
		} 
		catch (InstantiationException e) {
			throw new ConfigurationRuntimeException("Problems creating an instance of optimizer", e);
		} 
		catch (IllegalAccessException e) {
			throw new ConfigurationRuntimeException("Problems creating an instance of optimizer", e);
		}	
	}
	
	public final void contextualize(ISystem context)
	{
		if(context instanceof IPopulation) {
			// Contextualize this operator
			this.context = (IPopulation) context;
			// Attach a random generator to this object
			this.randgen = context.createRandGen();
			if(this.optimizer instanceof ITool)
				this.optimizer.contextualize(context);
		}
		else {
			throw new IllegalArgumentException("This object uses a population as execution context");
		}
	}
}