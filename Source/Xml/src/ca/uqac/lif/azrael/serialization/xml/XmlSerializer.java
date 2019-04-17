package ca.uqac.lif.azrael.serialization.xml;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.serialization.ObjectSerializer;
import ca.uqac.lif.xml.TextElement;
import ca.uqac.lif.xml.XmlElement;

public class XmlSerializer extends ObjectSerializer<XmlElement>
{
	public static final XmlElement TRUE = new XmlElement("true");
	
	public static final XmlElement FALSE = new XmlElement("false");
	
	public static final XmlElement NULL = new XmlElement("null");
	
	public static final String s_numberName = "num";
	
	public static final String s_stringName = "str";
	
	public static final String s_mapName = "map";
	
	@Override
	public XmlElement print(Boolean b) throws PrintException
	{
		if (b == null)
		{
			return NULL;
		}
		if (b)
		{
			return TRUE;
		}
		return FALSE;
	}

	@Override
	public XmlElement print(Number n) throws PrintException
	{
		XmlElement xe = new XmlElement(s_numberName);
		xe.addChild(new TextElement(n.toString()));
		return xe;
	}

	@Override
	public XmlElement print(String s) throws PrintException 
	{
		XmlElement xe = new XmlElement(s_stringName);
		xe.addChild(new TextElement(s));
		return xe;
	}

	@Override
	public XmlElement print(Map<String, ?> m) throws PrintException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlElement print(List<?> m) throws PrintException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlElement print(Set<?> m) throws PrintException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlElement print(Queue<?> m) throws PrintException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlElement printNull() throws PrintException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlElement wrap(Object o, XmlElement t) throws PrintException {
		// TODO Auto-generated method stub
		return null;
	}
}
