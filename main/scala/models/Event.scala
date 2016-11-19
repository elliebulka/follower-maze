package scala.models

object Event {

  trait EventObj {
    def payload = String
    def sequence = Int
    def payloadType = String
  }

  case class FollowEvent extends EventObj {
    payload: String,
    sequence: Int,
    payloadType: String,
    fromUserId: Int,
    toUserId: Int
  }

  case class UnfollowEvent {
    payload: String,
    sequence: Int,
    payloadType: String,
    fromUserId: Int,
    toUserId: Int
  }

}