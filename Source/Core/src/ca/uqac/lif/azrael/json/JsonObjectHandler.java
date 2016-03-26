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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import ca.uqac.lif.azrael.ObjectHandler;
import ca.uqac.lif.azrael.GenericSerializer;
import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;

public class JsonObjectHandler extends ObjectHandler<JsonElement>
{
	public JsonObjectHandler(GenericSerializer<JsonElement> s)
	{
		super(s);
	}

	@Override
	public JsonElement serializeObject(Object o, Map<String, JsonElement> contents)
	{
		JsonMap map = new JsonMap();
		for (String key : contents.keySet())
		{
			JsonElement value = contents.get(key);
			map.put(key, value);
		}
		return map;
	}

	@Override
	public Map<String, Object> deserializeContents(JsonElement e,
			Class<?> clazz) throws SerializerException
			{
		Map<String,Object> o_map = new HashMap<String,Object>();
		if (e == null || !(e instanceof JsonMap))
		{
			return o_map;
		}
		JsonMap map = (JsonMap) e;
		for (String attribute : map.keySet())
		{
			try
			{
				// Get the field associated with the map key and its declared type
				Field fld = GenericSerializer.getFromAllFields(attribute, clazz);
				fld.setAccessible(true);
				Class<?> field_type = fld.getType();
				// Get the map value, deserialize it and set the field to the result
				JsonElement value = map.get(attribute);
				Object value_o = m_serializer.deserializeAs(value, field_type);
				o_map.put(attribute, value_o);
			}
			catch (NoSuchFieldException ex)
			{
				throw new SerializerException(ex);
			}
		}
		return o_map;
			}

	@Override
	public boolean appliesTo(Class<?> clazz)
	{
		return true;
	}
}
