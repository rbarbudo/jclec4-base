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
	
	protected GESchema schema;
	
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
	 * Gets the schema
	 * 
	 * @return This schema
	 */

	public GESchema getSchema()
	{
		return schema;
	}

	/**
	 * Sets the schema
	 * 
	 * @param schema The new schema
	 */
	
	public void setSchema(GESchema schema)
	{
		this.schema = schema;
	}
	
	/**
	 * Informs about individual genotype length.
	 * 
	 * @return Individual genotype length
	 */
	
	public int getGenotypeLength()
	{
		return schema.genotypeSchema.length;
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