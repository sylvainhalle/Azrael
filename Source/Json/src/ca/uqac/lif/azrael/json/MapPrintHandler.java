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

import java.util.Map;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;

public class MapPrintHandler extends JsonPrintHandler
{
	public static String KEY_NAME = "!k";
	
	public static String KEY_VALUE = "!v";
	
	public MapPrintHandler(JsonPrinter printer)
	{
		super(printer);
	}
	
	@Override
	public boolean canHandle(Object o) 
	{
		return o instanceof Map;
	}
	
	@Override
	public JsonElement handle(Object o) throws PrintException 
	{
		Map<?,?> map = (Map<?,?>) o;
		JsonList keys = new JsonList();
		JsonList values = new JsonList();
		for (Map.Entry<?,?> entry : map.entrySet())
		{
			JsonElement j_key = m_printer.print(entry.getKey());
			keys.add(j_key);
			JsonElement j_value = m_printer.print(entry.getValue());
			values.add(j_value);
		}
		JsonMap out_map = new JsonMap();
		out_map.put(KEY_NAME, keys);
		out_map.put(KEY_VALUE, values);
		return m_printer.wrap(o, out_map);
	}

}
