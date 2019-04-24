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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Fridge that stores an object as a serialized string into a text file. 
 * @author Sylvain Hallé
 */
public class FileFridge extends SerializeFridge<String>
{
  /**
   * The name of the file where the object will be stored
   */
  protected String m_filename;
  
  /**
   * Creates a new file fridge.
   * @param printer The printer used to serialize the objects
   * @param reader The reader used to deserialize the objects
   * @param filename The name of the file where the object will be stored
   */
  public FileFridge(/*@ non_null @*/ ObjectPrinter<String> printer, 
      /*@ non_null @*/ ObjectReader<String> reader, /*@ non_null @*/ String filename)
  {
    super(printer, reader);
    m_filename = filename;
  }
  
  @Override
  public void save(String s) throws FridgeException
  {
    try
    {
      PrintStream ps = new PrintStream(new File(m_filename));
      ps.print(s);
      ps.close();
    }
    catch (FileNotFoundException e)
    {
      throw new FridgeException(e);
    }
  }

  @Override
  public String load() throws FridgeException
  {
    StringBuilder builder = new StringBuilder();
    try
    {
      Scanner scanner = new Scanner(new File(m_filename));
      while (scanner.hasNextLine())
      {
        builder.append(scanner.nextLine()).append("\n");
      }
      String o_s = builder.toString();
      scanner.close();
      return o_s;
    }
    catch (FileNotFoundException e)
    {
      throw new FridgeException(e);
    }
  }
}
