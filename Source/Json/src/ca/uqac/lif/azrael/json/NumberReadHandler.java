/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2021 Sylvain Hallé
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

import ca.uqac.lif.azrael.ReadException;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonString;

public class NumberReadHandler extends JsonReadHandler
{
	public NumberReadHandler(JsonReader reader)
	{
		super(reader);
	}

	@Override
	public boolean canHandle(JsonElement o) 
	{
		if (!(o instanceof JsonString))
		{
			return false;
		}
		return ((JsonString) o).stringValue().matches("[\\d]*\\.[\\d]+[FDIL]|[\\d]+[FDIL]");
	}

	@Override
	public Number handle(JsonElement o) throws ReadException
	{
	  if (!(o instanceof JsonString))
	  {
	    throw new ReadException("Expected a JsonString, got a " + o.getClass().getSimpleName());
	  }
	  JsonString js = (JsonString) o;
	  String beginning = js.stringValue().substring(0, js.stringValue().length() - 1);
	  String type = js.stringValue().substring(js.stringValue().length() - 1);
	  if (type.compareTo("L") == 0)
	  {
	    return Long.parseLong(beginning);
	  }
	  if (type.compareTo("I") == 0)
    {
      return Integer.parseInt(beginning);
    }
	  if (type.compareTo("F") == 0)
    {
      return Float.parseFloat(beginning);
    }
	  if (type.compareTo("D") == 0)
    {
      return Double.parseDouble(beginning);
    }
	  throw new ReadException("Invalid number type");
	}

}
