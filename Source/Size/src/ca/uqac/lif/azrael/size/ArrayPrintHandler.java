package ca.uqac.lif.azrael.size;

import ca.uqac.lif.azrael.PrintException;

public class ArrayPrintHandler extends ReferencePrintHandler
{
	public ArrayPrintHandler(SizePrinter printer)
	{
		super(printer);
	}
	
	@Override
	public boolean canHandle(Object o) 
	{
		return o.getClass().isArray();
	}

	@Override
	public Number getSize(Object o) throws PrintException 
	{
		Object[] array = (Object[]) o;
		int size = 16;
		for (Object elem : array)
		{
			size += (Integer) m_printer.print(elem);
			if (!SizePrinter.isPrimitive(elem))
			{
				// If the array does not store primitive values,
				// the size of an entry is that of a pointer
				size += SizePrinter.OBJREF_SIZE;
			}
		}
		return size;
	}
}
