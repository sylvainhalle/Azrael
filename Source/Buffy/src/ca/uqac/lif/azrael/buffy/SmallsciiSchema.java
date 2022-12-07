/*
    Azrael, a serializer for Java objects
    Copyright (C) 2016-2022 Sylvain Hallé
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
package ca.uqac.lif.azrael.buffy;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

public class SmallsciiSchema extends StringSchema
{
	public static final transient SmallsciiSchema instance = new SmallsciiSchema();
	
	protected static final transient String s_characters = "abcdefghijklmnopqrstuvwxyz0123456789.,!@#$%&*^()[]-+=<> |/\\'{}:";

	protected SmallsciiSchema()
	{
		super();
	}

	@Override
	public int hashCode()
	{
		return 0;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof SmallsciiSchema;
	}

	@Override
	public String toString()
	{
		return "S";
	}

	@Override
	public BitSequence print(Object o) throws PrintException
	{
		if (!(o instanceof String))
		{
			throw new PrintException("Expected a String");
		}
		String bs = (String) o;
		BitSequence s = new BitSequence();
		for (int i = 0; i < bs.length(); i++)
		{
			String letter = bs.substring(i, i+1);
			int code = getCode(letter);
			if (code < 0)
			{
				throw new PrintException(new InvalidCharacterException("Invalid character: " + letter));
			}
			try
			{
				BitSequence n_bs = new BitSequence(code, 6);
				s.addAll(n_bs);
			}
			catch (BitFormatException e)
			{
				// Do nothing
			}
		}
		try
		{
			// Add null value to mark end of string
			s.addAll(new BitSequence(0, 6));
		}
		catch (BitFormatException e)
		{
			throw new PrintException(e);
		}
		return s;
	}

	@Override
	public String read(BitSequence s) throws ReadException
	{
		StringBuilder sb = new StringBuilder();
		while (s.size() >= 6)
		{
			BitSequence subs = s.truncatePrefix(6);
			long code = subs.intValue();
			if (code == 0)
			{
				break;
			}
			String letter = getSymbol(code);
			sb.append(letter);
		}
		return sb.toString();
	}
	
	/**
   * Returns the Smallscii code of the first symbol of the given string
   * @param s The string to look for
   * @return The code value for that symbol; -1 if outside of character set
   */
  public static int getCode(String s)
  {
    String letter = s.substring(0, 1);
    int index = s_characters.indexOf(letter);
    if (index < 0)
    {
    	return -1;
    }
    return index  + 1;
  }
  
  /**
   * Returns the symbol for a give Smallscii code
   * @param code The code to look for
   * @return The corresponding symbol, empty string if outside of character set
   */
  public static String getSymbol(long code)
  {
    if (code < 1 || code > 63)
      return "";
    return s_characters.substring((int) (code - 1), (int) code);
  }
	
	public static class InvalidCharacterException extends RuntimeException
	{
		/**
		 * Dummy UID.
		 */
		private static final long serialVersionUID = 1L;
		
		public InvalidCharacterException(String c)
		{
			super("Invalid Buffscii character: " + c);
		}
	}
}