/**
 * 
 */
package net.sf.jclec.ge.mut;

import net.sf.jclec.ge.GEIndividual;
import net.sf.jclec.ge.GEMutator;

/**
 * @author Rafael Barbudo Lunar
 *
 */
public class OneLocusMutator extends GEMutator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 310735562418365281L;

	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor
	 */
	
	public OneLocusMutator() 
	{
		super();
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// AbstractMutator methods

	/**
	 * {@inheritDoc}
	 */
	
	@Override
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
		// Returns mutant
		sonsBuffer.add(species.createIndividual(mgenome));
	}

	// java.lang.Object methods

	public boolean equals(Object other)
	{
		if (other instanceof OneLocusMutator) {
			return true;
		}
		else {
			return false;
		}
	}	

}
