package net.sf.jclec.util.opt;

import java.util.List;
import java.util.ArrayList;

import net.sf.jclec.IConfigure;
import net.sf.jclec.IIndividual;
import net.sf.jclec.ISystem;
import net.sf.jclec.ISpecies;
import net.sf.jclec.IPopulation;

import net.sf.jclec.util.range.IRange;
import net.sf.jclec.util.range.Interval;

import net.sf.jclec.selector.BettersSelector;

import net.sf.jclec.realarray.RealArraySpecies;
import net.sf.jclec.realarray.RealArrayIndividual;

import org.apache.commons.configuration.Configuration;

/**
 * Nelder-Mead simplex optimizer.
 * 
 * @author Amelia Zafra
 */

public class NelderMeadOptimizer extends AbstractOptimizer implements IConfigure
{
	/////////////////////////////////////////////////////////////////
	// --------------------------------------- Serialization constant
	/////////////////////////////////////////////////////////////////
	
	/** Generated by Eclipse */
	
	private static final long serialVersionUID = 7436334275449929175L;

	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Properties
	/////////////////////////////////////////////////////////////////

	/** Contraction factor */
	
	protected double contractionFactor;
	
	/** Expansion factor */
	
	protected double expansionFactor;
	
	/** Reflection Factor */
	
	protected double reflectionFactor;
	
	/** Number of maximum iterations */
	
	protected int maxIterations;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------- Internal variables
	/////////////////////////////////////////////////////////////////
	
	/** Best Selector */
	
	BettersSelector bettersSelector;

	/** Genotype schema */
	
	protected IRange [] schema;
	
	/////////////////////////////////////////////////////////////////
	// ------------------------------------------------- Constructors
	/////////////////////////////////////////////////////////////////
		
	/**
	 * Empty constructor
	 */
	
	public NelderMeadOptimizer() 
	{
		super();
	}
	
	/////////////////////////////////////////////////////////////////
	// ----------------------------------------------- Public methods
	/////////////////////////////////////////////////////////////////

	// Setting and getting properties

	/** Contraction factor */	
	public double getContractionFactor()
	{
		return contractionFactor;
	}

	public void setContractionFactor(double contractionFactor)
	{
		this.contractionFactor = contractionFactor;
	}
	
	/** Expansion factor */	
	public double getExpansionFactor()
	{
		return expansionFactor;
	}
	
	public void setExpansionFactor(double expansionFactor)
	{
		this.expansionFactor = expansionFactor;
	}
	
	/** Reflection factor */
	public double getReflectionFactor()
	{
		return reflectionFactor;
	}
	
	public void setReflectionFactor(double reflectionFactor)
	{
		this.reflectionFactor = reflectionFactor;
	}
	
	
	/** Maximun iterations */
	public int getMaxIterations()
	{
		return maxIterations;
	}
	
	public void setMaxIterations(int maxIterations)
	{
		this.maxIterations = maxIterations;
	}
	
	// IConfigure interface
	
	/**
	 * {@inheritDoc} 
	 */
	
	public void configure(Configuration configuration)
	{
		double contractionFactor = configuration.getDouble("contraction-factor",0.5);
		setContractionFactor(contractionFactor);
		
		double reflectionFactor = configuration.getDouble("reflection-factor",1.0);
		setReflectionFactor(reflectionFactor);
		
		double expansionFactor = configuration.getDouble("expansion-factor",2.0);
		setExpansionFactor(expansionFactor);
		
		int maxIterations = configuration.getInt("max-iterations",10);
		setMaxIterations(maxIterations);
		
					
	}	
	
	// IOptimizer interface
			
	/**
	 * {@inheritDoc} 
	 */
	
	@SuppressWarnings("unchecked")
	public void contextualize(ISystem context)
	{
		super.contextualize(context);
		
		ISpecies spc = ((IPopulation) context).getSpecies();
		// Get context species
		if (spc instanceof RealArraySpecies) {
			schema = ((RealArraySpecies) spc).getGenotypeSchema();
		}
		else {
			throw new IllegalArgumentException("IRealArraySpecies expected");
		}
		bettersSelector = new BettersSelector(context);
	}
	
	
	/////////////////////////////////////////////////////////////////
	// -------------------------------------------- Protected methods
	/////////////////////////////////////////////////////////////////	
	
	protected void restart(List<IIndividual> soluciones, IIndividual mejorSolucion, int longitud)
	{
		int elem = soluciones.size();		
		for(int i=0; i<elem; i++) {			
			if(soluciones.get(i).equals(mejorSolucion)){
				for(int j=0; j<longitud; j++)
					soluciones.get(i).getGenotype()[j] = 0.5*(soluciones.get(i).getGenotype()[j] + mejorSolucion.getGenotype()[j]);
			}	
		}
		evaluator.evaluate(soluciones);
	}
	
	
	protected void move(IIndividual actual, IIndividual indRespecto, int longitud, double factor, double [] valorCentroide){
		
		double  a, b;
		double [] realArray = new double[longitud];
		double extremoSuperior, extremoInferior;
		
	
		for(int i=0; i<longitud; i++){

			//Use the factor and the centroide to change the solution 
			realArray[i] =  valorCentroide[i] + factor*(valorCentroide[i]- indRespecto.getGenotype()[i]);
			
			//Check the limits of the interval
			a = ((Interval) schema[i]).getLeft();
			b = ((Interval) schema[i]).getRight();
	
			extremoInferior = Math.min(a,b);
			extremoSuperior = Math.max(a,b);
			
			if(realArray[i] < extremoInferior)
				realArray[i] = extremoInferior;
			if(realArray[i] > extremoSuperior)
				realArray[i] = extremoSuperior;
		}
		
		actual.setGenotype(realArray);
	}
	
	protected List<IIndividual> obtainExtremeSolutions(List<IIndividual> source, int longitud){
		
		List<IIndividual> result = new ArrayList<IIndividual>();
	
		List<IIndividual> aux = bettersSelector.select(source, source.size());
		
		//Add the worst individual
		result.add(aux.get(source.size()-1));
		//Add the second worst individual
		result.add(aux.get(source.size()-2));
		//Add the best individual
		result.add(aux.get(0));
		
		
		return result;
	}
	
	
	protected void calculateCentroide(List<IIndividual> soluciones, IIndividual worst, double [] valorCentroide, int longitud){
		
		for(int i=0; i<longitud; i++)
			valorCentroide[i] = 0.0;
		
		for(int j=0; j<longitud; j++)
		{
			for(int i=0; i<soluciones.size(); i++){
				if(!soluciones.get(i).equals(worst))
					valorCentroide[j] += (soluciones.get(i)).getGenotype()[j];
			}
		}
		
		for(int i=0; i<longitud; i++)
		{	
			valorCentroide[i] /= longitud;
		}
		
	}
	
 
	@SuppressWarnings("unchecked")
	protected List<IIndividual> generateSolutions(IIndividual solucionInicial, int longitud){
		
		List<IIndividual> result = new ArrayList<IIndividual>();
		double a,b, dif;
		double extremoSuperior, extremoInferior;
		
	
		IIndividual copiaSolucionInicial = (IIndividual) solucionInicial.copy();
		result.add(copiaSolucionInicial);
		double [] realArray = new double[longitud];
		
		for(int i=0; i<longitud; i++){
			
			IIndividual nuevaSolucion = (IIndividual) solucionInicial.copy();
			realArray = nuevaSolucion.getGenotype();

			//Assign new values to the solution
			a = ((Interval) schema[i]).getLeft();
			b = ((Interval) schema[i]).getRight();
			
			
			dif = Math.abs(a-b);
			
			realArray[i] += 0.1*dif;
			
			// Check the limits of the intervals
			extremoInferior = Math.min(a,b);
			extremoSuperior = Math.max(a,b);
			
			if(realArray[i] < extremoInferior)
				realArray[i] = extremoInferior;
			if(realArray[i] > extremoSuperior)
				realArray[i] = extremoSuperior;
			
			nuevaSolucion.setGenotype(realArray);
			
			result.add(nuevaSolucion);	
		}
		evaluator.evaluate(result);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public IIndividual optimize(IIndividual ind) 
	{
		
		List<IIndividual> solutions;
		//The first solution is the worst the solution, the second is the
		//second worst solution and the third is the best solution
		List<IIndividual> extremeSolutions;
		List<IIndividual> lextension = new ArrayList<IIndividual>();
		List<IIndividual> lreflection = new ArrayList<IIndividual>();
		List<IIndividual> lcontraction = new ArrayList<IIndividual>();
		IIndividual extension = (IIndividual) ind.copy();
		IIndividual reflection = (IIndividual) ind.copy();
		IIndividual contraction = (IIndividual) ind.copy();
		lextension.add(extension);
		lreflection.add(reflection);
		lcontraction.add(contraction);
		
		//Obtain the lenght of the individuals
		int length = ind.getGenotype().length;
		double [] centroideValue = new double[length];

		for(int i=0; i<length; i++)
		{
			centroideValue[i] = 0.0;
		}
		
		//Generate new solutions
		solutions = generateSolutions(ind, length);
		
		extremeSolutions = obtainExtremeSolutions(solutions, length);
		
		// 0 - It is the worst solution
		// 1 - It is the second worst solution
		// 2 - It is the best solution
		
		for(int i=0; i<maxIterations; i++)
		{
			extremeSolutions = obtainExtremeSolutions(solutions, length);
		
			calculateCentroide(solutions, extremeSolutions.get(0), centroideValue, length);
		
			
			//Change the solution by means of reflection process
			move(reflection, extremeSolutions.get(0), length, reflectionFactor, centroideValue);
			evaluator.evaluate(lreflection);
			
			//If the new solution is better than the best solution, Apply expansion process
			if(evaluator.getComparator().compare(extremeSolutions.get(2).getFitness(),reflection.getFitness()) == -1)
			{
				
				move(extension, reflection, length, expansionFactor, centroideValue);
				
				evaluator.evaluate(lextension);
				
				if(evaluator.getComparator().compare(reflection.getFitness(),extension.getFitness()) == -1){
					extremeSolutions.get(0).setGenotype(extension.getGenotype());
					extremeSolutions.get(0).setFitness(extension.getFitness());
					
				}
				else
				{
					extremeSolutions.get(0).setGenotype( reflection.getGenotype());
					extremeSolutions.get(0).setFitness( reflection.getFitness());
				
				}
				
			}
			//If the new solutions is better than the second worst solution, add the new solution
			else if(evaluator.getComparator().compare(reflection.getFitness(),extremeSolutions.get(1).getFitness()) == 1){
				
				extremeSolutions.get(0).setGenotype(reflection.getGenotype());
				extremeSolutions.get(0).setFitness(reflection.getFitness());
			}
			
			else
			{
				//If the new solution is better than the worst solution, add the new solution
				if(evaluator.getComparator().compare(reflection.getFitness(),extremeSolutions.get(0).getFitness()) == 1){
					extremeSolutions.get(0).setGenotype( reflection.getGenotype());
					extremeSolutions.get(0).setFitness( reflection.getFitness());
				
				}
				else
				{
					//Apply contraction process
					move(contraction, extremeSolutions.get(0), length, contractionFactor, centroideValue);
					evaluator.evaluate(lcontraction);
				
					//If the new solution is better than the worst solution, add the new solution 
					if(evaluator.getComparator().compare(contraction.getFitness(),extremeSolutions.get(0).getFitness()) == 1)
					{
						extremeSolutions.get(0).setGenotype( contraction.getGenotype());
						extremeSolutions.get(0).setFitness( contraction.getFitness());
					
					}
					else
						// Restart solutions
						restart(solutions, extremeSolutions.get(0), length);
				}
					
			}			
			//
			for(int j=0; j<length; j++)	centroideValue[j] = 0.0;					
		}
		
		extremeSolutions = obtainExtremeSolutions(solutions, length);				
			
		return extremeSolutions.get(2);
	}	
}

