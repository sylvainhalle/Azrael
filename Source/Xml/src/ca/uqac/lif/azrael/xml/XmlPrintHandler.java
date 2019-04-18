package ca.uqac.lif.azrael.xml;

import ca.uqac.lif.azrael.PrintHandler;
import ca.uqac.lif.xml.XmlElement;

public abstract class XmlPrintHandler implements PrintHandler<XmlElement>
{
	protected XmlPrinter m_printer;
	
	public XmlPrintHandler(XmlPrinter printer)
	{
		super();
		m_printer = printer;
	}
	
	@Override
	public void reset() 
	{
		// Nothing to do
	}

}
