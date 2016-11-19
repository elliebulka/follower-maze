package scala

object MainApp extends App {

  val system = ActorSystem("follower-maze-system")
  val eventHandler = system.actorOf(Props[EventHandler], "event-handler")
  val userClientConnectionListener = system.actorOf(UserConnectionListener.props(eventHandler), "user-client-connection-listener")
  val eventSourceConnectionListener = system.actorOf(EventSourceConnectionListener.props(eventHandler), "event-source-connection-listener")

}