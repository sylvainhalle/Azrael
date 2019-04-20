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
package ca.uqac.lif.azrael.json;

import java.util.Queue;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;

public class QueuePrintHandler extends JsonPrintHandler
{
	public QueuePrintHandler(JsonPrinter printer)
	{
		super(printer);
	}
	
	@Override
	public boolean canHandle(Object o) 
	{
		return o instanceof Queue;
	}

	@Override
	public JsonElement handle(Object o) throws PrintException 
	{
		Queue<?> queue = (Queue<?>) o;
		JsonList out_list = new JsonList();
		for (Object e: queue)
		{
			JsonElement j_e = m_printer.print(e);
			out_list.add(j_e);
		}
		return m_printer.wrap(o, out_list);
	}
}
