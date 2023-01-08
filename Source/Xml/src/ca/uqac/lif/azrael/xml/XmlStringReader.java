/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2023 Sylvain Hallé
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

import ca.uqac.lif.azrael.ObjectReader;
import ca.uqac.lif.azrael.ReadException;
import ca.uqac.lif.xml.XmlElement;
import ca.uqac.lif.xml.XmlElement.XmlParseException;

/**
 * Object reader that reads an XML <em>string</em> and recreates an object
 * from it. It is a simple wrapper around {@link XmlReader} that parses
 * an input string into an XML element.
 * @author Sylvain Hallé
 */
public class XmlStringReader extends ObjectReader<String>
{
	/**
	 * The reader used to read the Xml element
	 */
	XmlReader m_reader;
	
	/**
	 * Creates a new Xml string reader
	 */
	public XmlStringReader()
	{
		super();
		m_reader = new XmlReader();
	}
	
	@Override
	public Object read(Object o) throws ReadException
	{
		if (!(o instanceof String))
		{
			throw new ReadException("Expected a string");
		}
		String s = (String) o;
		try 
		{
			XmlElement e = XmlElement.parse(s);
			return m_reader.read(e);
		}
		catch (XmlParseException e) 
		{
			throw new ReadException(e);
		}
	}
	
	@Override
	protected String getWrappedTypeName(Object t) throws ReadException 
	{
		return m_reader.getWrappedTypeName(t);
	}

	@Override
	protected Class<?> unwrapType(Object t) throws ReadException 
	{
		return m_reader.unwrapType(t);
	}

	@Override
	protected Object unwrapContents(Object t) throws ReadException 
	{
		return m_reader.unwrapContents(t);
	}

	@Override
	protected boolean isWrapped(Object t) 
	{
		return m_reader.isWrapped(t);
	}
}
