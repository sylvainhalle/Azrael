package ca.uqac.lif.azrael.size;

import ca.uqac.lif.azrael.ObjectPrinter;
import ca.uqac.lif.azrael.PrintException;

public class SizePrinter extends ObjectPrinter<Number>
{
	public static final int OBJECT_SHELL_SIZE   = 8;
    public static final int OBJREF_SIZE         = 4;
    public static final int LONG_FIELD_SIZE     = 8;
    public static final int INT_FIELD_SIZE      = 4;
    public static final int SHORT_FIELD_SIZE    = 2;
    public static final int CHAR_FIELD_SIZE     = 2;
    public static final int BYTE_FIELD_SIZE     = 1;
    public static final int BOOLEAN_FIELD_SIZE  = 1;
    public static final int DOUBLE_FIELD_SIZE   = 8;
    public static final int FLOAT_FIELD_SIZE    = 4;
    
	public SizePrinter()
	{
		super();
		m_handlers.add(new NumberPrintHandler());
		m_handlers.add(new StringPrintHandler(this));
		m_handlers.add(new BooleanPrintHandler());
		m_handlers.add(new CollectionPrintHandler(this));
		m_handlers.add(new ArrayPrintHandler(this));
		m_reflectionHandler = new SizeReflectionHandler(this);
		m_usePrintable = false;
	}
	
	@Override
	public Number wrap(Object o, Number t) throws PrintException
	{
		return t;
	}
	
	/**
	 * Checks if an object is a primitive
	 * @param o The object
	 * @return <tt>true</tt> if the object is primitive, <tt>false</tt>
	 * otherwise
	 */
	public static boolean isPrimitive(Object o)
	{
		return o == null || o instanceof Boolean || o instanceof Number || o instanceof String;
	}
}
