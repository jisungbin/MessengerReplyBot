package land.sungbin.replybot.engine

public interface ReplyEngine {
  public val isDeletedMessageSupported: Boolean

  public fun onNewMessage(message: String, profile: Profile, replier: Replier)
  public fun onDeletedMessage(message: String, profile: Profile, replier: Replier) {}
}