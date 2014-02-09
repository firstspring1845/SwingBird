package jt;

import twitter4j.*;

public interface ActivityListener
{
	public void onFavorite(User source, User target, Status status);

	public void onUnfavorite(User source, User target, Status status);

	public void onFollow(User source, User target);
}
