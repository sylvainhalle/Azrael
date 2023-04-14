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

import ca.uqac.lif.azrael.ObjectPrinter;
import ca.uqac.lif.azrael.PrintException;

public class SizePrinter extends ObjectPrinter<Number>
{
	public static final int OBJECT_SHELL_SIZE   = 24;
	public static final int OBJREF_SIZE         = 4;
	public static final int LONG_FIELD_SIZE     = 8;
	public static final int INT_FIELD_SIZE      = 4;
	public static final int SHORT_FIELD_SIZE    = 2;
	public static final int CHAR_FIELD_SIZE     = 2;
	public static final int BYTE_FIELD_SIZE     = 1;
	public static final int BOOLEAN_FIELD_SIZE  = 1;
	public static final int DOUBLE_FIELD_SIZE   = 8;
	public static final int FLOAT_FIELD_SIZE    = 4;

	public SizePrinter()
	{
		super();
		m_handlers.add(new NullPrintHandler(this));
		m_handlers.add(new NumberPrintHandler());
		m_handlers.add(new StringPrintHandler(this));
		m_handlers.add(new BooleanPrintHandler());
		m_handlers.add(new CollectionPrintHandler(this));
		m_handlers.add(new ArrayPrintHandler(this));
		m_reflectionHandler = new SizeReflectionHandler(this);
		m_usePrintable = false;
	}

	@Override
	public Number wrap(Object o, Number t) throws PrintException
	{
		return t;
	}

	/**
	 * Checks if an object is a primitive
	 * @param o The object
	 * @return <tt>true</tt> if the object is primitive, <tt>false</tt>
	 * otherwise
	 */
	public static boolean isPrimitive(Object o)
	{
		return o == null || o instanceof Boolean || o instanceof Number || o instanceof String;
	}
}
