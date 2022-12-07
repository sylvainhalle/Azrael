package ca.uqac.lif.azrael;

public interface AzraelReader<T>
{
	/**
	 * Deserializes the content of an object.
	 * @param t The serialized contents of the object
	 * @return The deserialized object
	 * @throws ReadException Thrown if deserialization produced an error
	 */
	public Object read(T t) throws ReadException;
}
