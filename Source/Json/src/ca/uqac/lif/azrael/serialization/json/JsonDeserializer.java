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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import ca.uqac.lif.azrael.ReadException;
import ca.uqac.lif.azrael.serialization.ObjectDeserializer;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonFalse;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNull;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.json.JsonTrue;

public class JsonDeserializer extends ObjectDeserializer
{

	@Override
	public Boolean readBoolean(Object o) throws ReadException
	{
		if (o == null)
		{
			throw new ReadException("Cannot deserialize from null");
		}
		if (o instanceof JsonNull)
		{
			return null;
		}
		if (o instanceof JsonTrue)
		{
			return true;
		}
		if (o instanceof JsonFalse)
		{
			return false;
		}
		throw new ReadException("Incompatible element to deserialize from");
	}

	@Override
	public Number readNumber(Object o) throws ReadException
	{
		if (o == null)
		{
			throw new ReadException("Cannot deserialize from null");
		}
		if (o instanceof JsonNull)
		{
			return null;
		}
		if (!(o instanceof JsonNumber))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		return ((JsonNumber) o).numberValue();
	}

	@Override
	public String readString(Object o) throws ReadException
	{
		if (o == null)
		{
			throw new ReadException("Cannot deserialize from null");
		}
		if (o instanceof JsonNull)
		{
			return null;
		}
		if (!(o instanceof JsonString))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		return ((JsonString) o).stringValue();
	}

	@Override
	public Map<String, JsonElement> readMap(Object o) throws ReadException
	{
		if (o == null)
		{
			throw new ReadException("Cannot deserialize from null");
		}
		if (o instanceof JsonNull)
		{
			return null;
		}
		if (!(o instanceof JsonMap))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		JsonMap in_map = (JsonMap) o;
		Map<String,JsonElement> out_map = new HashMap<String,JsonElement>();
		for (Map.Entry<String,JsonElement> entry : in_map.entrySet())
		{
			out_map.put(entry.getKey(), entry.getValue());
		}
		return out_map;
	}

	@Override
	public List<JsonElement> readList(Object o) throws ReadException 
	{
		if (o == null)
		{
			throw new ReadException("Cannot deserialize from null");
		}
		if (o instanceof JsonNull)
		{
			return null;
		}
		if (!(o instanceof JsonList))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		JsonList in_list = (JsonList) o;
		List<JsonElement> out_list = new ArrayList<JsonElement>(in_list.size());
		for (JsonElement e : in_list)
		{
			out_list.add(e);
		}
		return out_list;
	}
	
	@Override
	public Set<JsonElement> readSet(Object o) throws ReadException 
	{
		if (o == null)
		{
			throw new ReadException("Cannot deserialize from null");
		}
		if (o instanceof JsonNull)
		{
			return null;
		}
		if (!(isWrapped(o)))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		JsonList in_list = (JsonList) o;
		Set<JsonElement> out_set = new HashSet<JsonElement>(in_list.size());
		for (JsonElement e : in_list)
		{
			out_set.add(e);
		}
		return out_set;
	}
	
	@Override
	public Queue<?> readQueue(Object o) throws ReadException
	{
		if (o == null)
		{
			throw new ReadException("Cannot deserialize from null");
		}
		if (o instanceof JsonNull)
		{
			return null;
		}
		if (!(isWrapped(o)))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		JsonList in_list = (JsonList) o;
		Queue<JsonElement> out_queue = new ArrayDeque<JsonElement>(in_list.size());
		for (JsonElement e : in_list)
		{
			out_queue.add(e);
		}
		return out_queue;
	}

	@Override
	protected Class<?> unwrapType(Object t) throws ReadException
	{
		if (!(t instanceof JsonMap))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		JsonMap m = (JsonMap) t;
		if (!m.containsKey(JsonSerializer.CLASS_KEY))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		try 
		{
			return Class.forName(m.getString(JsonSerializer.CLASS_KEY));
		} 
		catch (ClassNotFoundException e) 
		{
			throw new ReadException(e);
		}
	}
	
	@Override
	public Object readAs(Class<?> clazz, Object t) throws ReadException
	{
		if (!JsonElement.class.isAssignableFrom(clazz))
		{
			return super.readAs(clazz, t); 
		}
		// clazz is a JsonElement
		if (!(t instanceof JsonElement))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		return t;
	}

	@Override
	protected JsonElement unwrapContents(Object t)  throws ReadException
	{
		if (!(t instanceof JsonMap))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		JsonMap m = (JsonMap) t;
		if (!m.containsKey(JsonSerializer.CONTENT_KEY))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		return m.get(JsonSerializer.CONTENT_KEY);
	}

	@Override
	protected boolean isWrapped(Object t)
	{
		if (!(t instanceof JsonMap))
		{
			return false;
		}
		JsonMap m = (JsonMap) t;
		return m.containsKey(JsonSerializer.CONTENT_KEY) && 
				m.containsKey(JsonSerializer.CLASS_KEY);
	}
}
