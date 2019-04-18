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

import ca.uqac.lif.azrael.ReadException;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;

public class MapReadHandler extends JsonReadHandler
{

	public MapReadHandler(JsonReader reader)
	{
		super(reader);
	}

	@Override
	public boolean canHandle(JsonElement o) throws ReadException
	{
		if (!m_reader.isWrapped(o))
		{
			return false;
		}
		Class<?> clazz = m_reader.unwrapType(o);
		return Map.class.isAssignableFrom(clazz);
	}

	@Override
	public Map<?,?> handle(JsonElement o) throws ReadException
	{
		Class<?> clazz = m_reader.unwrapType(o);
		JsonMap in_map = (JsonMap) m_reader.unwrapContents(o);
		JsonList l_keys = (JsonList) in_map.get(MapPrintHandler.KEY_NAME);
		JsonList l_values = (JsonList) in_map.get(MapPrintHandler.KEY_VALUE);
		@SuppressWarnings("unchecked")
		Map<Object,Object> out_map = (Map<Object,Object>) m_reader.getInstance(clazz);
		int size = Math.min(l_keys.size(), l_values.size());
		for (int i = 0; i < size; i++)
		{
			Object o_k = m_reader.read(l_keys.get(i));
			Object o_v = m_reader.read(l_values.get(i));
			out_map.put(o_k, o_v);
		}
		return out_map;
	}

}
