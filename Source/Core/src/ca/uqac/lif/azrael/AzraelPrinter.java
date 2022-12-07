package ca.uqac.lif.azrael;

public interface AzraelPrinter<T>
{
	/**
	 * Serializes the contents of an object.
	 * @param o The object
	 * @return The serialized contents
	 * @throws PrintException Thrown if an error occurs during the
	 * serialization
	 */
	public T print(Object o) throws PrintException;
}
