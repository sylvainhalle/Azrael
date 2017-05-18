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
package ca.uqac.lif.azrael.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.azrael.json.JsonSerializer;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonString;

public class SerializationTest
{
	protected JsonSerializer m_serializer;

	@Before
	public void setup()
	{
		// Ensures a blank serializer is given to each test case
		m_serializer = new JsonSerializer();
	}

	@Test
	public void testPrimitiveString1() throws SerializerException
	{
		String obj_a = "abc";
		JsonElement e = m_serializer.serialize(obj_a);
		assertNotNull(e);
		assertTrue("Serialized element should be a String", e instanceof JsonString);
		JsonString e_map = (JsonString) e;
		assertEquals(3, e_map.stringValue().length());
	}

	@Test
	public void testPrimitiveString2() throws SerializerException
	{
		String obj_a = "";
		JsonElement e = m_serializer.serialize(obj_a);
		assertNotNull(e);
		assertTrue("Serialized element should be a String", e instanceof JsonString);
		JsonString e_map = (JsonString) e;
		assertEquals(0, e_map.stringValue().length());
	}

	@Test
	public void testPrimitiveBoolean1() throws SerializerException
	{
		Boolean obj_a = true;
		JsonElement e = m_serializer.serialize(obj_a);
		assertNotNull(e);
		assertTrue("Serialized element should be a String", e instanceof JsonString);
		JsonString e_map = (JsonString) e;
		assertTrue("Value should be 'true'", e_map.stringValue().compareTo("true") == 0);
	}

	@Test
	public void testPrimitiveBoolean2() throws SerializerException
	{
		Boolean obj_a = false;
		JsonElement e = m_serializer.serialize(obj_a);
		assertNotNull(e);
		assertTrue("Serialized element should be a String", e instanceof JsonString);
		JsonString e_map = (JsonString) e;
		assertTrue("Value should be 'true'", e_map.stringValue().compareTo("false") == 0);
	}

	@Test
	public void testPrimitiveBoolean3() throws SerializerException
	{
		boolean obj_a = true;
		JsonElement e = m_serializer.serialize(obj_a);
		assertNotNull(e);
		assertTrue("Serialized element should be a String", e instanceof JsonString);
		JsonString e_map = (JsonString) e;
		assertTrue("Value should be 'true'", e_map.stringValue().compareTo("true") == 0);
	}

	@Test
	public void testPrimitiveBoolean4() throws SerializerException
	{
		boolean obj_a = false;
		JsonElement e = m_serializer.serialize(obj_a);
		assertNotNull(e);
		assertTrue("Serialized element should be a String", e instanceof JsonString);
		JsonString e_map = (JsonString) e;
		assertTrue("Value should be 'true'", e_map.stringValue().compareTo("false") == 0);
	}

	@Test
	public void testPrimitiveNumber1() throws SerializerException
	{
		Integer obj_a = 10;
		JsonElement e = m_serializer.serialize(obj_a);
		assertNotNull(e);
		assertTrue("Serialized element should be a JsonNumber", e instanceof JsonNumber);
		JsonNumber e_map = (JsonNumber) e;
		assertEquals(10, e_map.numberValue());
	}

	@Test
	public void testPrimitiveNumber2() throws SerializerException
	{
		int obj_a = 10;
		JsonElement e = m_serializer.serialize(obj_a);
		assertNotNull(e);
		assertTrue("Serialized element should be a JsonNumber", e instanceof JsonNumber);
		JsonNumber e_map = (JsonNumber) e;
		assertEquals(10, e_map.numberValue());
	}

	@Test
	public void testList1() throws SerializerException
	{
		List<Integer> obj_a = new LinkedList<Integer>();
		obj_a.add(1);
		obj_a.add(2);
		obj_a.add(3);
		JsonElement e = m_serializer.serialize(obj_a);
		assertNotNull(e);
		assertTrue("Serialized element should be a list, got a " + e.getClass().getSimpleName(), e instanceof JsonList);
		JsonList e_list = (JsonList) e;
		assertEquals(3, e_list.size());
		Object o_des = m_serializer.deserializeAs(e, LinkedList.class);
		assertNotNull(o_des);
		assertTrue("Object should be an instance of LinkedList, got " + o_des.getClass().getSimpleName(), o_des instanceof LinkedList);
	}
	
	@Test
	public void testJsonList1() throws SerializerException
	{
		List<JsonElement> obj_a = new LinkedList<JsonElement>();
		obj_a.add(new JsonString("1"));
		obj_a.add(new JsonString("2"));
		obj_a.add(new JsonString("3"));
		JsonElement e = m_serializer.serialize(obj_a);
		assertNotNull(e);
		assertTrue("Serialized element should be a list, got a " + e.getClass().getSimpleName(), e instanceof JsonList);
		JsonList e_list = (JsonList) e;
		assertEquals(3, e_list.size());
		Object o_des = m_serializer.deserializeAs(e, LinkedList.class);
		assertNotNull(o_des);
		assertTrue("Object should be an instance of LinkedList, got " + o_des.getClass().getSimpleName(), o_des instanceof LinkedList);
	}
	
	@Test
	public void testSet1() throws SerializerException
	{
		Set<Integer> obj_a = new HashSet<Integer>();
		obj_a.add(1);
		obj_a.add(2);
		obj_a.add(3);
		JsonElement e = m_serializer.serialize(obj_a);
		assertNotNull(e);
		assertTrue("Serialized element should be a list, got a " + e.getClass().getSimpleName(), e instanceof JsonList);
		JsonList e_list = (JsonList) e;
		assertEquals(3, e_list.size());
		Object o_des = m_serializer.deserializeAs(e, HashSet.class);
		assertNotNull(o_des);
		assertTrue("Object should be an instance of HashSet, got " + o_des.getClass().getSimpleName(), o_des instanceof HashSet);
	}

	@Test
	public void testMap1() throws SerializerException
	{
		Map<String,Integer> obj_a = new HashMap<String,Integer>();
		obj_a.put("a", 1);
		obj_a.put("b", 2);
		obj_a.put("c", 3);
		JsonElement e = m_serializer.serialize(obj_a);
		assertNotNull(e);
		assertTrue("Serialized element should be a list, got a " + e.getClass().getSimpleName(), e instanceof JsonList);
		JsonList e_list = (JsonList) e;
		assertEquals(3, e_list.size());
		Object o_des = m_serializer.deserializeAs(e, HashMap.class);
		assertNotNull(o_des);
		assertTrue("Object should be an instance of HashMap, got " + o_des.getClass().getSimpleName(), o_des instanceof HashMap);
		@SuppressWarnings("unchecked")
		HashMap<String,Integer> o_map = (HashMap<String,Integer>) o_des;
		assertEquals(1, (int) o_map.get("a"));
	}
	
	@Test
	public void testJson1() throws SerializerException
	{
		JsonMap jm = new JsonMap();
		jm.put("a", 1);
		JsonList jl = new JsonList();
		jl.add("b");
		jm.put("z", jl);
		JsonElement e = m_serializer.serialize(jm);
		assertNotNull(e);
		assertTrue("Serialized element should be a map, got a " + e.getClass().getSimpleName(), e instanceof JsonMap);
		Object o_des = m_serializer.deserializeAs(e, JsonMap.class);
		assertNotNull(o_des);
		assertTrue("Object should be an instance of JsonMap, got " + o_des.getClass().getSimpleName(), o_des instanceof JsonMap);
		JsonMap o_map = (JsonMap) o_des;
		assertEquals(1, (int) o_map.getNumber("a").intValue());
	}

	@Test
	public void testCompound1() throws SerializerException
	{
		ClassA obj_a = new ClassA();
		JsonElement e = m_serializer.serialize(obj_a);
		assertNotNull(e);
		assertTrue("Serialized element should be a map", e instanceof JsonMap);
		JsonMap e_map = (JsonMap) e;
		assertEquals(2, e_map.keySet().size());
		Object o_des = m_serializer.deserializeAs(e, ClassA.class);
		assertNotNull(o_des);
		assertTrue("Object should be an instance of ClassA, got " + o_des.getClass().getSimpleName(), o_des instanceof ClassA);
	}

	@Test
	public void testNoInstance1() throws SerializerException
	{
		m_serializer.addClassLoader(InterfaceZ.class.getClassLoader());
		InterfaceZ obj_a = new ClassY(1);
		JsonElement e = m_serializer.serializeAs(obj_a, InterfaceZ.class);
		assertNotNull(e);
		assertTrue("Serialized element should be a map", e instanceof JsonMap);
		JsonMap e_map = (JsonMap) e;
		assertEquals(2, e_map.keySet().size());
		Object o_des = m_serializer.deserializeAs(e, InterfaceZ.class);
		assertNotNull(o_des);
		assertTrue("Object should be an instance of ClassY, got " + o_des.getClass().getSimpleName(), o_des instanceof ClassY);
		assertEquals(1, ((ClassY) o_des).x);
	}

	@Test
	public void testEnum1() throws SerializerException
	{
		ClassB obj_b = new ClassB();
		JsonElement e = m_serializer.serialize(obj_b);
		assertNotNull(e);
		assertTrue("Serialized element should be a map", e instanceof JsonMap);
		Object o_des = m_serializer.deserializeAs(e, ClassB.class);
		assertNotNull(o_des);
		assertTrue("Object should be an instance of ClassB, got " + o_des.getClass().getSimpleName(), o_des instanceof ClassB);

	}

}
