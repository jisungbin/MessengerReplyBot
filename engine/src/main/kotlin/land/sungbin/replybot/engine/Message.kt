package land.sungbin.replybot.engine

public data class Message(
  public val content: String,
  public val room: String,
  public val roomId: String,
  public val sender: String,
  public val senderId: String,
  public val isGroupChat: String,
)