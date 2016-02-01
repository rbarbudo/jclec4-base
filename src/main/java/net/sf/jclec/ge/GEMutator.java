package net.sf.jclec.ge;

import net.sf.jclec.ISpecies;
import net.sf.jclec.base.AbstractMutator;

/**
 * GEIndividual (and subclasses) specific mutator.
 * 
 * @author Rafael Barbudo Lunar
 */

public abstract class GEMutator extends AbstractMutator 
{	
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by eclipse */
	
	private static final long serialVersionUID = 2556901599950702927L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////

	/** Individuals species */
	
	protected transient GESpecies species; 

	/** Individuals schema */
	
	protected transient GESchema schema;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor
	 */
	
	public GEMutator() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////
	
	// AbstractMutator methods
	
	/**
	 * {@inheritDoc}
	 */
	
	@Override	
	protected void prepareMutation()
	{
		ISpecies spc = context.getSpecies();
		if (spc instanceof GESpecies) {
			// Sets individual species
			this.species = (GESpecies) spc;
			// Sets genotype schema
			this.schema = this.species.getGenotypeSchema();
		}
		else {
			throw new IllegalStateException("Invalid species in context");
		}
	}

	/**
	 * Gets a mutate locus in represented individuals
	 */
	
	protected final int getMutableLocus()
	{
		int genotypeLength = schema.individualArrayGenotype.length;
		int ml;
		do {
			ml = randgen.choose(0, genotypeLength);
		}
		while (schema.individualArrayGenotype[ml].size() == 1);
		// Return mutation point
		return ml;
	}

	/**
	 * Flip method.
	 * 
	 * @param chrom Chromosome affected
	 * @param locus Locus affected
	 */
	
	protected final void flip(int [] chrom, int locus)
	{		
		// New locus value
		int newval;
		// Choose mutated value
		do {
			newval= schema.individualArrayGenotype[locus].getRandom(randgen);
		}
		while(chrom[locus] == newval);
		// Assigns new value
		chrom[locus] = newval;
	}	
}