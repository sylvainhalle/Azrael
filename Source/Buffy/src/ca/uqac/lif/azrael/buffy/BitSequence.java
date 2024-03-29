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

import java.util.Base64;
import java.util.Vector;

/**
 * Representation of a sequence of bits. This sequence can be converted
 * to/from an array of bytes.
 * 
 * @author Sylvain Hallé
 *
 */
public class BitSequence extends Vector<Boolean>
{

	/**
	 * Dummy UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Empty constructor. Constructs a bit sequence of length 0.
	 */
	public BitSequence()
	{
		super();
	}

	/**
	 * Constructs a bit sequence from an array of bytes. Since
	 * the array of bytes may represent a sequence of bits that is
	 * not a multiple of 8, the length of the bit sequence is also
	 * provided.
	 * 
	 * @param array The array of bytes
	 * @param length The length (in <em>bits</em> of the bit sequence
	 *   contained in the array of bytes
	 * @throws BitFormatException
	 */
	public BitSequence(byte[] array, int length) throws BitFormatException
	{
		this();
		readFromBytes(array, length);
	}
	
	public BitSequence(byte[] array) throws BitFormatException
	{
		this();
		readFromBytes(array, array.length * 8);
	}

	/**
	 * Reads an array of bytes. Since
	 * the array of bytes may represent a sequence of bits that is
	 * not a multiple of 8, the length of the bit sequence is also
	 * provided.
	 * @param array The array of bytes
	 * @param length The length (in <em>bits</em> of the bit sequence
	 *   contained in the array of bytes
	 * @throws BitFormatException
	 */
	protected void readFromBytes(byte[] array, int length) throws BitFormatException
	{
		int num_bytes = (int) Math.ceil(((float) length) / 8f);
		int cur_length = 0;
		if (num_bytes > array.length)
		{
			// Error: length is longer than the length of the array
			throw new BitFormatException();
		}
		for (int i = 0; i < num_bytes && cur_length < length; i++)
		{
			byte b = array[i];
			for (int j = 7; j >= 0 && cur_length < length; j--)
			{
				int val = (b >> j) & 1;
				if (val == 1)
				{
					this.add(true);
				}
				else
				{
					this.add(false);
				}
				cur_length++;
			}
		}    
	}

	/**
	 * Constructs a bit sequence from an integer value. The value is converted
	 * to a left-padded binary sequence
	 * @param value The value to initialise the bit sequence from
	 * @param length The length of the bit sequence
	 * @throws FormatException Thrown if length is too short to accommodate
	 *   the numerical value
	 */
	public BitSequence(long value, long length) throws BitFormatException
	{
		String binary = Long.toBinaryString(value);
		if (binary.length() > length)
		{
			// Error: length is too short to accommodate value
			throw new BitFormatException();
		}
		add(binary);
		for (int i = binary.length(); i < length; i++)
		{
			add(0, false); // left-pad to desired length
		}
	}

	/**
	 * Constructs a bit sequence from a string. All characters
	 * other than 0 or 1 are silently ignored.
	 * @param s The input string
	 */
	public BitSequence(String s)
	{
		this();
		add(s);
	}

	/**
	 * Appends a binary string to the current bit sequence. 
	 * @param s The binary string
	 */
	public void add(String s)
	{
		for (int i = 0; i < s.length(); i++)
		{
			String c = s.substring(i, i+1);
			if (c.compareTo("0") == 0)
			{
				this.add(false);
			}
			else if (c.compareTo("1") == 0)
			{
				this.add(true);
			}
		}
	}

	/**
	 * Displays the content of a byte as a string of 0 and 1
	 * @param b The byte to display
	 * @return
	 */
	public static String byteToString(byte b)
	{
		byte[] a = new byte[1];
		return bytesToString(a);
	}

	/**
	 * Displays the content of a byte array as a string of 0 and 1
	 * @param b The bytes to display
	 * @return
	 */
	public static String bytesToString(byte[] a)
	{
		if (a == null)
			return null;
		BitSequence ba = null;
		try
		{
			ba = new BitSequence(a, 8 * a.length);
		}
		catch (BitFormatException e)
		{
			return "";
		}
		return ba.toString();
	}

	/**
	 * Outputs the sequence of bits as an array of bytes.
	 * The last byte is padded with zeros if the number of bits
	 * in the array is not a multiple of 8.
	 * 
	 * @return The array of bytes
	 */
	public byte[] toByteArray()
	{
		int byte_size = (int) Math.ceil(((float) this.size()) / 8f);
		byte[] out = new byte[byte_size];
		int byte_pos = 0;
		for (int i = 0; i < this.size(); i += 8)
		{
			byte b = 0;
			for (byte j = 0; j < 8 && i + j < this.size(); j++)
			{
				if (this.elementAt(i + j) == true)
				{
					b |= 1 << (7 - j);
				}
			}
			out[byte_pos] = b;
			byte_pos++;
		}
		return out;
	}

	/**
	 * Returns the binary content of the sequence as a string.
	 * This method simply converts the result of {@link toByteArray}
	 * into a String object. 
	 * @return The binary content
	 */
	public String toByteString()
	{
		byte[] array = toByteArray();
		String out = new String(array);
		return out;
	}

	/**
	 * Converts a string into an array of bits. The method simply converts the
	 * contents of the string into an array of bytes, whose binary
	 * content is then read.
	 * @param s The string to read from
	 * @param length The number of bits
	 * @throws BitFormatException 
	 */
	public void fromByteString(String s, int length) throws BitFormatException
	{
		byte[] array = s.getBytes();
		this.clear();
		readFromBytes(array, length);
	}

	/**
	 * Converts a string into an array of bits. The method simply converts the
	 * contents of the string into an array of bytes, whose binary
	 * content is then read.
	 * @param s The string to read from
	 * @throws BitFormatException 
	 */
	public void fromByteString(String s) throws BitFormatException
	{
		byte[] array = s.getBytes();
		this.clear();
		readFromBytes(array, 8 * array.length);
	}

	/**
	 * Converts a string into an array of bits. The method simply converts the
	 * contents of the string into an array of bytes, whose binary
	 * content is then read.
	 * @param s The string to read from, encoded in Base64
	 * @throws BitFormatException 
	 * @throws Base64DecodingException 
	 */
	public void fromBase64(String s) throws BitFormatException
	{
		byte[] data = Base64.getDecoder().decode(s.getBytes());
		readFromBytes(data, data.length * 8);
	}

	/**
	 * Returns the binary content of the sequence as a Base64 string.
	 * @return The binary content in Base64
	 */
	public String toBase64()
	{
		byte[] data = toByteArray();
		String out = new String(Base64.getEncoder().encode(data));
		return out;
	}

	/**
	 * Returns the contents of a bit sequence as an integer value,
	 * with the most significant bit being the first of the sequence.
	 * 
	 * @return The integer value
	 */
	public long intValue()
	{
		long out = 0;
		long pos = 0;
		for (long i = (long) Math.pow(2, this.size() - 1); i >= 1; i /= 2)
		{
			boolean b = this.get((int) pos);
			if (b == true)
			{
				out += i;
			}
			pos++;
		}
		return out;
	}
	
	/**
	 * Returns the contents of a bit sequence as an integer value,
	 * with the most significant bit being the first of the sequence.
	 * 
	 * @return The integer value
	 */
	public long longValue()
	{
		return Long.parseLong(toString(), 2);
	}

	/**
	 * Creates a bit sequence from a part of the current bit sequence.
	 * @param start The start position in the sequence
	 * @param length The number of bits to retrieve from the start position
	 * @return The sub-sequence, or null if the bounds are invalid
	 */
	public BitSequence subSequence(long start, long length)
	{
		BitSequence bs = new BitSequence();
		if (start < 0 || start + length > this.size())
		{
			// Invalid bounds
			return null;
		}
		for (long i = start; i < start + length; i++)
		{
			if (this.get((int) i))
			{
				bs.add(true);
			}
			else
			{
				bs.add(false);
			}
		}
		return bs;
	}

	/**
	 * Truncates the bit sequence off the first n bits
	 * @param to The number of bits to remove from the beginning of the
	 *   sequence. If this value is greater than the length of the sequence,
	 *   the whole sequence is returned.
	 */
	public BitSequence truncatePrefix(long to)
	{
		BitSequence out = null;
		if (to <= 0)
		{
			// Nothing to do
			return this;
		}
		if (to >= this.size())
		{
			out = this.subSequence(0,  this.size());
			this.clear();
		}
		else
		{
			out = this.subSequence(0, to);
			this.removeRange(0, (int) to);      
		}
		return out;
	}

	@Override
	public int hashCode()
	{
		return size();
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof BitSequence))
		{
			return false;
		}
		BitSequence s = (BitSequence) o;
		if (s.size() != size())
		{
			return false;
		}
		for (int i = 0; i < size(); i++)
		{
			if (s.get(i) != get(i))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString()
	{
		return toString(0);
	}

	public String toString(int group)
	{
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < this.size(); i++)
		{
			if (i > 1 && group > 0 && (i % group) == 0)
			{
				out.append(" ");
			}
			boolean b = this.get(i);
			if (b == true)
			{
				out.append("1");
			}
			else
			{
				out.append("0");
			}
		}
		return out.toString();
	}

	public static void main(String[] args)
	{
		BitSequence s1 = null;
		try
		{
			s1 = new BitSequence(14, 4);
		}
		catch (BitFormatException e)
		{
			e.printStackTrace();
		}
		System.out.println(s1);
		System.out.println(s1.intValue());
		byte[] b = s1.toByteArray();
		try
		{
			s1 = new BitSequence(b, 4);
		}
		catch (BitFormatException e)
		{
			e.printStackTrace();
		}  
		System.out.println(s1);
	}
}
