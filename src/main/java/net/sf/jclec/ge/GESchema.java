package net.sf.jclec.ge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.jclec.IPopulation;
import net.sf.jclec.JCLEC;
import net.sf.jclec.symreg.Cte;
import net.sf.jclec.syntaxtree.NonTerminalNode;
import net.sf.jclec.syntaxtree.TerminalNode;
import net.sf.jclec.util.intset.IIntegerSet;
import net.sf.jclec.util.range.IRange;

/**
 * Schema for GEIndividual and its subclasses.
 * 
 * @author Rafael Barbudo Lunar
 */

public class GESchema implements JCLEC 
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////

	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 3206660269350387834L;

	/////////////////////////////////////////////////////////////////
	// --------------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////
	
	/** All terminals */
	
	protected TerminalNode [] terminals;

	/** All non terminals */
	
	protected NonTerminalNode [] nonTerminals;

	/** Root symbol name */
	
	protected String rootSymbol;
	
	/** Maximum depth tree */
	
	protected int maxDepthSize;
	
	/** Individual genotype schema */
	
	protected IIntegerSet [] genotypeSchema;
	
	/** Individual constants */
	
	protected IRange [] constants;
	
	/////////////////////////////////////////////////////////////
	// --------------------------------------- Internal variables
	///////////////////////////////////////////////////////////// 
	
	/** Terminal symbols map */
	
	protected transient HashMap<String, TerminalNode> terminalsMap;

	/** Non terminal symbols map */
	
	protected transient HashMap<String, NonTerminalNode[]> nonTerminalsMap;
	
	/** Minimum depth map for each production rule */

	protected transient HashMap<NonTerminalNode, Integer> minDepthMap;
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------------- Constructor
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty constructor
	 */
	
	public GESchema() 
	{
		super();
	}
	
	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Sets the maximum depth of the tree
	 * 
	 * @param maxDepthSize
	 */

	public void setMaxDepthSize(int maxDepthSize) 
	{
		this.maxDepthSize = maxDepthSize;
	}
	
	/**
	 * Gets the maximum depth size of the tree
	 * 
	 * @return maximum depth size
	 */
		
	public int getMaxDepthSize() 
	{
		return maxDepthSize;
	}
	
	/**
	 * Sets all the terminal symbols for this grammar
	 * 
	 * @param terminals Terminals set
	 */
	
	public void setTerminals(TerminalNode[] terminals) 
	{
		// Set terminal symbols
		this.terminals = terminals;
		// Set terminal symbols map
		setTerminalsMap();
	}

	/**
	 * Set all the non terminal symbols for this grammar
	 * 
	 * @param nonTerminals Non terminal symbols
	 */
	
	public void setNonTerminals(NonTerminalNode[] nonTerminals) 
	{
		// Set non-terminal symbols
		this.nonTerminals = nonTerminals;
		// Set non-terminal symbols map
		setNonTerminalsMap();
		// Set minDepthSize map
		setMinDepthMap();
	}

	/**
	 * Set the start symbol for this grammar
	 * 
	 * @param rootSymbol Start symbol
	 */
	
	public void setRootSymbol(String rootSymbol) 
	{
		this.rootSymbol = rootSymbol;
	}

	/**
	 * Get the root symbol
	 * 
	 * @return root symbol
	 */

	public String getRootSymbol() 
	{
		return rootSymbol;
	}
	
	/**
	 * Gets the terminal nodes
	 * 
	 * @return terminals The terminal nodes
	 */
		
	public TerminalNode[] getTerminals() 
	{
		return terminals;
	}
	
	/**
	 * Gets the array of non-terminal nodes
	 * 
	 * @return nonterminal The non terminal nodes
	 */
		
	public NonTerminalNode[] getNonTerminals() 
	{
		return nonTerminals;
	}
	
	/**
	 * Checks if a symbol is terminal
	 * 
	 * @param symbol the symbol
	 * 
	 * @return true or false
	 */
		
	public boolean isTerminal(String symbol)
	{
		return terminalsMap.containsKey(symbol);
	}
	
	/**
	 * Build and set the terminals map.
	 */
		
	protected final void setTerminalsMap() 
	{
		terminalsMap = new HashMap<String, TerminalNode> ();
		for (TerminalNode termSymbol : terminals)
			terminalsMap.put(termSymbol.getSymbol(), termSymbol);
	}
	
	/**
	 * Build and set the minimum depth map.
	 */
		
	protected final void setMinDepthMap() 
	{
		minDepthMap = new HashMap<NonTerminalNode, Integer> ();
		// Initialize the min-depth-map with an invalid value
		for (NonTerminalNode nonTermSymbol : nonTerminals)
			minDepthMap.put(nonTermSymbol, -1);
		// Calculate the values
		calculateMinDepthSize();
	}
	
	/**
	 * Gets the minimum depth size for a given production
	 * 
	 * @param production which we need to get minimum depth size
	 * 
	 * @return minimum depth size
	 */
		
	public int getMinDepthSize(NonTerminalNode production) 
	{
		return minDepthMap.get(production);
	}	
	
	/**
	 * Gets the minimum depth size for a given symbol
	 * 
	 * @param symbol which we need to get minimum depth size
	 * 
	 * @return minimum depth size
	 */
		
	public int getMinDepthSize(String symbol) 
	{
		// Get all the production rules of the symbol
		NonTerminalNode [] prodRules = nonTerminalsMap.get(symbol);
		int minDepth = getMinDepthSize(prodRules[0]);
		int actualDepth;
		
		// See which production has the minimun depth
		for(NonTerminalNode prodRule: prodRules) {
			actualDepth = getMinDepthSize(prodRule);
			if((actualDepth < minDepth)&&(actualDepth != -1))
				minDepth = getMinDepthSize(prodRule);
		}
		return minDepth;
	}
	
	/**
	 * Sets the minimum depth size for a given production
	 * 
	 * @param production which we need to set minimum depth size
	 * @param minDepthSize minimum depth
	 */
	
	public void setMinDepthSize(NonTerminalNode production, int minDepthSize)
	{
		minDepthMap.put(production, minDepthSize);
	}
	
	/**
	 * Build and set the non terminals map.
	 */

	protected final void setNonTerminalsMap() 
	{
		// Used to classify symbols
		HashMap<String, List<NonTerminalNode>> auxMap = 
			new HashMap<String, List<NonTerminalNode>> ();
		// Classify non-term symbols
		for (NonTerminalNode nonTermSymbol : nonTerminals) {
			String nonTermSymbolName = nonTermSymbol.getSymbol();
			if (auxMap.containsKey(nonTermSymbolName)) {
				auxMap.get(nonTermSymbolName).add(nonTermSymbol);
			}
			else {
				ArrayList<NonTerminalNode> list = 
					new ArrayList<NonTerminalNode>();
				list.add(nonTermSymbol);
				auxMap.put(nonTermSymbolName, list);
			}
		}			
		// Create non-term symbols map
		nonTerminalsMap = new HashMap<String, NonTerminalNode[]> ();
		for (String nonTermName : auxMap.keySet()) {
			// Get symbols list
			List<NonTerminalNode> list = auxMap.get(nonTermName);
			// Convert list to array
			NonTerminalNode [] array = 
				list.toArray(new NonTerminalNode[list.size()]);
			// Put array in non terminals map
			nonTerminalsMap.put(nonTermName, array);
		}
	}
	
	/**
	 * Gets a terminal from its symbol name.
	 *  
	 * @param symbol Symbol name
	 * 
	 * @return The desired terminal
	 */
	
	public final TerminalNode getTerminal(String symbol)
	{
		return terminalsMap.get(symbol);
	}
	
	/**
	 * Sets the genotype schema
	 * 
	 * @param genotypeSchema Genotype schema for the individual
	 */
	
	public final void setGenotypeSchema(IIntegerSet[] genotypeSchema) 
	{
		this.genotypeSchema = genotypeSchema;
	}
		
	/**
	 * Gets the constant schema 
	 * 
	 * @return The constant schema
	 */
	
	public final IRange[] getConstantSchema() 
	{
		return constants;
	}
	
	/**
	 * Sets the constants schema 
	 * 
	 * @param constants The constants schema 
	 */
	
	public final void setConstantSchema(IRange[] constants) 
	{
		this.constants = constants;
	}
	
	/**
	 * Select a production rule for a symbol of the grammar.
	 * 
	 * @param symbol Symbol to expand
	 * @param genotype Genotype of an individual
	 * @param posGenotype Reading position of the genotype
	 * 
	 * @return A production rule for the given symbol.
	 */	
	
	protected NonTerminalNode selectProduction(String symbol, int [] genotype, int posGenotype)
	{			
		NonTerminalNode [] prodRules = nonTerminalsMap.get(symbol);
		// Number of productions
		int nOfProdRules = prodRules.length;
		// Choose production based on the genotype
		int chosen = genotype[posGenotype] % nOfProdRules;
		// Return production chosen
		return prodRules[chosen];	
	}
	
	/**
	 * Select a production rule for a symbol of the grammar for a grow mapping.
	 * 
	 * @param symbol  Symbol to expand
	 * @param genotype Genotype of an individual
	 * @param posGenotype Reading position of the genotype
	 * @param depth Actual depth of the tree
	 * 
	 * @return A production rule for the given symbol.
	 */	
	
	protected NonTerminalNode selectProductionGrow(String symbol, int [] genotype, int posGenotype, int depth)
	{
		NonTerminalNode [] prodRules = nonTerminalsMap.get(symbol);
		// It keeps indices to the usable rules
		List<Integer> possibleRules = new ArrayList<Integer>();
		int i =0;
				
		for(NonTerminalNode prodRule: prodRules) {
			if(depth + getMinDepthSize(prodRule) < this.maxDepthSize)
				possibleRules.add(i);
			i++;
		}
			
		if(possibleRules.isEmpty())
			return null;

		// Number of productions
		int nOfProdRules = possibleRules.size();
		// Choose production based on the genotype
		int chosen = possibleRules.get(genotype[posGenotype] % nOfProdRules);
		// Return production chosen
		return prodRules[chosen];	
	}
	
	/**
	 * Select a production rule for a symbol of the grammar for a full mapping.
	 * 
	 * @param symbol  Symbol to expand
	 * @param genotype Genotype of an individual
	 * @param posGenotype Reading position of the genotype
	 * @param depth Actual depth of the tree
	 * 
	 * @return A production rule for  the given symbol.
	 */	
	
	protected NonTerminalNode selectProductionFull(String symbol, int [] genotype, int posGenotype, int depth)
	{			
		NonTerminalNode [] prodRules = nonTerminalsMap.get(symbol);
		// It keeps indices to the usable rules
		List<Integer> possibleRules = new ArrayList<Integer>();
		int i =0;
		// Variable used to control if we are using recursiveProductions or not
	    boolean recursiveRules = false;
		
	    // Iterate through the different rule productions
		for(NonTerminalNode rule : prodRules) {
			if(depth + getMinDepthSize(rule) < this.maxDepthSize) {
				if(!recursiveRules && rule.isRecursive()) {
					recursiveRules = true;
					possibleRules.clear();
				}
				if(!recursiveRules || (recursiveRules && rule.isRecursive())) {
					possibleRules.add(i);
				}
			}
			i++;
		}
		
		// We control if there is not any production rule
		if(possibleRules.isEmpty())
			return null;
		
		// Number of productions
		int nOfProdRules = possibleRules.size();
		// Choose production based on the genotype
		int chosen = possibleRules.get(genotype[posGenotype] % nOfProdRules);
		// Return production chosen
		return prodRules[chosen];			
	}
	
	/**
	 * Calculate the minimum depth size for all the productions
	 */
		
	public void calculateMinDepthSize() 
	{
		// List with all the production rules with no depth; at the end it has to be empty
		List <NonTerminalNode> prodRules = new ArrayList<NonTerminalNode>(Arrays.asList(nonTerminals));
		// List with all the production rules with 1 depth
		List <NonTerminalNode> rulesDepthOne = new ArrayList<NonTerminalNode>();
		// Set of symbols that at least one production rule's depth has been calculated
		Set<String> symbolsVisited = new HashSet<String>();
		// Actual depth (iterate)
		int actualDepth = 1;
		// Boolean used to control the loops
		boolean nonTerminalFound = false;
		boolean isCalculable = true;
		
		// Get the production with 1 as min derivation depth
		for(NonTerminalNode prodRule: prodRules) {
			for(String symbol: prodRule.getProduction())
				if(!isTerminal(symbol))
					nonTerminalFound = true;
			
			if(nonTerminalFound == false) {
				minDepthMap.put(prodRule, actualDepth);
				symbolsVisited.add(prodRule.getSymbol());
				rulesDepthOne.add(prodRule);
			}
			nonTerminalFound = false;
		}
		
		// Remove the production rules with 1 as depth size
		prodRules.removeAll(rulesDepthOne);
		// Increase the actual depth
		actualDepth++;
		
		// Get the depth for the other productions
		while(!prodRules.isEmpty()) {
			for(NonTerminalNode prodRule: prodRules.toArray(new NonTerminalNode [prodRules.size()])) {
				for(String symbol: prodRule.getProduction())
					if((!symbolsVisited.contains(symbol))&&(!isTerminal(symbol)))
						isCalculable = false;

				// Check if we can assign the depth to the production
				if(isCalculable == true) {
					minDepthMap.put(prodRule, actualDepth);
					symbolsVisited.add(prodRule.getSymbol());
					prodRules.remove(prodRule);
				}
				isCalculable = true;
			}
			// Increase the actual depth
			actualDepth++;
		}
	}
	
	/**
	 * Get the point of derivation dissimilarity for two given genotypes.
	 * 
	 * @param p0genotype Symbol to expand
	 * @param p2genotype Genotype of an individual
	 * 
	 * @return The point of dissimilarity of the genotypes.
	 */	
	
	public int getPointOfDerivationDissimilarity(int[] p0genotype, int[] p1genotype) 
	{
		int genotypeMinLength = Math.min(p0genotype.length, p1genotype.length);
		//The first element indicate if the solution is reached and the second the reading position
		int [] posGenotype = {0,0};
		searchDerivationDissimilarity(p0genotype, p1genotype, posGenotype, genotypeMinLength, rootSymbol);
		return posGenotype[1];
	}

	/**
	 * Search for a possible derivation dissimilarity in a given possition of the phenotype.
	 * 
	 * @param p0genotype  Symbol to expand
	 * @param p2genotype Genotype of an individual
	 * @param posGenotype Reading position of the genotype and flag to control if solution is reached
	 * @param endPosition Final of the genotype
	 * @param symbol Symbol to derivate and check for dissimilarity
	 * 
	 * @return The point of dissimilarity of the genotypes.
	 */	
	
	public void searchDerivationDissimilarity(int[] p0genotype, int[] p1genotype, int [] posGenotype, int endPosition, String symbol) 
	{
		NonTerminalNode prod0 = new NonTerminalNode();
		NonTerminalNode prod1 = new NonTerminalNode();
		
		// End of genotype is reached without a solution
		if(posGenotype[1] == endPosition)
			posGenotype[0] = -1;
		// Solution or end position is not reached yet
		if(posGenotype[0] == 0){	
			if (isTerminal(symbol) == false) {
				// Select a production rule for each genotype
				prod0 = selectProduction(symbol, p0genotype, posGenotype[1]);
				prod1 = selectProduction(symbol, p1genotype, posGenotype[1]);
				posGenotype[1] = posGenotype[1] + 1;
				if(prod0.equals(prod1))
					for(int i=0; i<prod0.getProduction().length; i++)
						searchDerivationDissimilarity(p0genotype, p1genotype, posGenotype, endPosition, prod0.getProduction()[i]);
				// Dissimilarity is found (posGenotype[1] is the first point where the derivation change)
				else
					posGenotype[0] = 1;
			}
		}
	}

	/**
	 * Map the phenotype from a given genotype using a grow technique
	 * 
	 * @param ind The individual
	 * @param symbol The symbol we can add
	 * @param posGenotype The reading position of the genotype
	 * @param depth The actual depth of the genotype
	 * @param context The system context
	 * 
	 * @return The actual reading position of the genotype
	 */
	
	public int grow(GEIndividual ind, String symbol, int posGenotype, int depth, IPopulation context)
	{
		if (isTerminal(symbol)) {
			ind.getPhenotype().addNode(getTerminal(symbol));
		}
		else {	
			NonTerminalNode selectedProduction = new NonTerminalNode();
			selectedProduction = selectProductionGrow(symbol, ind.getGenotype(), posGenotype, depth);
			
			// Increment position of genotype going back if it's necessary
			posGenotype++;
			if(posGenotype == ind.getGenotype().length-1)
				posGenotype = 0;
			if (selectedProduction != null){
				ind.getPhenotype().addNode(selectedProduction);
				for(int i=0; i<selectedProduction.getProduction().length; i++)
					posGenotype = grow(ind, selectedProduction.getProduction()[i], posGenotype, depth+1, context);
			}
			else {
				ind.setFeasibility(false);
				return posGenotype;
			}
		}
		return posGenotype;
	}

	/**
	 * Map the phenotype from a given genotype using a full technique
	 * 
	 * @param ind The individual
	 * @param symbol The symbol we can add
	 * @param posGenotype The reading position of the genotype
	 * @param depth The actual depth of the genotype
	 * @param context The system context
	 * 
	 * @return The actual reading position of the genotype
	 */
	
	public int full(GEIndividual ind, String symbol, int posGenotype, int depth, IPopulation context) 
	{
		if (isTerminal(symbol)) {
			ind.getPhenotype().addNode(getTerminal(symbol));
		}
		else {	
			NonTerminalNode selectedProduction = new NonTerminalNode();
			selectedProduction = selectProductionFull(symbol, ind.getGenotype(), posGenotype, depth);
			
			// Increment position of genotype going back if it's necessary
			posGenotype++;
			if(posGenotype==ind.getGenotype().length-1)
				posGenotype = 0;
			if (selectedProduction != null){
				ind.getPhenotype().addNode(selectedProduction);
				for(int i=0; i<selectedProduction.getProduction().length; i++)
					posGenotype = full(ind, selectedProduction.getProduction()[i], posGenotype, depth+1, context);
			}
			else {
				ind.setFeasibility(false);
				return posGenotype;
			}
		}
		return posGenotype;
	}
}