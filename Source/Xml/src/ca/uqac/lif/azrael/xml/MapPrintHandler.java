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

import java.util.Map;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.xml.XmlElement;

public class MapPrintHandler extends XmlPrintHandler
{
	public MapPrintHandler(XmlPrinter printer)
	{
		super(printer);
	}

	@Override
	public boolean canHandle(Object o) 
	{
		return o instanceof Map;
	}

	@Override
	public XmlElement handle(Object o) throws PrintException
	{
		Map<?,?> in_map = (Map<?,?>) o;
		XmlElement x_map = new XmlElement(XmlPrinter.s_mapName);
		for (Map.Entry<?,?> entry : in_map.entrySet())
		{
			XmlElement x_entry = new XmlElement(XmlPrinter.s_entryName);
			XmlElement x_key = new XmlElement(XmlPrinter.s_keyKey);
			x_key.addChild(m_printer.print(entry.getKey()));
			XmlElement x_value = new XmlElement(XmlPrinter.s_valueKey);
			x_value.addChild(m_printer.print(entry.getKey()));
			x_entry.addChild(x_key);
			x_entry.addChild(x_value);
			x_map.addChild(x_entry);
		}
		return m_printer.wrap(o, x_map);
	}
}
