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

import java.util.Queue;

import ca.uqac.lif.azrael.ReadException;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;

public class QueueReadHandler extends JsonReadHandler
{

	public QueueReadHandler(JsonReader reader)
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
		return Queue.class.isAssignableFrom(clazz);
	}

	@Override
	public Queue<?> handle(JsonElement o) throws ReadException
	{
		Class<?> clazz = m_reader.unwrapType(o);
		JsonList list = (JsonList) m_reader.unwrapContents(o);
		@SuppressWarnings("unchecked")
		Queue<Object> out_list = (Queue<Object>) m_reader.getInstance(clazz);
		for (JsonElement el : list)
		{
			Object o_el = m_reader.read(el);
			out_list.add(o_el);
		}
		return out_list;
	}

}
