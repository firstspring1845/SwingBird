package jt;

public enum EventType
{
	RT("RT", "リツイートされました"),
	FAV("FAV", "ふぁぼられました"),
	UNFAV("UNFAV", "あんふぁぼされました"),
	FOLLOW("FOLLOW", "フォローされました");

	public final String type;
	public final String message;

	private EventType(String type, String message)
	{
		this.type = type;
		this.message = message;
	}

}
