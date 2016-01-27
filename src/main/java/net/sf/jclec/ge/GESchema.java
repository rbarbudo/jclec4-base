package net.sf.jclec.ge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.jclec.JCLEC;
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
	
	/** Maximum of depth per tree */
	
	protected int maxDepthSize;
	
	/** Individual array genotype */
	
	protected IIntegerSet [] individualArrayGenotype;
	
	/** Individual constants */
	
	protected IRange [] constants;
	
	/////////////////////////////////////////////////////////////
	// --------------------------------------- Internal variables
	///////////////////////////////////////////////////////////// 
	
	/** Terminal symbols map */
	
	protected transient HashMap<String, TerminalNode> terminalsMap;

	/** Non terminal symbols map */
	
	protected transient HashMap<String, NonTerminalNode[]> nonTerminalsMap;
	
	/** Minimum depth map */

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
	 * Get the maximum depth size
	 * 
	 * @return maximum depth size
	 */
		
	public int getMaxDepthSize() 
	{
		return maxDepthSize;
	}
	
	/**
	 * Set all the terminal symbols for this grammar
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
		System.out.println(minDepthMap);
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
	 * Get the array of terminal nodes
	 * 
	 * @return terminal nodes
	 */
		
	public TerminalNode[] getTerminals() 
	{
		return terminals;
	}
	
	/**
	 * Get the array of nonterminal nodes
	 * 
	 * @return nonterminal nodes
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
		for (TerminalNode termSymbol : terminals) {
			terminalsMap.put(termSymbol.getSymbol(), termSymbol);
		}
	}
	
	/**
	 * Build and set the minimum depth map.
	 */
		
	protected final void setMinDepthMap() 
	{
		minDepthMap = new HashMap<NonTerminalNode, Integer> ();
		
		for (NonTerminalNode nonTermSymbol : nonTerminals)
			minDepthMap.put(nonTermSymbol, -1);
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
	 * @param gumbol which we need to get minimum depth size
	 * 
	 * @return minimum depth size
	 */
		
	public int getMinDepthSize(String symbol) 
	{
		NonTerminalNode [] prodRules = nonTerminalsMap.get(symbol);
		int minDepth = getMinDepthSize(prodRules[0]);
		
		for(NonTerminalNode prodRule: prodRules)
			if((getMinDepthSize(prodRule) < minDepth)&&(getMinDepthSize(prodRule) != -1))
				minDepth = getMinDepthSize(prodRule);
		
		return minDepth;
	}
	
	/**
	 * Sets the minimum depth size for a given symbol
	 * 
	 * @param production which we need to set minimum depth size
	 * @param minDepthSize minimum depth
	 * 
	 */
	
	public void setMinDepthSize(NonTerminalNode production, int minDepthSize)
	{
		System.out.println("he entrado");
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
	 * Get a terminal giving his name.
	 *  
	 * @param symbol Symbol name
	 * 
	 * @return Desired symbol
	 */
	
	public final TerminalNode getTerminal(String symbol)
	{
		return terminalsMap.get(symbol);
	}
	
	/**
	 * Sets the schema for the individual array genotype
	 * 
	 * @param individualArray Schema for the individual genotype
	 */
	
	public final void setIndividualArrayGenotype(IIntegerSet[] individualArray) 
	{
		this.individualArrayGenotype = individualArray;
	}
		
	/**
	 * Gets the schema for the individual array constants
	 * 
	 * @return constants Schema for the individual constants
	 */
	
	public final IRange[] getIndividualConstants() 
	{
		return constants;
	}
	
	/**
	 * Sets the schema for the individual array constants
	 * 
	 * @param constants Schema for the individual constants
	 */
	
	public final void setIndividualConstants(IRange[] constants) 
	{
		this.constants = constants;
	}
	
	/**
	 * Select a production rule for a symbol of the grammar.
	 * 
	 * @param symbol  Symbol to expand
	 * @param genotype Genotype of an individual
	 * @param posGenotype Reading position of the genotype
	 * 
	 * @return A production rule for  the given symbol.
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
	 * Select a production rule for a symbol of the grammar.
	 * 
	 * @param symbol  Symbol to expand
	 * @param genotype Genotype of an individual
	 * @param posGenotype Reading position of the genotype
	 * @param depth Actual depth of the tree
	 * 
	 * @return A production rule for  the given symbol.
	 */	
	
	protected NonTerminalNode selectProductionGrow(String symbol, int [] genotype, int posGenotype, int depth)
	{
		NonTerminalNode [] prodRules = nonTerminalsMap.get(symbol);
		// It keeps indices to the usable rules
		List<Integer> possibleRules = new ArrayList<Integer>();
		int i =0;
				
		for(NonTerminalNode prodRule: prodRules)
		{
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
	 * Select a production rule for a symbol of the grammar.
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
		for(NonTerminalNode rule : prodRules)
		{
			if(depth + getMinDepthSize(rule) < this.maxDepthSize)
			{
				if(!recursiveRules && rule.isRecursive())
				{
					recursiveRules = true;
					possibleRules.clear();
				}
				if(!recursiveRules || (recursiveRules && rule.isRecursive()))
				{
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
	 * Get the minimum depth size for a given symbol
	 * 
	 * @param prodRule for which we need to know the minimum depth size
	 * @param visitedRules Rules which we have already visited
	 * 
	 * @return minimum depth size
	 */
		
	public void calculateMinDepthSize() 
	{
		List <NonTerminalNode> prodRules = new ArrayList<NonTerminalNode>(Arrays.asList(nonTerminals));
		List <NonTerminalNode> rulesDepthOne = new ArrayList<NonTerminalNode>();
		Set<String> symbolsVisited = new HashSet<String>();
		Set<String> auxSymbolsVisited = new HashSet<String>();
		boolean nonTerminalFound = false;
		boolean isCalculable = true;
		int actualDepth = 1;
				
		for(NonTerminalNode prodRule: prodRules)
		{
			for(String symbol: prodRule.getProduction())
				if(!isTerminal(symbol))
					nonTerminalFound = true;
			
			if(nonTerminalFound == false)
			{
				minDepthMap.put(prodRule, 1);
				symbolsVisited.add(prodRule.getSymbol());
				rulesDepthOne.add(prodRule);
			}
			nonTerminalFound = false;
		}
		prodRules.removeAll(rulesDepthOne);
		
		actualDepth++;	
		while(!prodRules.isEmpty())
		{
			for(NonTerminalNode prodRule: prodRules.toArray(new NonTerminalNode [prodRules.size()]))
			{
				for(String symbol: prodRule.getProduction())
					if((!symbolsVisited.contains(symbol))&&(!isTerminal(symbol)))
						isCalculable = false;
				
				if(isCalculable == true)
				{
					minDepthMap.put(prodRule, actualDepth);
					auxSymbolsVisited.add(prodRule.getSymbol());
					prodRules.remove(prodRule);
				}
				isCalculable = true;
			}
			symbolsVisited = auxSymbolsVisited;
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
		// TODO Revisar exahustivamente que realmente funcione bien
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
	 * @param phenotype Phenotype of an individual
	 * @param symbol Symbol to add
	 * @param genotype Genotype to be mapped
	 * @param posGenotype Reading position of the genotype
	 * @param depth Actual depth of the tree
	 */
	
	public int grow(GEIndividual ind, String symbol, int posGenotype, int depth)
	{
		if (isTerminal(symbol)) 
			ind.getPhenotype().addNode(getTerminal(symbol));
		else
		{	
			NonTerminalNode selectedProduction = new NonTerminalNode();
			selectedProduction = selectProductionGrow(symbol, ind.getGenotype(), posGenotype, depth);
			
			// Increment position of genotype going back if it's necessary
			posGenotype++;
			if(posGenotype==ind.getGenotype().length-1)
				posGenotype = 0;
			if (selectedProduction != null){
				ind.getPhenotype().addNode(selectedProduction);
				for(int i=0; i<selectedProduction.getProduction().length; i++)
					posGenotype = grow(ind, selectedProduction.getProduction()[i], posGenotype, depth+1);
			}
			else
			{
				ind.setFeasibility(false);
				return posGenotype;
			}
		}
		return posGenotype;
	}

	/**
	 * Map the phenotype from a given genotype using a full technique
	 * 
	 * @param phenotype Phenotype of an individual
	 * @param symbol Symbol to add
	 * @param genotype Genotype to be mapped
	 * @param posGenotype Reading position of the genotype
	 * @param depth Actual depth of the tree
	 */
	
	public int full(GEIndividual ind, String symbol, int posGenotype, int depth) 
	{
		if (isTerminal(symbol)) 
			ind.getPhenotype().addNode(getTerminal(symbol));
		else
		{	
			NonTerminalNode selectedProduction = new NonTerminalNode();
			selectedProduction = selectProductionFull(symbol, ind.getGenotype(), posGenotype, depth);
			
			// Increment position of genotype going back if it's necessary
			posGenotype++;
			if(posGenotype==ind.getGenotype().length-1)
				posGenotype = 0;
			if (selectedProduction != null){
				ind.getPhenotype().addNode(selectedProduction);
				for(int i=0; i<selectedProduction.getProduction().length; i++)
					posGenotype = full(ind, selectedProduction.getProduction()[i], posGenotype, depth+1);
			}
			else
			{
				ind.setFeasibility(false);
				return posGenotype;
			}
		}
		return posGenotype;
	}
}