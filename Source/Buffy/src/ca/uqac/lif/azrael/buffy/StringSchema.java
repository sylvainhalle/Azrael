package ca.uqac.lif.azrael.buffy;

import ca.uqac.lif.azrael.PrintException;
import ca.uqac.lif.azrael.ReadException;

public abstract class StringSchema implements Schema
{
	@Override
	public abstract BitSequence print(Object o) throws PrintException;
	
	@Override
	public abstract String read(BitSequence s) throws ReadException;
}
