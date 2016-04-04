package net.sf.jclec.ge;

import net.sf.jclec.ISpecies;
import net.sf.jclec.base.AbstractCreator;

/**
 * GEIndividual (and subclasses) creator.
 * 
 * @author Rafael Barbudo Lunar
 */

public abstract class GECreator extends AbstractCreator 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 3944462586672143120L;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////

	/** Associated species */
	
	protected transient GESpecies species;
	
	/** Genotype schema */
	
	protected transient GESchema schema;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor
	 */
	
	public GECreator() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	public boolean equals(Object other)
	{
		if (other instanceof GECreator)
			return true;
		else 
			return false;
	}
	
	/**
	 * Creates a new individual mapping the phenotype too
	 * 
	 * @param genotype The individual genotype
	 * 
	 * @return the new individual
	 */
	
	public abstract GEIndividual createIndividual(int [] genotype);

	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////

	/**
	 * Create a int [] genotype, filling it randomly
	 */
	
	protected final int [] createGenotype()
	{
		int gl = schema.genotypeSchema.length;
		int [] result = new int[gl];
		for(int i=0; i<gl; i++)
			result[i] = schema.genotypeSchema[i].getRandom(randgen);
		return result;
	}
		
	@Override
	protected void prepareCreation()
	{
		ISpecies spc = context.getSpecies();
		if (spc instanceof GESpecies) {
			// Sets individual species
			this.species = (GESpecies) spc;
			// Sets genotype schema
			this.schema = this.species.getSchema();
		}
		else {
			throw new IllegalStateException("Illegal species in context");
		}
	}
}