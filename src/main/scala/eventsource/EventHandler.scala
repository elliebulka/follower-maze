package scala.eventsource

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.collection.mutable
import scala.models.Event.{Event, FollowEvent, UserConnectionEvent}
import scala.models.Types.{Payload, UserId}


class EventHandler extends Actor with EventConversion {

  private val userConnections = mutable.Map.empty[UserId, mutable.ListBuffer[ActorRef]]
  private val payloadsSeparator = "/n"
  private val payloadFieldSeparator = "|"

  def receive: Receive = {
    case payload: Payload =>
      payloadsStringToEvents(payload).sortWith(_.payload < _.payload) foreach { event =>
        handleEvent(event)
      }
    case UserConnectionEvent(id) =>
      val connections = userConnections.getOrElseUpdate(id, mutable.ListBuffer.empty[ActorRef])
      sender() +=: connections
  }

  private def handleEvent(event: Event) = {

  }

  private def follow(message: String) = {

  }

  private def payloadsStringToEvents(payloadsString: Payload): Array[Event] = {
    payloadsString.split(payloadsSeparator) flatMap { payload =>
      val event = for {
        event <- payloadToEvent(payload)
      } yield {
        event
      }
      event match {
        case Some(eventResult) => Some(eventResult)
        case None => None
      }
    }
  }

  private def payloadToEvent(payloadString: Payload): Option[Event] = {
    val eventFields = payloadString.split(payloadFieldSeparator)
    eventFields(1) match {
        case "F" => Some(FollowEvent(payloadString, eventFields(1),
          eventFields(0), eventFields(3).toInt, eventFields(2).toInt))
      }
  }

}