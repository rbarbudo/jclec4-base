package net.sf.jclec.ge;

import org.apache.commons.lang.builder.EqualsBuilder;

import net.sf.jclec.IConstrained;
import net.sf.jclec.IFitness;
import net.sf.jclec.IIndividual;
import net.sf.jclec.base.AbstractIndividual;
import net.sf.jclec.syntaxtree.SyntaxTree;

/**
 * Individual with a byte array as genotype and a SyntaxTree as phenotype
 * 
 * @author Rafael Barbudo Lunar
 */


public class GEIndividual extends AbstractIndividual<int[]> implements IConstrained
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by eclipse */
	
	private static final long serialVersionUID = -7923926819914728004L;
	
	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////
	
	/** Individual phenotype */
	
	private SyntaxTree phenotype = new SyntaxTree();
	
	/** Phenotype validity */
	
	private boolean feasible = true;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
//	/*
//	/**
//	 * Empty constructor
//	 */
//	
//	public GEIndividual() 
//	{
//		super();
//	}

	/**
	 * Constructor that sets individual genotype
	 * 
	 * @param genotype Individual genotype
	 */
	
	public GEIndividual(int[] genotype) 
	{
		super(genotype);
	}

	/**
	 * Constructor that sets individual genotype and phenotype
	 * 
	 * @param genotype Individual genotype
	 * @param phenotype Individual phenotype
	 */
	
	public GEIndividual(int[] genotype, SyntaxTree phenotype) 
	{
		super(genotype);
		setPhenotype(phenotype);
	}
	
	/**
	 * Constructor that sets individual genotype, phenotype and fitness
	 * 
	 * @param genotype Individual genotype
	 * @param phenotype Individual phenotype
	 * @param fitness Individual fitness
	 */
	
	public GEIndividual(int[] genotype, SyntaxTree phenotype, IFitness fitness) 
	{
		super(genotype);
		setPhenotype(phenotype);
		setFitness(fitness);
	}
	
//	/**
//	 * Constructor that sets individual genotype and phenotype
//	 * 
//	 * @param genotype Individual genotype
//	 * @param phenotype Individual phenotype
//	 */
//	
//	public GEIndividual(int[] genotype, SyntaxTree phenotype) 
//	{
//		super(genotype);
//		setPhenotype(phenotype);
//	}
	
//	/**
//	 * Constructor that sets individual genotype, phenotype and fitness
//	 * 
//	 * @param genotype Individual genotype
//	 * @param phenotype Individual phenotype
//	 * @param fitness  Individual fitness
//	 */
//	
//	public GEIndividual(int[] genotype, SyntaxTree phenotype, IFitness fitness) 
//	{
//		super(genotype, fitness);
//		setPhenotype(phenotype);
//	}
	
	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Getter for the individual phenotype
	 * 
	 * @return Individual phenotype
	 */
	
	public SyntaxTree getPhenotype() 
	{
		return phenotype;
	}

	/**
	 * Setter for the individual phenotype
	 * 
	 * @param phenotype The phenotype to set
	 */
	
	public void setPhenotype(SyntaxTree phenotype) 
	{
		this.phenotype = phenotype;
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public IIndividual copy()
	{
		// Genotype length
		int gl = genotype.length;
			
		// Allocate a copy of genotype and phenotype
		int [] gother = new int[gl];
		SyntaxTree pother = new SyntaxTree();
		
		// Copy genotype and phenotype
		System.arraycopy(genotype, 0, gother, 0, gl);
		pother = phenotype.copy();
		
		// Create new individuals, then return it
		if (fitness != null) {
			return new GEIndividual(gother, pother, fitness.copy());			
		}
		else {
			return new GEIndividual(gother, pother);			
		}
	}
	
	/**
	 * Distance between two GEIndividual
	 * 
	 * @param other Individual used to obtein the distance
	 */
	
	public double distance(IIndividual other)
	{
		// TODO Cambiar por lo que corresponda (o dejar Hamming)
		return 0;
	}

	/**
	 * Set if the individual is feasible or not
	 * 
	 * @param feasibility Individual feasibility
	 */
	
	public void setFeasibility(boolean feasibility)
	{
		this.feasible = feasibility;
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public boolean equals(Object other)
	{
		if (other instanceof GEIndividual) {
			GEIndividual iaother = (GEIndividual) other;
			EqualsBuilder eb = new EqualsBuilder();
			eb.append(genotype, iaother.genotype);
			eb.append(phenotype, iaother.phenotype);
			eb.append(fitness, iaother.fitness);
			return eb.isEquals();
		}
		else {
			return false;
		}
	}

	@Override
	public boolean isFeasible() 
	{
		return feasible;
	}

	@Override
	public double degreeOfInfeasibility() 
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
