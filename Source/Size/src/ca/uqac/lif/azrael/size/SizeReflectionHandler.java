package ca.uqac.lif.azrael.size;

import java.util.IdentityHashMap;
import java.util.Map;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReflectionPrintHandler;

public class SizeReflectionHandler extends ReflectionPrintHandler<Number>
{
	protected IdentityHashMap<Object,Integer> m_seenObjects;

	public SizeReflectionHandler(SizePrinter s) 
	{
		super(s);
		m_ignoreTransient = false;
		m_seenObjects = new IdentityHashMap<Object,Integer>();
	}

	@Override
	public Number handle(Object o) throws PrintException
	{
		// We count objects only once
		if (m_seenObjects.containsKey(o))
		{
			return 0;
		}
		m_seenObjects.put(o, 1);
		return super.handle(o);
	}
	
	@Override
	public Number encapsulateFields(Object o, Map<String,Object> contents) throws PrintException
	{
		int size = 24; // Basic overhead of a Java object
		for (Object f_v : contents.values())
		{
			if (SizePrinter.isPrimitive(f_v))
			{
				size += (Integer) m_printer.print(f_v);
			}
			else
			{
				size += SizePrinter.OBJREF_SIZE;
				size += (Integer) m_printer.print(f_v);
			}
		}
		return size;
	}
	
	@Override
	public void reset()
	{
		m_seenObjects.clear();
	}
}
