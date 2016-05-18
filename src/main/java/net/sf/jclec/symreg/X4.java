package net.sf.jclec.symreg;

import net.sf.jclec.exprtree.fun.Argument;

public class X4 extends Argument<Double> 
{
	private static final long serialVersionUID = 1214063510339143669L;

	public X4() 
	{
		super(Double.class, 3);
	}
	
	// java.lang.Object methods
	
	public boolean equals(Object other)
	{
		return other instanceof X4;
	}	
	
	public String toString()
	{
		return "X4";
	}
}