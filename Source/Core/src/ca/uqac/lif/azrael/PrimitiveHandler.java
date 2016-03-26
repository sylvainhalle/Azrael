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
package ca.uqac.lif.azrael;

public abstract class PrimitiveHandler<T> extends Handler<T>
{

  public PrimitiveHandler(GenericSerializer<T> s)
  {
    super(s);
  }
  
  /**
   * Checks if a given object is "primitive"
   * @param o The object
   * @return true if the object is primitive, false otherwise
   */
  public static final boolean isPrimitive(Object o)
  {
	  return o instanceof Number || o instanceof String ||
			  o instanceof Boolean;
  }

}
