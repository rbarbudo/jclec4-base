package net.sf.jclec.ge.rec;

import net.sf.jclec.ge.GEIndividual;
import net.sf.jclec.ge.GERecombinator;

/**
 * The tipical One Point Crossover operator applied to GE
 * 
 * @author Rafael Barbudo Lunar
 */

public class RippleCrossover extends GERecombinator 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by eclipse */
	
	private static final long serialVersionUID = -7708378524323292937L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////

	/**
	 * Empty constructor
	 */

	public RippleCrossover() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// java.lang.Object methods

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof RippleCrossover)
			return true;
		else
			return false;
	}	

	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////

	// AbstractRecombinator methods

	/**
	 * {@inheritDoc}
	 */

	@Override	
	protected void recombineNext() 
	{
		// Genotype length
		int gl = species.getGenotypeLength();
		// Parents genotypes
		int [] p0_genome = 
			( (GEIndividual) parentsBuffer.get(parentsCounter)).getGenotype();
		int [] p1_genome = 
			( (GEIndividual) parentsBuffer.get(parentsCounter+1)).getGenotype();
		// Creating sons genotypes
		int [] s0_genome = new int[gl];
		int [] s1_genome = new int[gl];
		// Sets a crossover point
		int cp = randgen.choose(1, gl-1);
		// First son genotype
		System.arraycopy(p0_genome,  0, s0_genome,  0, cp);
		System.arraycopy(p1_genome, cp, s0_genome, cp, gl-cp);
		// Second son genotype
		System.arraycopy(p1_genome,  0, s1_genome,  0, cp);
		System.arraycopy(p0_genome, cp, s1_genome, cp, gl-cp);
		// Put sons in buffer
		sonsBuffer.add(species.createIndividual(s0_genome));
		sonsBuffer.add(species.createIndividual(s1_genome));
	}
}