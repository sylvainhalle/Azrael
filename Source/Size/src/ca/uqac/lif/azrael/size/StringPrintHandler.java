package ca.uqac.lif.azrael.size;

import ca.uqac.lif.azrael.PrintException;

public class StringPrintHandler extends ReferencePrintHandler
{
	public StringPrintHandler(SizePrinter printer)
	{
		super(printer);
	}

	@Override
	public boolean canHandle(Object o) 
	{
		return o instanceof String;
	}

	@Override
	public Number getSize(Object o) throws PrintException 
	{
		String s = (String) o;
		return 28 + SizePrinter.CHAR_FIELD_SIZE * s.length();
	}

}
