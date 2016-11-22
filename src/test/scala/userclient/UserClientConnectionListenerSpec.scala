package userclient

import java.net.InetSocketAddress
import akka.io.Tcp.Connected
import akka.testkit.TestProbe

class UserClientConnectionListenerSpec extends AkkaBaseSpec{

  "Connecting to UserClientConnectionListener" should {
    "return create UserClientListener actor" in {
      val userClientConnectionListener = system.actorOf(UserClientConnectionListener.props(TestProbe().ref))
      userClientConnectionListener ! Connected(new InetSocketAddress(9100), new InetSocketAddress(9000))
      TestProbe().expectActor("user/$*")
    }
  }

}
