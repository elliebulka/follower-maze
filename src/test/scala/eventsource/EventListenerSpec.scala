package eventsource

import java.net.InetSocketAddress

import akka.io.Tcp.{Connected, Received}
import akka.testkit.TestProbe
import akka.util.ByteString
import org.webbitserver.netty.EventSourceConnectionHandler

/**
  * Created by elliebulka on 11/20/16.
  */
class EventListenerSpec extends AkkaBaseSpec {

  "Receiving to event data " should {
    "result in sending data to event handler" in {
      val dataString = "1|F|100\n2|U|20|200\n3|B"
      val data = ByteString("1|F|100\n2|U|20|200\n3|B  ")
      val eventDistributor = TestProbe()
      val eventListener = system.actorOf(EventListener.props(eventDistributor.ref))
      eventListener ! Received(data)
      eventDistributor.expectMsg(dataString)
    }
  }

}
