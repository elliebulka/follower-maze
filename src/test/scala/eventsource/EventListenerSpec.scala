package eventsource

import akka.io.Tcp.{Connected, Received}
import akka.testkit.TestProbe
import akka.util.ByteString


class EventListenerSpec extends AkkaBaseSpec {

  "Receiving event data " should {
    "result in sending data to event handler" in {
      val dataString = "1|F|100\n2|U|20|200\n3|B"
      val data = ByteString("1|F|100\n2|U|20|200\n3|B  ")
      val eventHandler = TestProbe()
      val eventListener = system.actorOf(EventListener.props(eventHandler.ref))
      eventListener ! Received(data)
      eventHandler.expectMsg(dataString)
    }
  }

}
