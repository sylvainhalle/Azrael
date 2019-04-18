package ca.uqac.lif.azrael.size;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.PrintHandler;

public class NumberPrintHandler implements PrintHandler<Number>
{

	@Override
	public boolean canHandle(Object o) 
	{
		return o instanceof Number;
	}

	@Override
	public Number handle(Object o) throws PrintException 
	{
		if (o instanceof Integer)
		{
			return SizePrinter.INT_FIELD_SIZE;
		}
		if (o instanceof Short)
		{
			return SizePrinter.SHORT_FIELD_SIZE;
		}
		if (o instanceof Byte)
		{
			return SizePrinter.BYTE_FIELD_SIZE;
		}
		if (o instanceof Long)
		{
			return SizePrinter.LONG_FIELD_SIZE;
		}
		if (o instanceof Float)
		{
			return SizePrinter.FLOAT_FIELD_SIZE;
		}
		if (o instanceof Double)
		{
			return SizePrinter.DOUBLE_FIELD_SIZE;
		}
		return 0;
	}

	@Override
	public void reset()
	{
		// Nothing to do
	}

}
