package net.sf.jclec.symreg;

import net.sf.jclec.exprtree.fun.Argument;

public class Y extends Argument<Double> 
{
	private static final long serialVersionUID = 1214063510339143669L;

	public Y() 
	{
		super(Double.class, 1);
	}
	
	// java.lang.Object methods
	
	public boolean equals(Object other)
	{
		return other instanceof Y;
	}	
	
	public String toString()
	{
		return "Y";
	}
}