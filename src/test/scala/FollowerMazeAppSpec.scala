package scala

import akka.testkit.TestProbe
import src.main.scala.FollowerMazeApp

class FollowerMazeAppSpec extends AkkaBaseSpec {



  "Creating FollowerMazeApp" should {
    "result in creating an actor named 'event-handler'" in {
      new FollowerMazeApp(system)
      TestProbe().expectActor("/user/event-handler")
    }
    "result in creating an actor named 'event-source-connection-listener'" in {
      TestProbe().expectActor("/user/event-source-connection-listener")
    }
    "result in creating an actor named 'user-client-connection-listener'" in {
      TestProbe().expectActor("/user/user-client-connection-listener")
    }

  }


}