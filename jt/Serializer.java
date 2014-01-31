package jt;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer
{

	public static boolean write(Object o, String f)
	{
		try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(f)))
		{
			os.writeObject(o);
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static Object read(String f)
	{
		try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(f)))
		{
			return is.readObject();
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}