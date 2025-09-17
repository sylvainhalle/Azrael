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

import java.util.List;

import ca.uqac.lif.azrael.ReadException;
import ca.uqac.lif.xml.XmlElement;

/**
 * Object reader that reads an XML <em>string</em> and recreates an object
 * from it. It is a simple wrapper around {@link XmlReader} that parses
 * an input string into an XML element.
 * @author Sylvain Hallé
 */
public class ListReadHandler extends XmlReadHandler
{	
	/**
	 * Creates a new Xml string reader
	 */
	public ListReadHandler(XmlReader reader)
	{
		super(reader);
	}
	
	@Override
	public boolean canHandle(XmlElement o) throws ReadException
	{
		if (!m_reader.isWrapped(o))
		{
			return false;
		}
		Class<?> clazz = m_reader.unwrapType(o);
		return List.class.isAssignableFrom(clazz);
	}
	
	@Override
	public List<?> handle(XmlElement o) throws ReadException
	{
		Class<?> clazz = m_reader.unwrapType(o);
		XmlElement in_list = m_reader.unwrapContents(o);
		@SuppressWarnings("unchecked")
		List<Object> out_list = (List<Object>) m_reader.getInstance(clazz);
		for (XmlElement child : in_list.getChildren())
		{
			Object o_k = m_reader.read(child);
			out_list.add(o_k);
		}	
		return out_list;
	}
}
