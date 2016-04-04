package net.sf.jclec.symreg;

import net.sf.jclec.exprtree.fun.AbstractPrimitive;
import net.sf.jclec.exprtree.fun.ExprTreeFunction;

/**
 * @author Rafael Barbudo Lunar
 */

public class PseudoCte extends AbstractPrimitive 
{
	private static final long serialVersionUID = 4361377041058015617L;

	/**
	 * This operator receives on double array as argument and return
	 * another double array as result.
	 */
	
	public PseudoCte() 
	{
		super(new Class<?> [] {Double.class}, Double.class);
	}

	@Override
	protected void evaluate(ExprTreeFunction context) 
	{
		push(context, 1.0);
	}

	// java.lang.Object methods
	
	public boolean equals(Object other)
	{
		return other instanceof PseudoCte;
	}	
	
	public String toString()
	{
		return "1.0";
	}	
}