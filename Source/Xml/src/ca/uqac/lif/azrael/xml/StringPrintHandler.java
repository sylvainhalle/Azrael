package ca.uqac.lif.azrael.xml;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.xml.TextElement;
import ca.uqac.lif.xml.XmlElement;

public class StringPrintHandler extends XmlPrintHandler
{
	public StringPrintHandler(XmlPrinter printer)
	{
		super(printer);
	}

	@Override
	public boolean canHandle(Object o) 
	{
		return o instanceof String;
	}

	@Override
	public XmlElement handle(Object o) throws PrintException
	{
		String s = (String) o;
		XmlElement xe = new XmlElement(XmlPrinter.s_stringName);
		xe.addChild(new TextElement(s));
		return xe;
	}
}
