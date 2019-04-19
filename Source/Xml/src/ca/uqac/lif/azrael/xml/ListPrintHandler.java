
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
 */package ca.uqac.lif.azrael.xml;

import java.util.List;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.xml.XmlElement;

/**
 * Exports a list.
 * @author Sylvain Hallé
 */
public class ListPrintHandler extends XmlPrintHandler
{
	public ListPrintHandler(XmlPrinter printer)
	{
		super(printer);
	}

	@Override
	public boolean canHandle(Object o) 
	{
		return o instanceof List;
	}

	@Override
	public XmlElement handle(Object o) throws PrintException
	{
		List<?> in_list = (List<?>) o;
		XmlElement x_list = new XmlElement(XmlPrinter.s_listName);
		for (Object e : in_list)
		{
			XmlElement xe = m_printer.print(e);
			x_list.addChild(xe);
		}
		return m_printer.wrap(o, x_list);
	}
}
