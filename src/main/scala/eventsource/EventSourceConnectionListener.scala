package eventsource

import java.net.InetSocketAddress
import akka.actor.{Actor, ActorRef, Props}
import akka.io.{IO, Tcp}
import akka.io.Tcp.{Bind, Connected, Register}

object EventSourceConnectionListener {
  def props(eventHandler: ActorRef) =
    Props(classOf[EventSourceConnectionListener], eventHandler)
}

/**
  * The event source connection listener actor listens on port 9090 for an incoming event
  * source connection. When a connection is received and event listener actor is created to
  * receive and process the incoming stream of events from the event source.
  *
  * @param eventHandler event handler actor
  */
class EventSourceConnectionListener(eventHandler: ActorRef) extends Actor {

  import context.system

  val connectionPort = system.settings.config.getInt("app.event-source-connection-listener-port")

  IO(Tcp) ! Bind(self, new InetSocketAddress(connectionPort))

  def receive: Receive = {
    case Connected(remote, local) =>
      val connection = sender()
      val eventListener = system.actorOf(EventListener.props(eventHandler), "event-listener")
      connection ! Register(eventListener)
  }

}