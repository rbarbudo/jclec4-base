package net.sf.jclec.symreg;

import net.sf.jclec.exprtree.fun.AbstractPrimitive;
import net.sf.jclec.exprtree.fun.ExprTreeFunction;

/**
 * @author Rafael Barbudo Lunar
 *
 */
public class Tan extends AbstractPrimitive 
{
	private static final long serialVersionUID = -2234209831796929654L;

	/**
	 * This operator receives on double array as argument and return
	 * another double array as result.
	 */
	
	public Tan() 
	{
		super(new Class<?> [] {Double.class}, Double.class);
	}

	@Override
	protected void evaluate(ExprTreeFunction context) 
	{
		// Get arguments (in context stack)
		Double arg1 = pop(context);
		// Push result in context stack
		push(context, Math.tan(arg1));
	}

	// java.lang.Object methods
	
	public boolean equals(Object other)
	{
		return other instanceof Tan;
	}	
	
	public String toString()
	{
		return "Tan";
	}	
}
