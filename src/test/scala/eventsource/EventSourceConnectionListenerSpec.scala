package eventsource

import akka.testkit.TestProbe
import java.net.InetSocketAddress

import akka.io.{IO, Tcp}
import akka.io.Tcp.{Connect, Connected, Register}
import src.main.scala.FollowerMazeApp



/**
  * Created by elliebulka on 11/20/16.
  */
class EventSourceConnectionListenerSpec extends AkkaBaseSpec {


  "Connecting to EventSourceConnectionListener" should {
    "result in creating an EventListener" in {
      val eventSourceConnectionListener = system.actorOf(EventSourceConnectionListener.props(TestProbe().ref))
      eventSourceConnectionListener ! Connected(new InetSocketAddress(9100), new InetSocketAddress(9000))
      TestProbe().expectActor("/user/event-listener")
    }
  }

}
