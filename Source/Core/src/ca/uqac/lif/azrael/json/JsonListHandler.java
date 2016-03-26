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

import java.util.LinkedList;
import java.util.List;

import ca.uqac.lif.azrael.ListHandler;
import ca.uqac.lif.azrael.Serializer;
import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;

public class JsonListHandler extends ListHandler<JsonElement>
{
	public JsonListHandler(Serializer<JsonElement> s)
	{
		super(s);
	}

	@Override
	protected JsonElement getNewList()
	{
		return new JsonList();
	}

	@Override
	protected JsonElement addToList(JsonElement list, JsonElement element) throws SerializerException
	{
		if (list == null || !(list instanceof JsonList))
		{
			throw new SerializerException("Input must be a JSON list");
		}
		JsonList j_list = (JsonList) list;
		j_list.add(element);
		return j_list;
	}

	@Override
	protected List<JsonElement> getElements(JsonElement e) throws SerializerException
	{
		JsonElement je = m_serializer.unwrapTypeInfo(e);
		if (je == null || !(je instanceof JsonList))
		{
			throw new SerializerException("Input must be a JSON list");
		}
		JsonList j_list = (JsonList) je;
		List<JsonElement> elements = new LinkedList<JsonElement>();
		for (JsonElement el : j_list)
		{
			elements.add(el);
		}
		return elements;
	}
}
