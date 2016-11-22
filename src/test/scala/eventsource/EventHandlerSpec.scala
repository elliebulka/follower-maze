package eventsource

import akka.testkit.{TestActorRef, TestProbe}
import org.scalatest.PrivateMethodTester

import scala.eventsource.EventHandler
import scala.models.Event._


class EventHandlerSpec extends AkkaBaseSpec with PrivateMethodTester {

  trait TestObj {
    val eventString = "666|F|60|50\n1|U|12|9\n542532|B\n111|S|22\n99|P|8|100"
    val broadcastPayload = "542532|B"
    val broadcastEvent = BroadcastEvent("542532|B", 542532, "B")
    val followEvent = FollowEvent("666|F|60|50", 666, "F", "60", "50")
    val unfollowEvent = UnfollowEvent("1|U|12|9", 1, "U", "12", "9")
    val eventArray: Array[Event] = Array(FollowEvent("666|F|60|50", 666, "F", "60", "50"),
      UnfollowEvent("1|U|12|9", 1, "U", "12", "9"), BroadcastEvent("542532|B", 542532, "B"),
      StatusUpdateEvent("111|S|22", 111, "S", "22"), PrivateMessageEvent("99|P|8|100", 99, "P", "8", "100"))
    val eventHandlerRef = TestActorRef(new EventHandler)
    val eventHandler = eventHandlerRef.underlyingActor
    val user1 = "60"
    val user2 = "50"
    val user3 = "100"
    val user4 = "22"
    val user1ClientListener = TestProbe()
    val user2ClientListener = TestProbe()
    val user3ClientListener = TestProbe()
    val user4ClientListener = TestProbe()
  }

  "payload to event method" should {
    "take payload string and return correct event" in new TestObj {
      assert(eventHandler.payloadToEvent(broadcastPayload) == Some(broadcastEvent))
    }
  }

  "event string to event array" should {
    "take event string and return correct event array" in new TestObj {
      assert(eventHandler.eventStringToEventsArray(eventString) sameElements eventArray)
    }
  }

  "follow/unfollow events" should {
    "change list of followers" in new TestObj {
        eventHandlerRef ! "666|F|60|50"
        assert(eventHandler.userFollowers.get(user2) match {
          case Some(x) => x.contains(user1)
          case None => false
        })

        eventHandlerRef ! "667|U|60|50"
        assert(eventHandler.userFollowers.get(user2) match {
          case Some(x) => !x.contains(user1)
          case None => true
        })
    }
  }

  "broadcast events" should {
    "route broadcast event to correct users" in new TestObj {

      user1ClientListener.send(eventHandlerRef, UserConnectionEvent(user1))
      user2ClientListener.send(eventHandlerRef, UserConnectionEvent(user2))
      user3ClientListener.send(eventHandlerRef, UserConnectionEvent(user3))
      user4ClientListener.send(eventHandlerRef, UserConnectionEvent(user4))

      eventHandlerRef ! broadcastPayload

      user1ClientListener.expectMsg(broadcastPayload)
      user2ClientListener.expectMsg(broadcastPayload)
      user3ClientListener.expectMsg(broadcastPayload)
      user4ClientListener.expectMsg(broadcastPayload)
    }
  }

  "status update events" should {
    "route update events to correct users" in new TestObj {
      user1ClientListener.send(eventHandlerRef, UserConnectionEvent("60"))

      eventHandlerRef ! "668|F|60|22"
      eventHandlerRef ! "111|S|22"

      user1ClientListener.expectMsg("111|S|22")

    }
  }




}
