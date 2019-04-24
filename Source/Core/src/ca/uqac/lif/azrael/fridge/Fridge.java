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
package ca.uqac.lif.azrael.fridge;

/**
 * Implementation of the <a href="https://en.wikipedia.org/wiki/Memento_pattern">memento
 * pattern</a>. A fridge can be used to store an object, and retrieve this object at a
 * later time.
 * @author Sylvain Hallé
 *
 */
public interface Fridge
{
  /**
   * Stores an object
   * @param o The object
   * @throws FridgeException Thrown if the store operation cannot be performed
   */
  public void store(Object o) throws FridgeException;
  
  /**
   * Fetches an object
   * @return The object
   * @throws FridgeException Thrown if the fetch operation cannot be performed
   */
  public Object fetch() throws FridgeException;
}
