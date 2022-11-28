/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2022 Sylvain Hallé
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

import ca.uqac.lif.azrael.ObjectReader;
import ca.uqac.lif.azrael.ReadException;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;

/**
 * Object reader that creates an object from a JSON element.
 * @author Sylvain Hallé
 */
public class JsonReader extends ObjectReader<JsonElement>
{
	/**
	 * Creates a new JSON reader
	 */
	public JsonReader()
	{
		super();
		m_handlers.add(new RawReadHandler(this));
		m_handlers.add(new NullReadHandler(this));
		m_handlers.add(new BooleanReadHandler(this));
		m_handlers.add(new NumberReadHandler(this));
		m_handlers.add(new StringReadHandler(this));
		m_handlers.add(new ListReadHandler(this));
		m_handlers.add(new EnumReadHandler(this));
		m_handlers.add(new QueueReadHandler(this));
		m_handlers.add(new SetReadHandler(this));
		m_handlers.add(new MapReadHandler(this));
		m_handlers.add(new ByteArrayReadHandler(this));
	}
	
	@Override
	protected String getWrappedTypeName(Object t) throws ReadException
	{
		if (!(t instanceof JsonMap))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		JsonMap m = (JsonMap) t;
		if (!m.containsKey(JsonPrinter.CLASS_KEY))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		return m.getString(JsonPrinter.CLASS_KEY);
	}

	@Override
	protected Class<?> unwrapType(Object t) throws ReadException
	{
		try 
		{
			return Class.forName(getWrappedTypeName(t));
		} 
		catch (ClassNotFoundException e) 
		{
			throw new ReadException(e);
		}
	}

	@Override
	protected JsonElement unwrapContents(Object t)  throws ReadException
	{
		if (!(t instanceof JsonMap))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		JsonMap m = (JsonMap) t;
		if (!m.containsKey(JsonPrinter.CONTENT_KEY))
		{
			throw new ReadException("Incompatible element to deserialize from");
		}
		return m.get(JsonPrinter.CONTENT_KEY);
	}

	@Override
	protected boolean isWrapped(Object t)
	{
		if (!(t instanceof JsonMap))
		{
			return false;
		}
		JsonMap m = (JsonMap) t;
		return m.containsKey(JsonPrinter.CONTENT_KEY) && 
				m.containsKey(JsonPrinter.CLASS_KEY);
	}
}
