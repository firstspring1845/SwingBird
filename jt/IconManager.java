package jt;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;

public class IconManager
{
	private static Thread getterThread;

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static Map<String, ImageIcon> cache = Collections.synchronizedMap(new HashMap());
	public static ImageIcon loadingImage = new ImageIcon("defaulticon.png");

	public static ImageIcon getImage(String url)
	{
		Scanner s = new Scanner(url);
		s.useDelimiter(Pattern.compile(".*/"));
		String fileName = s.next();
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
		getter.addURL(url);
		return loadingImage;
	}

	static class getter implements Runnable
	{
		static Queue<URL> q = new ConcurrentLinkedQueue<>();

		static void addURL(String str)
		{
			try
			{
				URL u = new URL(str);
				if (!q.contains(u))
				{
					q.add(u);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			if (getterThread == null || !getterThread.isAlive())
			{
				getterThread = new Thread(new getter());
				getterThread.start();
			}
		}

		@Override
		public void run()
		{
			while (!q.isEmpty())
			{
				try
				{
					URL u = q.peek();
					Scanner s = new Scanner(u.toString());
					s.useDelimiter(Pattern.compile(".*/"));
					String fileName = s.next();
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
					q.poll();
					Main.refresh();
				} catch (Exception e)
				{
					q.poll();
					System.out.println("���������������ł�");
					e.printStackTrace();
				}
			}
		}
	}

}
