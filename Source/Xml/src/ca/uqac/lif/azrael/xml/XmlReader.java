/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2022 Sylvain Hallé
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
import ca.uqac.lif.xml.TextElement;
import ca.uqac.lif.xml.XmlElement;

/**
 * Reads the contents of an object from an XML element.
 * @author Sylvain Hallé
 */
public class XmlReader extends ObjectReader<XmlElement>
{
	/**
	 * Creates a new XML reader
	 */
	public XmlReader()
	{
		super();
		m_handlers.add(new NullReadHandler(this));
		m_handlers.add(new EnumReadHandler(this));
		m_handlers.add(new ListReadHandler(this));
		m_handlers.add(new MapReadHandler(this));
		m_handlers.add(new StringReadHandler(this));
		m_handlers.add(new NumberReadHandler(this));
	}

	@Override
	protected String getWrappedTypeName(Object t) throws ReadException
	{
		if (!(t instanceof XmlElement))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		XmlElement m = (XmlElement) t;
		if (m.getName().compareTo(XmlPrinter.s_stringName) == 0)
		{
			return String.class.toString();
		}
		XmlElement class_elem = getChildWithName(m, XmlPrinter.s_classKey);
		if (class_elem == null)
		{
			throw new ReadException("No class key found in element");
		}
		return ((TextElement) class_elem.getChildren().get(0)).getText();
	}

	@Override
	protected Class<?> unwrapType(Object t) throws ReadException
	{
		try 
		{
			return Class.forName(getWrappedTypeName(t));
		} 
		catch (ClassNotFoundException e) 
		{
			throw new ReadException(e);
		}
	}

	@Override
	protected XmlElement unwrapContents(Object t) throws ReadException 
	{
		if (!(t instanceof XmlElement))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		XmlElement m = (XmlElement) t;
		XmlElement value_elem = getChildWithName(m, XmlPrinter.s_valueKey);
		if (value_elem == null)
		{
			throw new ReadException("No value key found in element");
		}
		return value_elem.getChildren().get(0);
	}

	@Override
	protected boolean isWrapped(Object t) 
	{
		if (!(t instanceof XmlElement))
		{
			return false;
		}
		XmlElement m = (XmlElement) t;
		XmlElement class_elem = getChildWithName(m, XmlPrinter.s_classKey);
		XmlElement value_elem = getChildWithName(m, XmlPrinter.s_valueKey);
		return class_elem != null && value_elem != null;
	}

	/**
	 * Searches for a child element with a given name.
	 * @param e the parent element
	 * @param name The name to search for
	 * @return The first child with the given name, or <tt>null</tt> if no
	 * such child exists
	 */
	public static XmlElement getChildWithName(XmlElement e, String name)
	{
		for (XmlElement child : e.getChildren())
		{
			if (child.getName().compareTo(name) == 0)
			{
				return child;
			}
		}
		return null;
	}

}
