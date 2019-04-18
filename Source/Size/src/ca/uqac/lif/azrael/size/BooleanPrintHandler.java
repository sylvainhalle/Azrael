package ca.uqac.lif.azrael.size;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.PrintHandler;

public class BooleanPrintHandler implements PrintHandler<Number>
{

	@Override
	public boolean canHandle(Object o) 
	{
		return o instanceof Boolean;
	}

	@Override
	public Number handle(Object o) throws PrintException 
	{
		return SizePrinter.BOOLEAN_FIELD_SIZE;
	}
	
	@Override
	public void reset()
	{
		// Nothing to do
	}

}
