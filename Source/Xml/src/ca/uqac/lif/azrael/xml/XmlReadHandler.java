package ca.uqac.lif.azrael.xml;

import ca.uqac.lif.azrael.ReadHandler;
import ca.uqac.lif.xml.XmlElement;

public abstract class XmlReadHandler implements ReadHandler<XmlElement>
{
	protected XmlReader m_reader;

	public XmlReadHandler(XmlReader printer)
	{
		super();
		m_reader = printer;
	}
}
