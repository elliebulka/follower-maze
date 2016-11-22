package scala.eventsource

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import errors.InvalidEventError.InvalidEventError

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
        processEvent(payload)
      }
    case UserConnectionEvent(id) =>
      val connections = userConnections.getOrElseUpdate(id, mutable.Buffer.empty[ActorRef])
      sender() +=: connections
  }

  /**
    *
    * @param event
    */
  private def processEvent(event: Event) = {
    event match {
      case FollowEvent(payload, sequence, payloadType, fromUserId, toUserId) => follow(payload, fromUserId, toUserId)
      case UnfollowEvent(payload, sequence, payloadType, fromUserId, toUserId) => unfollow(payload, fromUserId, toUserId)
      case BroadcastEvent(payload, sequence, payloadType) => broadcast(payload)
      case PrivateMessageEvent(payload, sequence, payloadType, fromUserId, toUserId) => privateMessage(payload, toUserId)
      case StatusUpdateEvent(payload, sequence, payloadType, fromUserId) => updateStatus(payload, fromUserId)
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
    val followerList = userFollowers.getOrElseUpdate(toUserId, mutable.Buffer.empty[UserId])
    fromUserId +=: followerList
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
    userFollowers.get(toUserId) foreach (followerList => followerList -= fromUserId)
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

  private def privateMessage(payload: Payload, toUserId: UserId): Unit = {
    for {
      connections <- userConnections.get(toUserId)
      userConnection <- connections
    } {
      userConnection ! payload
    }
  }

  private def updateStatus(payload: Payload, fromUserId: UserId) = {
    for {
      followerList <- userFollowers.get(fromUserId)
      follower <- followerList
      userConnections <- userConnections.get(follower)
      userConnection <- userConnections
    } {
      userConnection ! payload
    }
  }


  /**
    *
    * @param eventString String containing one or more payload strings concatenated together
    * @return Array[Event]
    */
  def eventStringToEventsArray(eventString: EventString): Array[Event] = {
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
  def payloadToEvent(payload: Payload): Option[Event] = {
    val eventFields = payload.split(payloadFieldSeparator)
    try {
      eventFields(1) match {
        case "F" =>
          Some(FollowEvent(payload, eventFields(0).toInt,
            eventFields(1), eventFields(2), eventFields(3)))
        case "U" =>
          Some(UnfollowEvent(payload, eventFields(0).toInt,
            eventFields(1), eventFields(2), eventFields(3)))
        case "B" =>
          Some(BroadcastEvent(payload, eventFields(0).toInt, eventFields(1)))
        case "P" =>
          Some(PrivateMessageEvent(payload, eventFields(0).toInt,
            eventFields(1), eventFields(2), eventFields(3)))
        case "S" =>
          Some(StatusUpdateEvent(payload, eventFields(0).toInt, eventFields(1), eventFields(2)))
      }
    } catch {
      case ex: ArrayIndexOutOfBoundsException => throw InvalidEventError("Invalid payload: " + payload)
      case ex: Throwable => throw ex
    }


  }

}