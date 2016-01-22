package net.sf.jclec.ge.mut;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationRuntimeException;

import net.sf.jclec.IConfigure;
import net.sf.jclec.ge.GEIndividual;
import net.sf.jclec.util.opt.IOptimizer;

public class OptOneLocusMutator extends OneLocusMutator implements IConfigure
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
	
	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	/*
	 *  Equivalente al metodo mutateNext del original pero incluyendo además
	 *  optimizacion con Nelder Mead
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
		// TODO optimiza cuando muta o cada cierto numero de generaciones (prob vs iter)
		mutant.setGenotype(mgenome);
		GEIndividual optimizado = (GEIndividual) optimizer.optimize(mutant);
		// Returns mutant
		sonsBuffer.add(optimizado);
	}
	
	@SuppressWarnings("unchecked")
	@Override
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
}
