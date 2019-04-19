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
import ca.uqac.lif.azrael.PrintHandler;

public class NumberPrintHandler implements PrintHandler<Number>
{

	@Override
	public boolean canHandle(Object o) 
	{
		return o instanceof Number;
	}

	@Override
	public Number handle(Object o) throws PrintException 
	{
		if (o instanceof Integer)
		{
			return SizePrinter.INT_FIELD_SIZE;
		}
		if (o instanceof Short)
		{
			return SizePrinter.SHORT_FIELD_SIZE;
		}
		if (o instanceof Byte)
		{
			return SizePrinter.BYTE_FIELD_SIZE;
		}
		if (o instanceof Long)
		{
			return SizePrinter.LONG_FIELD_SIZE;
		}
		if (o instanceof Float)
		{
			return SizePrinter.FLOAT_FIELD_SIZE;
		}
		if (o instanceof Double)
		{
			return SizePrinter.DOUBLE_FIELD_SIZE;
		}
		return 0;
	}

	@Override
	public void reset()
	{
		// Nothing to do
	}

}
