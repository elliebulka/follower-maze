package eventsource

import akka.testkit.TestActorRef
import org.scalatest.{FlatSpec, Matchers, PrivateMethodTester}

import scala.eventsource.EventHandler
import scala.models.Event._


class EventHandlerSpec extends AkkaBaseSpec with PrivateMethodTester {

  trait TestObj {
    val eventString = "666|F|60|50\n1|U|12|9\n542532|B"
    val payload = "542532|B"
    val broadcastEvent = Some(BroadcastEvent("542532|B", 542532, "B"))
    val eventArray: Array[Event] = Array(FollowEvent("666|F|60|50", 666, "F", "60", "50"),
      UnfollowEvent("1|U|12|9", 1, "U", "12", "9"), BroadcastEvent("542532|B", 542532, "B"))
    val eventHandlerRef = TestActorRef(new EventHandler)
    val eventHandler = eventHandlerRef.underlyingActor
    
  }

  "payload to event method" should {
    "take payload string and return correct event" in new TestObj {
      assert(eventHandler.payloadToEvent(payload) == broadcastEvent)
    }
  }

  "event string to event array" should {
    "take event string and return correct event array" in new TestObj {
      assert(eventHandler.eventStringToEventsArray(eventString) sameElements eventArray)
    }
  }



}
