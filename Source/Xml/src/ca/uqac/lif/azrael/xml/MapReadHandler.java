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

import java.util.Map;

import ca.uqac.lif.azrael.ReadException;
import ca.uqac.lif.xml.XmlElement;

/**
 * Object reader that reads an XML <em>string</em> and recreates an object
 * from it. It is a simple wrapper around {@link XmlReader} that parses
 * an input string into an XML element.
 * @author Sylvain Hallé
 */
public class MapReadHandler extends XmlReadHandler
{	
	/**
	 * Creates a new Xml string reader
	 */
	public MapReadHandler(XmlReader reader)
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
		return Map.class.isAssignableFrom(clazz);
	}
	
	@Override
	public Map<?,?> handle(XmlElement o) throws ReadException
	{
		Class<?> clazz = m_reader.unwrapType(o);
		XmlElement in_map = m_reader.unwrapContents(o);
		@SuppressWarnings("unchecked")
		Map<Object,Object> out_map = (Map<Object,Object>) m_reader.getInstance(clazz);
		for (XmlElement child : in_map.getChildren())
		{
			if (!child.getName().equals(XmlPrinter.s_entryName))
			{
				throw new ReadException("Expected entry element, found " + child.getName());
			}
			XmlElement c_key = XmlReader.getChildWithName(child, XmlPrinter.s_keyKey);
			XmlElement c_value = XmlReader.getChildWithName(child, XmlPrinter.s_valueKey);
			Object o_k = m_reader.read(c_key.getChildren().get(0));
			Object o_v = m_reader.read(c_value.getChildren().get(0));
			out_map.put(o_k, o_v);
		}	
		return out_map;
	}
}
