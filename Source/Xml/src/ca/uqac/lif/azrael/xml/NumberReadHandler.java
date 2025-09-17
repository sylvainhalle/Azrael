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

import ca.uqac.lif.azrael.ReadException;
import ca.uqac.lif.xml.TextElement;
import ca.uqac.lif.xml.XmlElement;

/**
 * Object reader that reads an XML <em>string</em> and recreates an object
 * from it. It is a simple wrapper around {@link XmlReader} that parses
 * an input string into an XML element.
 * @author Sylvain Hallé
 */
public class NumberReadHandler extends XmlReadHandler
{	
	/**
	 * Creates a new Xml string reader
	 */
	public NumberReadHandler(XmlReader reader)
	{
		super(reader);
	}

	@Override
	public boolean canHandle(XmlElement o)
	{
		return o.getName().compareTo(XmlPrinter.s_numberName) == 0;
	}

	@Override
	public Number handle(XmlElement o) throws ReadException
	{
		XmlElement child = o.getChildren().get(0);
		if (!(child instanceof TextElement))
		{
			throw new ReadException("Expected a text element");
		}
		String s = ((TextElement) child).getText();
		String beginning = s.substring(0, s.length() - 1);
		String type = s.substring(s.length() - 1);
		if (type.compareTo("L") == 0)
		{
			return Long.parseLong(beginning);
		}
		if (type.compareTo("I") == 0)
		{
			return Integer.parseInt(beginning);
		}
		if (type.compareTo("F") == 0)
		{
			return Float.parseFloat(beginning);
		}
		if (type.compareTo("D") == 0)
		{
			return Double.parseDouble(beginning);
		}
		throw new ReadException("Invalid number type");
	}
}
