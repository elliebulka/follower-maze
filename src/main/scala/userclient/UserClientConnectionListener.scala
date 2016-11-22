package userclient

import java.net.InetSocketAddress
import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import akka.io.Tcp.{Bind, Connected, Register}
import akka.io.{IO, Tcp}

object UserClientConnectionListener {
  def props(eventHandler: ActorRef) = Props(classOf[UserClientConnectionListener], eventHandler)
}

/**
  * User Client Connection Listener actor listens for user clients connection on specified port.
  * When the client is connected, a  user client listener actor is created and registered.
  *
  * @param eventHandler event handler actor
  */
class UserClientConnectionListener(eventHandler: ActorRef) extends Actor {

  import context.system

  val connectionPort = system.settings.config.getInt("app.client-connection-listener-port")

  IO(Tcp) ! Bind(self, new InetSocketAddress(connectionPort))

  def receive: Receive = {
    case Connected(remote, local) =>
      val userConnection = sender()
      val userClientListener = system.actorOf(UserClientListener.props(userConnection, eventHandler), UUID.randomUUID().toString)
      userConnection ! Register(userClientListener)
  }

}
