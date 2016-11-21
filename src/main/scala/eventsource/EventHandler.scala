package scala.eventsource

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.collection.mutable
import scala.models.Event.{Event, FollowEvent, UnfollowEvent, UserConnectionEvent}
import scala.models.Types.{EventString, Payload, UserId}


class EventHandler extends Actor {

  private val userConnections = mutable.Map.empty[UserId, mutable.Buffer[ActorRef]]
  private val userFollowers = mutable.Map.empty[UserId, mutable.Set[UserId]]
  private val payloadsSeparator = "/n"
  private val payloadFieldSeparator = "|"

  def receive: Receive = {
    /**
      * sort events by sequence id before they are handled
      */
    case events: EventString =>
      eventStringToEvents(events).sortWith(_.sequence < _.sequence) foreach { payload =>
        handleEvent(payload)
      }
    case UserConnectionEvent(id) =>
      val connections = userConnections.getOrElseUpdate(id, mutable.Buffer.empty[ActorRef])
      sender() +=: connections
  }

  private def handleEvent(event: Event) = {
    event match {
      case FollowEvent(payload, sequence, payloadType, fromUserId, toUserId) => follow(payload, fromUserId, toUserId)
      case _ => // do nothing
    }
  }

  private def follow(payload: String, fromUserId: Int, toUserId: Int) = {
    val followers = userFollowers.getOrElseUpdate(toUserId, mutable.Set.empty[UserId])
    followers += fromUserId
    for {
      connections <- userConnections.get(toUserId)
      userConnection <- connections
    } {
      userConnection ! payload
    }
  }

  private def eventStringToEvents(eventString: EventString): Array[Event] = {
    eventString.split(payloadsSeparator) flatMap { payload =>
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
        case "F" => Some(FollowEvent(payloadString, eventFields(1).toInt,
          eventFields(0), eventFields(3).toInt, eventFields(2).toInt))
      }
  }

}