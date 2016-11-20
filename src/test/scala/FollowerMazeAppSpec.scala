package scala

import akka.testkit.TestProbe
import src.main.scala.FollowerMazeApp

class FollowerMazeAppSpec extends AkkaBaseSpec {



  "Creating FollowerMazeApp" should {
    "result in creating an actor named 'event-handler'" in {
      new FollowerMazeApp(system)
      TestProbe().expectActor("/user/event-handler")
    }
    "result in creating an actor named 'event-listener'" in {
      new FollowerMazeApp(system)
      TestProbe().expectActor("/user/event-listener")
    }
    "result in creating an actor named 'source-listener'" in {
      new FollowerMazeApp(system)
      TestProbe().expectActor("/user/source-listener")
    }
  }


}