package scala.models

import scala.models.Types.{Payload, Sequence, UserId}

package object Types {

  /**
    * Event String is the incoming data from the event source.
    * It can contain many payloads joined together by \n
    * ex. 666|F|60|50 \n 1|U|12|9 \n 542532|B
    *
    * Payload is each individual event
    * ex. 666|F|60|50
    */
  type EventString = String
  type UserId = String
  type Sequence = Int
  type Payload = String

}

object Event {

  sealed trait Event {
    def payload: Payload
    def sequence: Int
    def payloadType: String
  }

  case class FollowEvent (
                          payload: Payload,
                          sequence: Sequence,
                          payloadType: String,
                          fromUserId: UserId,
                          toUserId: UserId
                         ) extends Event


  case class UnfollowEvent (
                            payload: Payload,
                            sequence: Sequence,
                            payloadType: String,
                            fromUserId: UserId,
                            toUserId: UserId
                           ) extends Event

  case class UserConnectionEvent (
                                   userId: UserId
                                 )

  case class BroadcastEvent(
                             payload: Payload,
                             sequence: Sequence,
                             payloadType: String
                           ) extends Event

  case class PrivateMessageEvent(
                                payload: Payload,
                                sequence: Sequence,
                                payloadType: String,
                                fromUserId: UserId,
                                toUserId: UserId
                                ) extends Event

  case class StatusUpdateEvent(
                              payload: Payload,
                              sequence: Sequence,
                              payloadType: String,
                              fromUserId: UserId
                              ) extends Event


}