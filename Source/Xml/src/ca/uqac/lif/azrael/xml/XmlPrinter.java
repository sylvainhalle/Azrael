/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2019 Sylvain Hallé
    Laboratoire d'informatique formelle
    Université du Québec à Chicoutimi, Canada

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.
    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.azrael.xml;

import ca.uqac.lif.azrael.ObjectPrinter;
import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.xml.TextElement;
import ca.uqac.lif.xml.XmlElement;

/**
 * Prints an object into an XML element.
 * @author Sylvain Hallé
 */
public class XmlPrinter extends ObjectPrinter<XmlElement>
{
	public static final XmlElement TRUE = new XmlElement("true");
	
	public static final XmlElement FALSE = new XmlElement("false");
	
	public static final XmlElement NULL = new XmlElement("null");
	
	public static final String s_numberName = "num";
	
	public static final String s_stringName = "str";
	
	public static final String s_mapName = "map";
	
	public static final String s_listName = "list";
	
	public static final String s_entryName = "entry";
	
	public static final String s_wrapName = "object";
	
	public static final String s_classKey = "class";
	
	public static final String s_keyKey = "key";
	
	public static final String s_valueKey = "value";
	
	/**
	 * Creates a new XML printer
	 */
	public XmlPrinter()
	{
		super();
		m_handlers.add(new NullPrintHandler(this));
		m_handlers.add(new BooleanPrintHandler(this));
		m_handlers.add(new NumberPrintHandler(this));
		m_handlers.add(new StringPrintHandler(this));
		m_handlers.add(new ListPrintHandler(this));
		m_handlers.add(new MapPrintHandler(this));
	}
	
	@Override
	public XmlElement wrap(Object o, XmlElement t) throws PrintException
	{
		XmlElement obj = new XmlElement(s_wrapName);
		XmlElement cl = new XmlElement(s_classKey);
		cl.addChild(new TextElement(o.getClass().getName()));
		obj.addChild(cl);
		XmlElement va = new XmlElement(s_valueKey);
		va.addChild(t);
		obj.addChild(va);
		return obj;
	}
}
