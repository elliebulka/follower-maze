package userclient

import java.net.InetSocketAddress
import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import akka.io.Tcp.{Bind, Connected, Register}
import akka.io.{IO, Tcp}

/**
  * Created by elliebulka on 11/20/16.
  */
object UserClientConnectionListener {
  def props(eventHandler: ActorRef) = Props(classOf[UserClientConnectionListener], eventHandler)
}

class UserClientConnectionListener(eventHandler: ActorRef) extends Actor {

  import context.system

  val connectionPort = system.settings.config.getInt("app.client-connection-listener-port")

  IO(Tcp) ! Bind(self, new InetSocketAddress(connectionPort))

  def receive: Receive = {
    case Connected(remote, local) =>
      val userConnection = sender()
     // val userConnectionHandler = system.actorOf(UserConnectionHandler.props(userConnection, eventDistributor), UUID.randomUUID().toString)
     // userConnection ! Register(userConnectionHandler)
  }

}
