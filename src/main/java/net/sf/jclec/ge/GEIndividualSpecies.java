package net.sf.jclec.ge;

import java.io.FileNotFoundException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import net.sf.jclec.IConfigure;
import net.sf.jclec.syntaxtree.NonTerminalNode;
import net.sf.jclec.syntaxtree.TerminalNode;
import net.sf.jclec.util.grammar.GrammarParser;
import net.sf.jclec.util.intset.IIntegerSet;
import net.sf.jclec.util.range.IRange;

/**
 * Species for GEIndividual
 * 
 * @author Rafael Barbudo Lunar
 */

public class GEIndividualSpecies extends GESpecies implements IConfigure 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by Eclipse */
	
	private static final long serialVersionUID = -4236846822314017860L;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor
	 */
	
	public GEIndividualSpecies() 
	{
		super();
		genotypeSchema = new GESchema();
	}

	/**
	 * Constructor that sets genotype schema.
	 * 
	 * @param genotypeSchema
	 */
	
	public GEIndividualSpecies(GESchema genotypeSchema) 
	{
		super();
		setGenotypeSchema(genotypeSchema);	
	}
	
	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// Setting properties
	
	/**
	 * Set the name of the root symbol 
	 * 
	 * @param rootSymbolName Root symbol name
	 */
	
	public void setRootSymbol(String rootSymbolName)
	{
		genotypeSchema.setRootSymbol(rootSymbolName);
	}
		
	/**
	 * Set terminal symbols for this grammar.
	 * 
	 * @param terminals Terminal symbols
	 */
		
	public void setTerminals(TerminalNode [] terminals)
	{
		genotypeSchema.setTerminals(terminals);
	}
		
	/**
	 * Set non-terminal symbols for this grammar.
	 * 
	 * @param nonTerminals Nonterminal symbols
	 */
		
	public void setNonTerminals(NonTerminalNode [] nonTerminals)
	{
		genotypeSchema.setNonTerminals(nonTerminals);
	}
	
	/**
	 * Set the individual array.
	 * 
	 * @param individualArray Individual array schema
	 */
	
	public void setIndividualArrayGenotype(IIntegerSet[] individualArray) 
	{
		genotypeSchema.setIndividualArrayGenotype(individualArray);
	}
	
	/**
	 * Set the individual constant array.
	 * 
	 * @param constants Individual array schema
	 */
	
	public void setIndividualConstant(IRange[] constants) 
	{
		genotypeSchema.setIndividualConstants(constants);
	}
	
	/**
	 * Set genotype schema
	 * 
	 * @param genotypeSchema New genotype schema
	 */
	
	public void setGenotypeSchema(GESchema genotypeSchema)
	{
		this.genotypeSchema = genotypeSchema;
	}
	
	/**
	 * Set the maximum depth size for this schema.
	 * 
	 * @param maxDpthSize Maximum depth
	 */
	
	private void setMaxDepthSize(int maxDepthSize) 
	{
		genotypeSchema.setMaxDepthSize(maxDepthSize);	
	}
	
	// Configuration and creation methods
	
	/**
	 * {@inheritDoc}
	 */

	@Override
	public GEIndividual createIndividual(int[] genotype) 
	{	
		GEIndividual newIndividual = null;
		
		if(provider instanceof GECreator)
			newIndividual = ((GECreator)provider).createIndividual(genotype);
		else
			throw new RuntimeException("Provider must be an GECreator subclass instance");
			
		return newIndividual;
	}
	
	/**
	 * Configuration method.
	 * 
	 * @param settings Set of parameters needed to configure the species
	 */
	
	public void configure(Configuration settings) 
	{
		GrammarParser gp = new GrammarParser();
		TerminalNode [] terminals;
		
		// Try to get the terminals, non terminals and root nodes
		try {
			
			// Get the file where the grammar is located
			String bnfFile = settings.getString("grammar-file");
			// Get the file where the code of the terminal nodes is located
			String codeFile = settings.getString("code-file");
			
			// Get all the terminals node
			terminals = gp.getTerminals(bnfFile);
			// Set the code to the list of terminals and set them to the schema
			terminals = gp.setTerminalsCode(codeFile, terminals);
			setTerminals(terminals);
			// Get the non terminal nodes and set to the genotype
			setNonTerminals(gp.getNonTerminals(bnfFile));
			// Get the root symbol and set to the genotype
			setRootSymbol(gp.getRootNode(bnfFile));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		// Constants length
		int constantLength = settings.getList("constant-schema.locus[@type]").size();
		// Constants schema
		IRange [] constants = new IRange[constantLength];
		// Set constants schema components
		for(int i=0; i<constantLength; i++) {
			// Get component classname
			String componentClassname = 
					settings.getString("constant-schema.locus("+i+")[@type]");
			try {
				Class<?> componentClass = 
						Class.forName(componentClassname);
				// Set schema component
				constants[i] = 
						(IRange) componentClass.newInstance();
				// Configure component
				if (constants[i] instanceof IConfigure) {
					((IConfigure) constants[i]).configure
						(settings.subset("constant-schema.locus("+i+")"));
				}
			}
			catch(ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(0);
			}
			catch(IllegalAccessException e) {
				e.printStackTrace();
				System.exit(0);
			}
			catch(InstantiationException e) {
				e.printStackTrace();
				System.exit(0);
			}			
		}
		// Assign constants schema
		setIndividualConstant(constants);
		
		// Genotype lenght
		int genotypeLength = settings.getList("genotype-schema.locus[@type]").size();
		// Genotype schema
		IIntegerSet [] individualArray = new IIntegerSet[genotypeLength];
		// Set genotype schema components
		for (int i=0; i<genotypeLength; i++) {
			// Get component classname
			String componentClassname = 
					settings.getString("genotype-schema.locus("+i+")[@type]");
			try {
				Class<?> componentClass = 
						Class.forName(componentClassname);
				// Set schema component
				individualArray[i] = 
						(IIntegerSet) componentClass.newInstance();
				// Configure component
				if (individualArray[i] instanceof IConfigure) {
					((IConfigure) individualArray[i]).configure
						(settings.subset("genotype-schema.locus("+i+")"));
				}
			}
			
			catch(ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(0);
			}
			catch(IllegalAccessException e) {
				e.printStackTrace();
				System.exit(0);
			}
			catch(InstantiationException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		// Assign genotype schema
		setIndividualArrayGenotype(individualArray);
		
		// Get and set max-tree-depth
		int maxDepthSize = settings.getInt("max-depth-size");
		setMaxDepthSize(maxDepthSize);
	}

	/**
	 * {@inheritDoc}
	 */
	
	public String toString()
	{
		// Performs Schema rendering
		ToStringBuilder tsb = new ToStringBuilder(this);
		// Append schema
		tsb.append("schema", genotypeSchema);
		// Returns rendered schema
		return tsb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	public boolean equals(Object other)
	{
		if (other instanceof GEIndividualSpecies) {
			EqualsBuilder eb = new EqualsBuilder();
			GEIndividualSpecies iaoth = (GEIndividualSpecies) other;
			eb.append(this.genotypeSchema, iaoth.genotypeSchema);
			return eb.isEquals();
		}
		else
			return false;
	}	
}