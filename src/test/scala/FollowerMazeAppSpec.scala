package scala

import akka.actor.ActorDSL._
import akka.testkit.TestProbe
import akka.util.Timeout
import src.main.scala.FollowerMazeApp

class FollowerMazeAppSpec extends AkkaBaseSpec {



  "Creating FollowerMazeApp" should {
    "result in creating a top-level actor named 'event-handler'" in {
      new FollowerMazeApp(system)
      TestProbe().expectActor("/user/event-handler")
    }
  }


}