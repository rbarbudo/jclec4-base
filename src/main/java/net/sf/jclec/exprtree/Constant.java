/**
 * 
 */
package net.sf.jclec.exprtree;

import net.sf.jclec.exprtree.fun.AbstractPrimitive;
import net.sf.jclec.exprtree.fun.ExprTreeFunction;

/**
 * @author Rafael Barbudo Lunar
 *
 */

public class Constant extends AbstractPrimitive 
{
	private static final long serialVersionUID = -4349262554064087860L;

	private Double value = null;
	
	public Constant() 
	{
		super(new Class<?> [] {Double.class, Double.class}, Double.class);
		int max = 10;
		int min = -10;
				
		double rand = (double)(Math.random() * (max - min) + min);
		setValue(rand);
		
		//System.out.println("value:"+getValue());
		//System.out.println("constante:"+this);
		
	}


	@Override
	protected void evaluate(ExprTreeFunction context) 
	{
		push(context,getValue());	
	}
	
	public void setValue(double value) 
	{
		this.value = value;
	}

	public double getValue() 
	{
		return value;
	}


	@Override
	public IPrimitive copy() 
	{
		Constant cNew = new Constant();
		cNew.value = this.value;
		return cNew;
	}


	@Override
	public IPrimitive instance() 
	{
		return new Constant();
	}
	
	public String toString()
	{
		return "Cte";
	}
	
}
