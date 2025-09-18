/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2025 Sylvain Hallé
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

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.xml.TextElement;
import ca.uqac.lif.xml.XmlElement;

/**
 * Exports a string.
 * @author Sylvain Hallé
 */
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
		xe.addChild(new TextElement(escape(s)));
		return xe;
	}
	
	/**
	 * Escapes special characters in a string so that it can be safely embedded in
	 * an XML document.
	 * 
	 * @param s
	 *          The string to escape
	 * @return The escaped string
	 */
	protected static String escape(String s)
	{
		s = s.replace("&", "&amp;");
		s = s.replace("<", "&lt;");
		s = s.replace(">", "&gt;");
		s = s.replace("\"", "&quot;");
		s = s.replace("'", "&apos;");
		return s;
	}
}
