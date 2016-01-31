/*
    A serializer for Java objects
    Copyright (C) 2016 Sylvain Hallé
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

import java.util.HashSet;
import java.util.Set;

import ca.uqac.lif.azrael.MapHandler;
import ca.uqac.lif.azrael.Serializer;
import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.cornipickle.json.JsonElement;
import ca.uqac.lif.cornipickle.json.JsonList;
import ca.uqac.lif.cornipickle.json.JsonMap;

public class JsonMapHandler extends MapHandler<JsonElement>
{
	/**
	 * The special attribute used to represent the key in the JSON element
	 */
	protected static String s_keyAttribute = "!key";
	
	/**
	 * The special attribute used to represent the value in the JSON element
	 */
	protected static String s_valueAttribute = "!value";
	
	public JsonMapHandler(Serializer<JsonElement> s)
	{
		super(s);
	}

	@Override
	protected JsonElement getNewMap()
	{
		// Note that we instantiate a *list*, and not a map. The reason is
		// that in Java, any object can be a key to a map, while in JSON,
		// map keys must be strings. To resolve the issue, the Java map becomes
		// a JSON list, each element being a map with two keys: "key" and
		// "value", containing the serialized key and value, respectively.
		return new JsonList();
	}

	@Override
	protected JsonElement addToMap(JsonElement map, KeyValuePair<JsonElement,JsonElement> pair) throws SerializerException
	{
		if (map == null || !(map instanceof JsonList))
		{
			throw new SerializerException("Input must be a JSON list");
		}
		JsonList j_list = (JsonList) map;
		JsonMap j_pair = new JsonMap();
		j_pair.put(s_keyAttribute, pair.getKey());
		j_pair.put(s_valueAttribute, pair.getValue());
		j_list.add(j_pair);
		return j_list;
	}

	@Override
	protected Set<KeyValuePair<JsonElement,JsonElement>> getPairs(JsonElement e) throws SerializerException
	{
		if (e == null || !(e instanceof JsonList))
		{
			throw new SerializerException("Input must be a JSON list");
		}
		JsonList j_list = (JsonList) e;
		Set<KeyValuePair<JsonElement,JsonElement>> elements = new HashSet<KeyValuePair<JsonElement,JsonElement>>();
		for (JsonElement el : j_list)
		{
			if (el == null || !(el instanceof JsonMap))
			{
				throw new SerializerException("Elements of the serialized map must be maps");
			}
			JsonMap m = (JsonMap) el;
			if (!m.containsKey(s_keyAttribute) || !m.containsKey(s_valueAttribute))
			{
				throw new SerializerException("Elements of the serialized map must have a key and a value");
			}
			JsonElement j_key = m.get(s_keyAttribute);
			JsonElement j_value = m.get(s_valueAttribute);
			KeyValuePair<JsonElement,JsonElement> pair = new KeyValuePair<JsonElement,JsonElement>(j_key, j_value);
			elements.add(pair);
		}
		return elements;
	}
}
