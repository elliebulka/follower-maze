package scala.models

import scala.models.Types.{Payload, Sequence, UserId}

package object Types {

  type EventString = String
  type UserId = Int
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


}