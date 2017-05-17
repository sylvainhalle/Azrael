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

import ca.uqac.lif.azrael.BooleanHandler;
import ca.uqac.lif.azrael.EnumHandler;
import ca.uqac.lif.azrael.NullHandler;
import ca.uqac.lif.azrael.NumberHandler;
import ca.uqac.lif.azrael.ObjectHandler;
import ca.uqac.lif.azrael.GenericSerializer;
import ca.uqac.lif.azrael.StringHandler;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNull;
import ca.uqac.lif.json.JsonString;

public class JsonSerializer extends GenericSerializer<JsonElement>
{
	/**
	 * The special attribute name added to the serialization to record
	 * the precise class name of the serialized object
	 */
	protected static transient final String s_classAttribute = "!class";

	/**
	 * The special attribute name added to the serialization to
	 * enclose the wrapped object
	 */
	protected static transient final String s_objectAttribute = "!contents";
	
	public JsonSerializer()
	{
		super();
		addObjectHandler(0, new JsonListHandler(this));
		addObjectHandler(0, new JsonMapHandler(this));
		addObjectHandler(0, new JsonSetHandler(this));
		addObjectHandler(0, new JsonElementHandler(this));
		addClassLoader(JsonElement.class.getClassLoader());
	}

	@Override
	public JsonElement wrapTypeInfo(Object o, JsonElement e)
	{
		JsonMap out = new JsonMap();
		out.put(s_classAttribute, o.getClass().getName());
		out.put(s_objectAttribute, e);
		return out;
	}

	@Override
	public JsonElement unwrapTypeInfo(JsonElement e)
	{
		if (e == null || !(e instanceof JsonMap))
		{
			return e;
		}
		JsonMap map = (JsonMap) e;
		if (map.containsKey(s_classAttribute) && map.containsKey(s_objectAttribute))
		{
			return map.get(s_objectAttribute);
		}
		return map;
	}

	@Override
	protected StringHandler<JsonElement> getStringHandler()
	{
		return new JsonStringHandler(JsonSerializer.this);
	}

	@Override
	protected NumberHandler<JsonElement> getNumberHandler()
	{
		return new JsonNumberHandler(JsonSerializer.this);
	}
	
	@Override
	protected NullHandler<JsonElement> getNullHandler() 
	{
		return new JsonNullHandler(JsonSerializer.this);
	}

	@Override
	protected BooleanHandler<JsonElement> getBooleanHandler()
	{
		return new JsonBooleanHandler(JsonSerializer.this);
	}

	@Override
	protected ObjectHandler<JsonElement> getDefaultObjectHandler()
	{
		return new JsonObjectHandler(JsonSerializer.this);
	}
	
	@Override
	protected EnumHandler<JsonElement> getEnumHandler()
	{
		return new JsonEnumHandler(JsonSerializer.this);
	}

	@Override
	public ca.uqac.lif.azrael.GenericSerializer.TypeInfo<JsonElement> getTypeInfo(JsonElement in, Class<?> target_class) throws ClassNotFoundException
	{
		TypeInfo<JsonElement> info = new TypeInfo<JsonElement>();
		info.e = in;
		info.clazz = target_class;
		if (in instanceof JsonMap)
		{
			JsonMap map = (JsonMap) in;
			if (map.containsKey(s_classAttribute))
			{
				String class_name = ((JsonString) map.get(s_classAttribute)).stringValue();
				info.clazz = findClass(class_name);
			}
		}
		if (in instanceof JsonNull)
		{
			info.clazz = null;
		}
		return info;
	}
}
