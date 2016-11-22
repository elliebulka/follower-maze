package scala.eventsource

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.collection.mutable
import scala.models.Event._
import scala.models.Types.{EventString, Payload, UserId}


class EventHandler extends Actor {

  private val userConnections = mutable.Map.empty[UserId, mutable.Buffer[ActorRef]]
  private val userFollowers = mutable.Map.empty[UserId, mutable.Buffer[UserId]]
  /**
    * character separates payloads in event string
    */
  private val payloadsSeparator = "\n"
  /**
    * character separates payload fields in payload
    */
  private val payloadFieldSeparator = "\\|"

  def receive: Receive = {
    /**
      * sort events by sequence id before they are handled
      */
    case events: EventString =>
      eventStringToEventsArray(events).sortWith(_.sequence < _.sequence) foreach { payload =>
        handleEvent(payload)
      }
    case UserConnectionEvent(id) =>
      val connections = userConnections.getOrElseUpdate(id, mutable.Buffer.empty[ActorRef])
      sender() +=: connections
  }

  /**
    *
    * @param event
    */
  private def handleEvent(event: Event) = {
    println("processing event: " + event.toString)
    event match {
      case FollowEvent(payload, sequence, payloadType, fromUserId, toUserId) => follow(payload, fromUserId, toUserId)
      case UnfollowEvent(payload, sequence, payloadType, fromUserId, toUserId) => unfollow(payload, fromUserId, toUserId)
      case BroadCastEvent(payload, sequence, payloadType) => broadcast(payload)
      case _ => // do nothing
    }
  }

  /**
    * Follow adds the user with id fromUserId to the set of
    * followers and notifies user with id toUserId of the
    * follow event
    *
    * @param payload
    * @param fromUserId user that is following
    * @param toUserId user that is being followed
    */
  private def follow(payload: String, fromUserId: UserId, toUserId: UserId) = {
    val followers = userFollowers.getOrElseUpdate(toUserId, mutable.Buffer.empty[UserId])
    fromUserId +=: followers
    for {
      connections <- userConnections.get(toUserId)
      userConnection <- connections
    } {
      userConnection ! payload
    }
  }

  /**
    * Unfollow removes the user with id fromUserId from the set of
    * user followers. No clients are notified of unfollow event
    *
    * @param payload
    * @param fromUserId user that is following
    * @param toUserId user that is being followed
    */
  private def unfollow(payload: String, fromUserId: UserId, toUserId: UserId) = {
    userFollowers.get(toUserId) foreach (followerSet => followerSet -= fromUserId)
  }

  /**
    * iterate over user connections and send payload to all users
    * @param payload Single event string to be broadcasted to users
    */
  private def broadcast(payload: Payload): Unit = {
    for {
      connections <- userConnections.valuesIterator
      userConnection <- connections
    } {
      userConnection ! payload
    }
  }

  /**
    *
    * @param eventString String containing one or more payload strings concatenated together
    * @return Array[Event]
    */
  private def eventStringToEventsArray(eventString: EventString): Array[Event] = {
    eventString.split(payloadsSeparator) flatMap { payload =>
      payloadToEvent(payload) match {
        case Some(eventResult) => Some(eventResult)
        case None => None
      }
    }
  }

  /**
    *
    * @param payload single event extracted from eventString
    * @return Option[Event]
    */
  private def payloadToEvent(payload: Payload): Option[Event] = {
    val eventFields = payload.split(payloadFieldSeparator)
    eventFields(1) match {
        case "F" =>
         Some(FollowEvent(payload, eventFields(0).toInt,
            eventFields(1), eventFields(3), eventFields(2)))
        case "U" =>
          Some(UnfollowEvent(payload, eventFields(0).toInt,
            eventFields(1), eventFields(3), eventFields(2)))
        case "B" =>
          Some(BroadCastEvent(payload, eventFields(0).toInt, eventFields(1)))
        case "P" => None
        case "S" => None
        case _ => None
      }
  }

}