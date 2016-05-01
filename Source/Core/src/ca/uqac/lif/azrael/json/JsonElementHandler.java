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

import ca.uqac.lif.azrael.GenericSerializer;
import ca.uqac.lif.azrael.Handler;
import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.json.JsonElement;

/**
 * Element handler for JSON elements. This is to make sure that when the
 * object to serialize as a JSON element is already a JSON element, it
 * is simply output as is (and ditto for the deserialization). 
 */
public class JsonElementHandler extends Handler<JsonElement>
{
	public JsonElementHandler(GenericSerializer<JsonElement> s)
	{
		super(s);
	}

	@Override
	public boolean appliesTo(Class<?> clazz)
	{
		if (clazz == null)
		{
			return false;
		}
		return JsonElement.class.isAssignableFrom(clazz);
	}

	@Override
	public JsonElement serializeAs(Object o, Class<?> clazz) throws SerializerException
	{
		if (o == null || !(o instanceof JsonElement))
		{
			throw new SerializerException("Input must be a JSON element");
		}
		return (JsonElement) o;
	}

	@Override
	public Object deserializeAs(JsonElement e, Class<?> clazz) throws SerializerException
	{
		if (clazz == null || !JsonElement.class.isAssignableFrom(clazz))
		{
			throw new SerializerException("The class to deserialize must be a JSON element");
		}
		return e;
	}

}
