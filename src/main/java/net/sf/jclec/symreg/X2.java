package net.sf.jclec.symreg;

import net.sf.jclec.exprtree.fun.Argument;

public class X2 extends Argument<Double> 
{
	private static final long serialVersionUID = 1214063510339143669L;

	public X2() 
	{
		super(Double.class, 1);
	}
	
	// java.lang.Object methods
	
	public boolean equals(Object other)
	{
		return other instanceof X2;
	}	
	
	public String toString()
	{
		return "X2";
	}
}