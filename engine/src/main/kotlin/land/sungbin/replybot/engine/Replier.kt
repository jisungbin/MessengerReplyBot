package land.sungbin.replybot.engine

public interface Replier {
  public fun markAsRead()
  public fun markAsRead(room: String)

  public fun reply(message: String)
  public fun reply(room: String, message: String)

  public fun delayedReply(message: String, delay: Long)
  public fun delayedReply(room: String, message: String, delay: Long)
}
