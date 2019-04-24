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

import ca.uqac.lif.azrael.ObjectPrinter;
import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.json.JsonElement;

/**
 * Object printer that prints an object into a JSON <em>string</em>.
 * @author Sylvain Hallé
 */
public class JsonStringPrinter extends ObjectPrinter<String>
{
	/**
	 * The printer used to print objects to JSON
	 */
	protected JsonPrinter m_printer;
	
	/**
	 * Creates a new JSON string printer.
	 */
	public JsonStringPrinter()
	{
		super();
		m_printer = new JsonPrinter();
	}
	
	@Override
	public String print(Object o) throws PrintException
	{
		JsonElement je = m_printer.print(o);
		return je.toString();
	}

	@Override
	public String wrap(Object o, String t) throws PrintException
	{
		throw new UnsupportedOperationException("This printer cannot wrap objects");
	}
}