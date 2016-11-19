package scala.models

object Event {

  sealed trait Event {
    def payload: String
    def sequence: Int
    def payloadType: String
  }

  case class FollowEvent (
    payload: String,
    sequence: Int,
    payloadType: String,
    fromUserId: Int,
    toUserId: Int
                         ) extends Event


  case class UnfollowEvent (
    payload: String,
    sequence: Int,
    payloadType: String,
    fromUserId: Int,
    toUserId: Int
                           ) extends Event


}