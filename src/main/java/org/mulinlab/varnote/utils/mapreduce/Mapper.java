package org.mulinlab.varnote.utils.mapreduce;


/**
 * Mapper Interface
 * @author Li Jun Mulin
 *
 */
public interface Mapper<T> {

	public void doMap();
	
	public T getResult();
	
}
