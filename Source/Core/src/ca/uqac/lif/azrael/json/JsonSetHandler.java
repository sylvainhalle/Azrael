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

import ca.uqac.lif.azrael.GenericSerializer;
import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.azrael.SetHandler;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;

public class JsonSetHandler extends SetHandler<JsonElement>
{
	public JsonSetHandler(GenericSerializer<JsonElement> s)
	{
		super(s);
	}

	@Override
	protected JsonElement getNewSet()
	{
		return new JsonList();
	}

	@Override
	protected JsonElement addToSet(JsonElement set, JsonElement element) throws SerializerException
	{
		if (set == null || !(set instanceof JsonList))
		{
			throw new SerializerException("Input must be a JSON list");
		}
		JsonList j_list = (JsonList) set;
		j_list.add(element);
		return j_list;
	}

	@Override
	protected Set<JsonElement> getElements(JsonElement e) throws SerializerException
	{
		JsonElement je = m_serializer.unwrapTypeInfo(e);
		if (je == null || !(je instanceof JsonList))
		{
			throw new SerializerException("Input must be a JSON list");
		}
		JsonList j_list = (JsonList) je;
		Set<JsonElement> elements = new HashSet<JsonElement>();
		for (JsonElement el : j_list)
		{
			elements.add(el);
		}
		return elements;
	}
}
