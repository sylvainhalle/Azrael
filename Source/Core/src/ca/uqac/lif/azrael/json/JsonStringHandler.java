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

import ca.uqac.lif.azrael.Serializer;
import ca.uqac.lif.azrael.StringHandler;
import ca.uqac.lif.cornipickle.json.JsonElement;
import ca.uqac.lif.cornipickle.json.JsonString;

/**
 * A JSON handler for string data
 * @author sylvain
 *
 */
class JsonStringHandler extends StringHandler<JsonElement>
{
  public JsonStringHandler(Serializer<JsonElement> s)
  {
    super(s);
  }
  
  @Override
  public JsonString serializeAs(Object o, Class<?> clazz)
  {
    if (o == null || !(o instanceof String))
    {
      return null;
    }
    return new JsonString(o.toString());
  }

  @Override
  public String deserializeAs(JsonElement e, Class<?> clazz)
  {
    if (e != null && e instanceof JsonString)
    {
      return ((JsonString) e).stringValue();
    }
    return null;
  }

}
