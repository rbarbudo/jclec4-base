package net.sf.jclec.symreg;

import net.sf.jclec.exprtree.fun.AbstractPrimitive;
import net.sf.jclec.exprtree.fun.ExprTreeFunction;

/**
 * @author Rafael Barbudo Lunar
 *
 */
public class Log extends AbstractPrimitive 
{
	private static final long serialVersionUID = 4361377041058015617L;

	/**
	 * This operator receives on double array as argument and return
	 * another double array as result.
	 */
	
	public Log() 
	{
		super(new Class<?> [] {Double.class}, Double.class);
	}

	@Override
	protected void evaluate(ExprTreeFunction context) 
	{
		// Get arguments (in context stack)
		Double arg1 = pop(context);
		// Push result in context stack
		if(arg1 == 0)
			push(context, Math.log10(0.0001));
		else
			push(context, Math.log10(Math.abs(arg1)));
	}

	// java.lang.Object methods
	
	public boolean equals(Object other)
	{
		return other instanceof Log;
	}	
	
	public String toString()
	{
		return "Log";
	}	
}
