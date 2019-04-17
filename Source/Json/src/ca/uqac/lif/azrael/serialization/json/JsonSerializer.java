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
package ca.uqac.lif.azrael.serialization.json;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.serialization.ObjectSerializer;
import ca.uqac.lif.json.JsonBoolean;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonFalse;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNull;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.json.JsonTrue;

public class JsonSerializer extends ObjectSerializer<JsonElement>
{
	public static final transient String CLASS_KEY = "!c";
	
	public static final transient String CONTENT_KEY = "!t";
	
	@Override
	public JsonElement print(Object o) throws PrintException
	{
		if (o instanceof JsonElement)
		{
			return wrap(o, (JsonElement) o);
		}
		return super.print(o);
	}
	
	@Override
	public JsonNull printNull() throws PrintException
	{
		return JsonNull.instance;
	}
	
	@Override
	public JsonBoolean print(Boolean b) throws PrintException
	{
		if (b)
		{
			return JsonTrue.instance;
		}
		return JsonFalse.instance;
	}

	@Override
	public JsonNumber print(Number n) throws PrintException
	{
		return new JsonNumber(n);
	}

	@Override
	public JsonString print(String s) throws PrintException 
	{
		return new JsonString(s);
	}

	@Override
	public JsonElement print(Map<String,?> m) throws PrintException
	{
		if (m instanceof JsonMap)
		{
			return wrap(m, (JsonMap) m);
		}
		JsonMap map = new JsonMap();
		for (Map.Entry<String,?> entry : m.entrySet())
		{
			Object value = entry.getValue();
			if (value instanceof JsonElement)
			{
				map.put(entry.getKey(), value);
			}
			else
			{
				map.put(entry.getKey(), print(value));
			}
		}
		return map;
	}

	@Override
	public JsonElement print(List<?> list) throws PrintException 
	{
		if (list instanceof JsonList)
		{
			return wrap(list, (JsonList) list);
		}
		JsonList out_list = new JsonList();
		for (Object o : list)
		{
			if (o instanceof JsonElement)
			{
				out_list.add((JsonElement) o);
			}
			else
			{
				out_list.add(print(o));
			}
		}
		return out_list;
	}
	
	@Override
	public JsonElement print(Queue<?> s) throws PrintException 
	{
		return wrapToList(s);
	}
	
	@Override
	public JsonElement print(Set<?> s) throws PrintException 
	{
		return wrapToList(s);
	}
	
	/**
	 * Wraps a collection into a JSON list
	 * @param c The collection
	 * @return The list
	 * @throws PrintException Thrown if an error occurs during serialization
	 */
	protected JsonElement wrapToList(Collection<?> c) throws PrintException
	{
		JsonList out_list = new JsonList();
		for (Object o : c)
		{
			if (o instanceof JsonElement)
			{
				out_list.add((JsonElement) o);
			}
			else
			{
				out_list.add(print(o));
			}
		}
		return wrap(c, out_list);
	}

	@Override
	public JsonElement wrap(Object o, JsonElement t) throws PrintException
	{
		JsonMap map = new JsonMap();
		map.put(CLASS_KEY, new JsonString(o.getClass().getName()));
		map.put(CONTENT_KEY, t);
		return map;
	}
}
