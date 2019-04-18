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
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonString;

public class JsonPrinter extends ObjectPrinter<JsonElement>
{
	public static final transient String CLASS_KEY = "!c";

	public static final transient String CONTENT_KEY = "!t";

	public JsonPrinter()
	{
		super();
		m_handlers.add(new NullPrintHandler(this));
		m_handlers.add(new BooleanPrintHandler(this));
		m_handlers.add(new NumberPrintHandler(this));
		m_handlers.add(new StringPrintHandler(this));
		m_handlers.add(new ListPrintHandler(this));
		m_handlers.add(new MapPrintHandler(this));
		m_handlers.add(new RawPrintHandler(this));
	}

	@Override
	public JsonElement wrap(Object o, JsonElement t) throws PrintException
	{
		JsonMap map = new JsonMap();
		map.put(CLASS_KEY, new JsonString(o.getClass().getName()));
		map.put(CONTENT_KEY, t);
		return map;
	}
}
