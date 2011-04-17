package graphicsEngine;

/**
 * Byte loader class by Odin Jensen.
 * Used for loading files from other languages where the byte order is different.
 */

public class ByteLoader
{
    // Contents of file
    private byte[] data;
    
    // File pointer
    private int filePos = 0;
    
    /**
     * Create new byte loader
     * @param data Data pointer
     */
    public ByteLoader(byte[] data)
    {
        this.data = data;
    }
    
    /**
     * Set file position.
     * @param filePos New file position
     */
    public void setFileOffset(int filePos)
    {
        this.filePos = filePos;
    }
    
    /**
     * Add to file position.
     * @param add Value to add
     */
    public void addToFileOffset(int add)
    {
        this.filePos += add;
    }
    
    /**
     * Read next byte from file.
     * @return next byte in file
     */
    public int readByte()
    {
	int b1 = (data[filePos] & 0xFF);
	filePos += 1;
	return b1;
    }

    /**
     * Read next short from file.
     * @return next short in file
     */
    public int readShort()
    {
	int s1 = (data[filePos] & 0xFF);
	int s2 = (data[filePos+1] & 0xFF) << 8;
	filePos += 2;
	return (s1 | s2);
    }

    /**
     * Read next int from file.
     * @return next int in file
     */
    public int readInt()
    {
	int i1 = (data[filePos ] & 0xFF);
	int i2 = (data[filePos+1] & 0xFF) <<  8;
	int i3 = (data[filePos+2] & 0xFF) << 16;
	int i4 = (data[filePos+3] & 0xFF) << 24;
	filePos += 4;
	return (i1 | i2 | i3 | i4);
    }

    /**
     * Read next float from file.
     * @return next float in file
     */
    public float readFloat()
    {
	return Float.intBitsToFloat(readInt());
    }

    /**
     * Read next string from file.
     * @param size of string
     * @return next String in file
     */
    public String readString(int size)
    {
	// Look for zero terminated string from byte array
	for (int i = filePos; i < filePos + size ; i++ )
	{
		if (data[i] == (byte)0)
		{
			return new String(data, filePos, i - filePos);
		}
	}
	return new String(data, filePos, size);
    }       
}