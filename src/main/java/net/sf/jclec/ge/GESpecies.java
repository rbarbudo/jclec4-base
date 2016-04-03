package net.sf.jclec.ge;

import net.sf.jclec.IProvider;
import net.sf.jclec.ISpecies;

/**
 * Abstract implementation for ISpecies.
 * 
 * @author Rafael Barbudo Lunar
 */

@SuppressWarnings("serial")
public abstract class GESpecies implements ISpecies 
{	
	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////

	/** Genotype Schema */
	
	protected GESchema genotypeSchema;
	
	/** Individuals provider */
	
	protected IProvider provider;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor
	 */
	
	public GESpecies()
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
		
	/**
	 * Factory method.
	 * 
	 * @param genotype Individual genotype.
	 * 
	 * @return A new instance of represented class
	 */
	
	public abstract GEIndividual createIndividual(int [] genotype);

	/**
	 * Informs about individual genotype length.
	 * 
	 * @return Individual genotype length
	 */
	
	public int getGenotypeLength()
	{
		return genotypeSchema.individualArrayGenotype.length;
	}

	/**
	 * Get the genotype schema
	 * 
	 * @return This genotype schema
	 */

	public GESchema getGenotypeSchema()
	{
		return genotypeSchema;
	}

	/**
	 * Set the provider used in the creation of new individuals
	 * 
	 * @param provider Individual provider
	 */
	
	public void setProvider(GECreator provider) 
	{
		this.provider = provider;
	}
}