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
package ca.uqac.lif.azrael.clone;

/**
 * A special object that indicates that the object being serialized implements
 * the {@link Readable} interface.
 * @author Sylvain Hallé
 */
public class WrappedObject
{
  /**
   * The class of the original object
   */
  public Class<?> m_class;
  
  /**
   * The object that is wrapped
   */
  public Object m_innerObject;
  
  /**
   * Wraps an object
   * @param o The object
   * @param printed The printed version of the object
   */
  public WrappedObject(Object o, Object printed)
  {
    super();
    m_class = o.getClass();
    m_innerObject = printed;
  }
  
  /**
   * Gets the wrapped object
   * @return The object
   */
  public Object getInnerObject()
  {
    return m_innerObject;
  }
  
  /**
   * Gets the class of the original object
   * @return The class
   */
  public Class<?> getInnerClass()
  {
    return m_class;
  }
}
