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

import ca.uqac.lif.azrael.ObjectPrinter;
import ca.uqac.lif.azrael.ObjectReader;
import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

/**
 * A fridge that sends the object to save into an object printer.
 * @author Sylvain Hallé
 */
public abstract class SerializeFridge<T> implements Fridge
{
  protected ObjectPrinter<T> m_printer;
  
  protected ObjectReader<T> m_reader;
  
  public SerializeFridge(ObjectPrinter<T> printer, ObjectReader<T> reader)
  {
    super();
    m_printer = printer;
    m_reader = reader;
  }
  
  @Override
  public void store(Object o) throws FridgeException
  {
    try
    {
      T s_o = m_printer.print(o);
      save(s_o);
    }
    catch (PrintException e)
    {
      throw new FridgeException(e);
    }
    m_printer.reset();
  }
  
  @Override
  public Object fetch() throws FridgeException
  {
    T s_o = load();
    try
    {
      return m_reader.read(s_o);
    }
    catch (ReadException e)
    {
      throw new FridgeException(e);
    }
  }
  
  /**
   * Saves a serialized object
   * @param t The object
   * @throws FridgeException Thrown if the object cannot be saved
   */
  public abstract void save(T t) throws FridgeException;
  
  /**
   * Loads a serialized object
   * @return t The object
   * @throws FridgeException Thrown if the object cannot be loaded
   */
  public abstract T load() throws FridgeException;
}
