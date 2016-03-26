package ca.uqac.lif.azrael;

public interface Serializer<T>
{
	public T serialize(Object o) throws SerializerException;
	
	public T serializeAs(Object o, Class<?> clazz) throws SerializerException;
	
	public Object deserializeAs(T e, Class<?> clazz) throws SerializerException;
}
