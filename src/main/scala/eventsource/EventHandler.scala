package scala.eventsource

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.collection.mutable
import scala.models.Event.{Event, UserConnectionEvent}
import scala.models.Types.{Payload, UserId}


class EventHandler extends Actor with EventConversion {

  //empty list of connections and events
  private val userConnections = mutable.Map.empty[UserId, mutable.ListBuffer[ActorRef]]

  def receive: Receive = {
    case payload: Payload =>

    case UserConnectionEvent(id) =>
      val connections = userConnections.getOrElseUpdate(id, mutable.ListBuffer.empty[ActorRef])
      sender() +=: connections
  }

  private def handleEvent(event: Event) = {

  }

  private def follow(message: String) = {

  }

}