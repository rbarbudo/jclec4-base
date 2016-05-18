package net.sf.jclec.symreg;

import net.sf.jclec.exprtree.fun.AbstractPrimitive;
import net.sf.jclec.exprtree.fun.ExprTreeFunction;

/**
 * @author Rafael Barbudo Lunar
 *
 */
public class Sqrt extends AbstractPrimitive 
{
	private static final long serialVersionUID = 4361377041058015617L;

	/**
	 * This operator receives on double array as argument and return
	 * another double array as result.
	 */
	
	public Sqrt() 
	{
		super(new Class<?> [] {Double.class}, Double.class);
	}

	@Override
	protected void evaluate(ExprTreeFunction context) 
	{
		// Get arguments (in context stack)
		Double arg1 = pop(context);
		// Push result in context stack
		push(context, Math.sqrt(arg1));
	}

	// java.lang.Object methods
	
	public boolean equals(Object other)
	{
		return other instanceof Sqrt;
	}	
	
	public String toString()
	{
		return "Square";
	}	
}
