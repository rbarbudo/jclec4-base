package net.sf.jclec.symreg;

import net.sf.jclec.exprtree.fun.AbstractPrimitive;
import net.sf.jclec.exprtree.fun.ExprTreeFunction;

public class Div extends AbstractPrimitive 
{
	private static final long serialVersionUID = -8752124434918959738L;

	/**
	 * This operator receives two double arrays as arguments and return
	 * a double array as result.
	 */
	
	public Div() 
	{
		super(new Class<?> [] {Double.class, Double.class}, Double.class);
	}

	@Override
	protected void evaluate(ExprTreeFunction context) 
	{
		// Get arguments (in context stack)
		Double arg1 = pop(context);
		Double arg2 = pop(context);
		// Push result in context stack
		if(arg2 != 0)
			push(context, arg1/arg2);
		else
			push(context, 0.0);
	}

	// java.lang.Object methods
	
	public boolean equals(Object other)
	{
		return other instanceof Div;
	}	
	
	public String toString()
	{
		return "/";
	}	
}
