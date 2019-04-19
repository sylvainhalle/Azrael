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
package ca.uqac.lif.azrael.size;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

import ca.uqac.lif.azrael.PrintException;

public class CollectionPrintHandler extends ReferencePrintHandler
{
	public CollectionPrintHandler(SizePrinter printer)
	{
		super(printer);
	}
	
	@Override
	public boolean canHandle(Object o) 
	{
		return o instanceof Collection || o instanceof Map;
	}

	@Override
	public Number getSize(Object o) throws PrintException 
	{
		if (o instanceof HashSet)
		{
			HashSet<?> col = (HashSet<?>) o;
			int size = 16 + 64 + 36 * col.size();
			for (Object elem : col)
			{
				size += (Integer) m_printer.print(elem);
			}
			return size;
		}
		if (o instanceof HashMap)
		{
			HashMap<?,?> col = (HashMap<?,?>) o;
			int size = 64 + 36 * col.size();
			for (Map.Entry<?,?> entry : col.entrySet())
			{
				size += (Integer) m_printer.print(entry.getKey());
				size += (Integer) m_printer.print(entry.getValue());
			}
			return size;
		}
		if (o instanceof Hashtable)
		{
			Hashtable<?,?> col = (Hashtable<?,?>) o;
			int size = 56 + 36 * col.size();
			for (Map.Entry<?,?> entry : col.entrySet())
			{
				size += (Integer) m_printer.print(entry.getKey());
				size += (Integer) m_printer.print(entry.getValue());
			}
			return size;
		}
		if (o instanceof LinkedList)
		{
			LinkedList<?> col = (LinkedList<?>) o;
			int size = 24 + 24 * col.size();
			for (Object elem : col)
			{
				size += (Integer) m_printer.print(elem);
			}
			return size;
		}
		if (o instanceof ArrayList)
		{
			ArrayList<?> col = (ArrayList<?>) o;
			int size = 48 + 4 * col.size();
			for (Object elem : col)
			{
				size += (Integer) m_printer.print(elem);
			}
			return size;
		}
		return 0;
	}

}
