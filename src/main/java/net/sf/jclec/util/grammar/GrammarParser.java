package net.sf.jclec.util.grammar;

import java.io.FileNotFoundException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import net.sf.jclec.IConfigure;
import net.sf.jclec.exprtree.IPrimitive;
import net.sf.jclec.symreg.ISimpleSchematized;
import net.sf.jclec.syntaxtree.NonTerminalNode;
import net.sf.jclec.syntaxtree.TerminalNode;
import net.sf.jclec.util.random.IRandGenFactory;
import net.sf.jclec.util.range.IRange;

/**
 * This class parse a grammar file into a set of terminal and non terminal node
 * 
 * @author Rafael Barbudo Lunar
 */

public class GrammarParser 
{	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Empty Constructor
	 */
	
	public GrammarParser() {
		
	}

	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Get the apache commons configuration class from the grammar file
	 * 
	 * @param filename File where the grammar is located
	 * 
	 * @return XML Configuration
	 */
	
	public XMLConfiguration loadXMLFile(String filename) throws FileNotFoundException
	{	
	    XMLConfiguration config = null;
		try {
	        config = new XMLConfiguration(filename);
	    } 		
		catch (ConfigurationException e) {
	        e.printStackTrace();
	    }
		return config;
	}
	
	/**
	 * Get the terminal nodes from the grammar file
	 * 
	 * @param filename File where the grammar is located
	 * 
	 * @return Array with the terminal nodes
	 */
	
	public TerminalNode [] getTerminals(String filename) throws FileNotFoundException
	{
		// Get the apache commons configuration class from the grammar file
		XMLConfiguration settings = loadXMLFile(filename);
		// Get terminal symbols
		int numberOfTermSymbols = settings.getList("terminal-symbols.symbol.name").size();
		TerminalNode [] terminals = new TerminalNode[numberOfTermSymbols]; 
		for (int j=0; j<numberOfTermSymbols; j++) {
			TerminalNode termSymbol = new TerminalNode();
			// Symbol name
			termSymbol.setSymbol(settings.getString("terminal-symbols.symbol("+j+")"+".name"));
			// Set array element
			terminals[j] = termSymbol;
		}
		return terminals;	
	}
	
	/**
	 * Get the non terminal nodes from the grammar file
	 * 
	 * @param filename File where the grammar is located
	 * 
	 * @return Array with the  non terminal nodes
	 */
	
	@SuppressWarnings("unchecked")
	public NonTerminalNode [] getNonTerminals(String filename) throws FileNotFoundException
	{
		// Get the apache commons configuration class from the grammar file
		XMLConfiguration settings = loadXMLFile(filename);
		// Get non-terminal symbols
		int numberOfNonTermSymbols = 
			settings.getList("non-terminal-symbols.symbol.name").size();
		NonTerminalNode [] nonTermSymbols = 
			new NonTerminalNode[numberOfNonTermSymbols];
		for (int j=0; j<numberOfNonTermSymbols; j++) {
			NonTerminalNode nonTermSymbol = new NonTerminalNode();
			// Symbol name
			nonTermSymbol.setSymbol(settings.getString("non-terminal-symbols.symbol("+j+")"+".name"));
			// Symbol production
			nonTermSymbol.setProduction((String [])
				settings.getList("non-terminal-symbols.symbol("+j+")"+".production-rule.element").toArray(new String[0]));				
			// Set array element
			nonTermSymbols[j] = nonTermSymbol;
		}
		return nonTermSymbols;
	}
	
	/**
	 * Get the root node from the grammar file
	 * 
	 * @param filename File where the grammar is located
	 * 
	 * @return Symbol of the the root node
	 */
	
	public String getRootNode(String filename) throws FileNotFoundException
	{
		// Get the apache commons configuration class from the grammar file
		XMLConfiguration settings = loadXMLFile(filename);
		// Get root-symbol
		return settings.getString("root-symbol");
	}
	
	/**
	 * Set the code to a given set of terminal nodes
	 * 
	 * @param filename File where the code is located
	 * @param terminal Set of terminals whihout the code
	 * 
	 * @return Array of terminals with the code
	 */
	
	public TerminalNode [] setTerminalsCode(String filename, TerminalNode [] terminals) throws FileNotFoundException
	{
		// Get the apache commons configuration class from the grammar file
		XMLConfiguration settings = loadXMLFile(filename);
		// Get the number of terminal symbols
		int numberOfTerm = settings.getList("terminal.name").size();
		for(int j=0; j<numberOfTerm; j++){
			try {
				String termSymbolCodeClassname = 
					settings.getString("terminal("+j+")"+".code");

				@SuppressWarnings("unchecked")
				Class<? extends IPrimitive> termSymbolCodeClass = 
					(Class<? extends IPrimitive>) Class.forName(termSymbolCodeClassname);
				IPrimitive code = termSymbolCodeClass.newInstance();
				
				if(code instanceof ISimpleSchematized) {
					((ISimpleSchematized) code).configure(settings.subset("terminal("+j+")"));
				}	
				terminals[j].setCode(code);
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			} 
			catch (InstantiationException e) {
				e.printStackTrace();
			} 
			catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return terminals;
	}	
}