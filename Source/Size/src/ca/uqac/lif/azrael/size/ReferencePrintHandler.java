package ca.uqac.lif.azrael.size;

import java.util.IdentityHashMap;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.PrintHandler;

public abstract class ReferencePrintHandler implements PrintHandler<Number>
{
	protected IdentityHashMap<Object,Integer> m_seenObjects;
	
	protected SizePrinter m_printer;
	
	public ReferencePrintHandler(SizePrinter printer)
	{
		super();
		m_printer = printer;
		m_seenObjects = new IdentityHashMap<Object,Integer>();
	}
	
	@Override
	public final Number handle(Object o) throws PrintException 
	{
		// We count objects only once
		if (m_seenObjects.containsKey(o))
		{
			return 0;
		}
		m_seenObjects.put(o, 1);
		return getSize(o);
	}
	
	public abstract Number getSize(Object o) throws PrintException;
	
	@Override
	public void reset()
	{
		m_seenObjects.clear();
	}
}
