package net.sf.jclec.symreg;

import net.sf.jclec.IConfigure;
import net.sf.jclec.util.random.IRandGen;
import net.sf.jclec.util.range.IRange;

/**
 * Interface used to defined a primitive whit a 
 * single value which has to schematized
 * 
 * @author Rafael Barbudo Lunar
 */

public interface ISimpleSchematized extends IConfigure
{
	/**
	 * Gets the schema of the primitive
	 * 
	 * @return The schema
	 */
	
	public IRange getSchema();
	
	/**
	 * Sets the schema of the primitive
	 * 
	 * @param schema The new schema
	 */
	
	public void setSchema(IRange schema);	
	
	/**
	 * Sets the random generator
	 * 
	 * @param schema The new schema
	 */
	
	public void setRandomGenerator(IRandGen randgen);
}