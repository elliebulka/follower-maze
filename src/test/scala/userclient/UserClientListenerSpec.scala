package userclient

import java.util.UUID

import akka.io.Tcp.{Received, Write}
import akka.testkit.TestProbe
import akka.util.ByteString
import eventsource.EventListener

import scala.models.Event.UserConnectionEvent

trait Test

class UserClientListenerSpec extends AkkaBaseSpec {


  trait TestObj {
    val userConnection = TestProbe()
    val eventHandler = TestProbe()
    val userClientListener = system.actorOf(UserClientListener.props(userConnection.ref, eventHandler.ref))
  }

  "Receiving user data " should {
    "result in sending user connection event to event handler" in new TestObj {
      val userId = "123"
      userClientListener ! Received(ByteString(userId))
      eventHandler.expectMsg(UserConnectionEvent(userId))
    }
  }
  "sending payload string to user client" should {
    "result in user connection receiving event" in new TestObj {

      val payload = "542532|B"
      userClientListener ! payload
      userConnection.expectMsg(Write(ByteString(payload+"\n")))
    }
  }



}
