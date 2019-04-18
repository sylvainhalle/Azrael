package ca.uqac.lif.azrael.xml;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.xml.XmlElement;

public class BooleanPrintHandler extends XmlPrintHandler
{
	public BooleanPrintHandler(XmlPrinter printer)
	{
		super(printer);
	}

	@Override
	public boolean canHandle(Object o) 
	{
		return o instanceof Boolean;
	}

	@Override
	public XmlElement handle(Object o) throws PrintException
	{
		Boolean b = (Boolean) o;
		if (b == null)
		{
			return XmlPrinter.NULL;
		}
		if (b)
		{
			return XmlPrinter.TRUE;
		}
		return XmlPrinter.FALSE;
	}
}
