package jt;

import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.io.*;
import java.util.TreeMap;

public class AccountManager implements Serializable
{
	public static final String KEY = "v1MQD1SYBNNLPJb67M4jog";

	public static final String SECRET = "zqlg5fZEUywQNHblnraEWkHrz1SEUinVUOJTQe7jMY";

	public static final String PATH = "./Accounts.dat";

	private AccountManager()
	{
	}

	private static final long serialVersionUID = 1111L;

	private static AccountManager instance;

	private TreeMap<String, AccessToken> accounts = new TreeMap<>();

	public String selected = "";

	public static void load()
	{
		try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(PATH)))
		{
			instance = (AccountManager) is.readObject();
			if (instance.accounts == null)
			{
				instance.accounts = new TreeMap<>();
			}
			if (instance.selected == null)
			{
				instance.selected = "";
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			instance = new AccountManager();
		}
	}

	public static void save()
	{
		try (ObjectOutputStream os = new ObjectOutputStream(
				new FileOutputStream("./accounts.dat")))
		{
			os.writeObject(instance);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static AccountManager getInstance()
	{
		return instance;
	}

	public void put(String screenName, AccessToken token)
	{
		accounts.put(screenName, token);
	}

	public AccessToken get(String screenName)
	{
		return accounts.get(screenName);
	}

	public boolean has(String screenName)
	{
		return accounts.containsKey(screenName);
	}

	public String[] getAccounts()
	{
		return accounts.keySet().toArray(new String[0]);
	}

	public Twitter getTwitter()
	{
		Twitter t = new TwitterFactory().getInstance();
		t.setOAuthConsumer(KEY, SECRET);
		if (has(selected))
		{
			t.setOAuthAccessToken(get(selected));
		}
		return t;
	}

	public AsyncTwitter getAsyncTwitter()
	{
		AsyncTwitter t = new AsyncTwitterFactory().getInstance();
		t.setOAuthConsumer(KEY, SECRET);
		if (has(selected))
		{
			t.setOAuthAccessToken(get(selected));
		}
		return t;
	}

	public TwitterStream getTwitterStream()
	{
		TwitterStream t = new TwitterStreamFactory().getInstance();
		t.setOAuthConsumer(KEY, SECRET);
		if (has(selected))
		{
			t.setOAuthAccessToken(get(selected));
		}
		return t;
	}

}