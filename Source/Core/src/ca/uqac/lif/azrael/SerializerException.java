/*
    A serializer for Java objects
    Copyright (C) 2016-2017 Sylvain Hallé
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

public class SerializerException extends Exception
{

  /**
   * Dummy UID
   */
  private static final long serialVersionUID = 1L;

  public SerializerException()
  {
    // TODO Auto-generated constructor stub
  }

  public SerializerException(String message)
  {
    super(message);
    // TODO Auto-generated constructor stub
  }

  public SerializerException(Throwable cause)
  {
    super(cause);
    // TODO Auto-generated constructor stub
  }

  public SerializerException(String message, Throwable cause)
  {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }
}
