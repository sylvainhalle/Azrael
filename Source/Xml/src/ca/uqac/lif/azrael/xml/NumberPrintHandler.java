package ca.uqac.lif.azrael.xml;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.xml.TextElement;
import ca.uqac.lif.xml.XmlElement;

public class NumberPrintHandler extends XmlPrintHandler
{
	public NumberPrintHandler(XmlPrinter printer)
	{
		super(printer);
	}

	@Override
	public boolean canHandle(Object o) 
	{
		return o instanceof Number;
	}

	@Override
	public XmlElement handle(Object o) throws PrintException
	{
		Number n = (Number) o;
		XmlElement xe = new XmlElement(XmlPrinter.s_numberName);
		xe.addChild(new TextElement(n.toString()));
		return xe;
	}
}
