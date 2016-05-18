package net.sf.jclec.symreg;

import net.sf.jclec.exprtree.fun.Argument;

public class X3 extends Argument<Double> 
{
	private static final long serialVersionUID = 1214063510339143669L;

	public X3() 
	{
		super(Double.class, 2);
	}
	
	// java.lang.Object methods
	
	public boolean equals(Object other)
	{
		return other instanceof X3;
	}	
	
	public String toString()
	{
		return "X3";
	}
}