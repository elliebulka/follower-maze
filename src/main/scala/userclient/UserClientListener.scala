package userclient

import akka.actor.Actor.Receive
import akka.actor.{ActorRef, Props}
import akka.io.Tcp.{Received, Write}
import akka.util.ByteString

import scala.models.Event.UserConnectionEvent
import scala.models.Types._

object UserClientListener {
  def props(userConnection: ActorRef, eventHandler: ActorRef) = Props(classOf[UserClientListener],
    userConnection, eventHandler)
}

/**
  * user client listener actor forwards user connection events to the event handler
  * and forwards events from the event source to the appropriate user connections
  *
  * @param userConnection user connection actor
  * @param eventHandler event handler actor
  */
class UserClientListener(userConnection: ActorRef, eventHandler: ActorRef) {

  def receive: Receive = {

    /**
      * UserConnectionEvent data is sent to event handler
      */
    case Received(data) => eventHandler ! UserConnectionEvent(data.utf8String.toInt)

    /**
      * Event Source payload is sent to user connection
      */
    case payload: Payload => userConnection ! Write(ByteString(payload))

  }

}
