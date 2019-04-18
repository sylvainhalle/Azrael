package ca.uqac.lif.azrael.xml;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.xml.XmlElement;

public class NullPrintHandler extends XmlPrintHandler
{
	public NullPrintHandler(XmlPrinter printer)
	{
		super(printer);
	}

	@Override
	public boolean canHandle(Object o) 
	{
		return o == null;
	}

	@Override
	public XmlElement handle(Object o) throws PrintException
	{
		return XmlPrinter.NULL;
	}
}
