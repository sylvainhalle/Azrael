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
import ca.uqac.lif.json.JsonParser;
import ca.uqac.lif.json.JsonParser.JsonParseException;

/**
 * Object reader that reads a JSON <em>string</em> and recreates an object
 * from it.
 * @author Sylvain Hallé
 */
public class JsonStringReader extends ObjectReader<String>
{
	/**
	 * Reads an object from a JSON string using default settings.
	 * @param s The JSON string to read from
	 * @return The reconstructed object
	 * @throws ReadException Thrown if the object cannot be read from a
	 * JSON string
	 */
	public static Object fromJson(String s) throws ReadException
	{
		JsonStringReader jsr = new JsonStringReader();
		return jsr.read(s);
	}
	
	/**
	 * The reader used to read the JSON element
	 */
	JsonReader m_reader;
	
	/**
	 * The parser used to parse the JSON string
	 */
	JsonParser m_parser;
	
	/**
	 * Creates a new JSON string reader
	 */
	public JsonStringReader()
	{
		super();
		m_reader = new JsonReader();
		m_parser = new JsonParser();
	}
	
	@Override
	public Object read(Object o) throws ReadException
	{
		String s = (String) o;
		try 
		{
			JsonElement e = m_parser.parse(s);
			return m_reader.read(e);
		}
		catch (JsonParseException e) 
		{
			throw new ReadException(e);
		}
	}
	
	@Override
	protected String getWrappedTypeName(Object t) throws ReadException 
	{
		return m_reader.getWrappedTypeName(t);
	}

	@Override
	protected Class<?> unwrapType(Object t) throws ReadException 
	{
		return m_reader.unwrapType(t);
	}

	@Override
	protected Object unwrapContents(Object t) throws ReadException 
	{
		return m_reader.unwrapContents(t);
	}

	@Override
	protected boolean isWrapped(Object t) 
	{
		return m_reader.isWrapped(t);
	}
}
