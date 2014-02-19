package jt;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class IconCache implements Runnable
{
	static
	{
		new Thread(new IconCache()).start();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static Map<String, ImageIcon> cache = Collections.synchronizedMap(new HashMap());
	private static BlockingQueue<URL> q = new LinkedBlockingQueue<>();
	public static final ImageIcon loadingImage = new ImageIcon("defaulticon.png");
	private static List<Runnable> callbacks = new LinkedList<>();

	public static void addCallback(Runnable r)
	{
		callbacks.add(r);
	}

	public static ImageIcon getImage(String url)
	{
		String fileName = url.replaceAll(".*/", "");
		if (cache.containsKey(fileName))
		{
			return cache.get(fileName);
		}
		File localFile = new File("cache/" + fileName);
		if (localFile.exists())
		{
			ImageIcon i = new ImageIcon("cache/" + fileName);
			cache.put(fileName, i);
			return i;
		}
		try
		{
			q.add(new URL(url));
		} catch (MalformedURLException e)
		{

		}
		return loadingImage;
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				URL u = q.take();
				String fileName = u.toString().replaceAll(".*/","");
				if(new File("cache/" + fileName).exists())
				{
					continue;
				}
				HttpURLConnection c = (HttpURLConnection) u.openConnection();
				c.setRequestMethod("GET");
				c.connect();
				System.out.println("GET");
				InputStream is = new BufferedInputStream(c.getInputStream());
				byte[] arr = new byte[c.getContentLength()];
				int i = 0;
				while (i < arr.length)
				{
					int av = is.available();
					if (av == 0)
					{
						continue;
					} else
					{
						is.read(arr, i, av);
						i += av;
					}
				}
				System.out.println(arr.length);
				new File("cache").mkdir();
				OutputStream os = new FileOutputStream("cache/" + fileName);
				os.write(arr);
				os.close();
				for(Runnable r : callbacks)
				{
					r.run();
				}
			} catch (Exception e)
			{
				System.out.println("ほげ～");
				e.printStackTrace();
			}
		}
	}
}


