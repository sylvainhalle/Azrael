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

import java.util.IdentityHashMap;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.PrintHandler;

public abstract class ReferencePrintHandler implements PrintHandler<Number>
{
	protected IdentityHashMap<Object,Integer> m_seenObjects;
	
	protected SizePrinter m_printer;
	
	public ReferencePrintHandler(SizePrinter printer)
	{
		super();
		m_printer = printer;
		m_seenObjects = new IdentityHashMap<Object,Integer>();
	}
	
	@Override
	public final Number handle(Object o) throws PrintException 
	{
		// We count objects only once
		if (m_seenObjects.containsKey(o))
		{
			return 0;
		}
		m_seenObjects.put(o, 1);
		return getSize(o);
	}
	
	public abstract Number getSize(Object o) throws PrintException;
	
	@Override
	public void reset()
	{
		m_seenObjects.clear();
	}
}
