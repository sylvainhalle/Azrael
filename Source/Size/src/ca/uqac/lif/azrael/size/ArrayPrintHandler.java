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

import ca.uqac.lif.azrael.PrintException;

public class ArrayPrintHandler extends ReferencePrintHandler
{
	public ArrayPrintHandler(SizePrinter printer)
	{
		super(printer);
	}
	
	@Override
	public boolean canHandle(Object o) 
	{
		return o.getClass().isArray();
	}

	@Override
	public Number getSize(Object o) throws PrintException 
	{
		Object[] array = (Object[]) o;
		int size = 16;
		for (Object elem : array)
		{
			size += (Integer) m_printer.print(elem);
			if (!SizePrinter.isPrimitive(elem))
			{
				// If the array does not store primitive values,
				// the size of an entry is that of a pointer
				size += SizePrinter.OBJREF_SIZE;
			}
		}
		return size;
	}
}
