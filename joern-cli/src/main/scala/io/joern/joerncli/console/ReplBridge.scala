package io.joern.joerncli.console

import io.joern.console.BridgeBase
import java.io.PrintStream

object ReplBridge extends BridgeBase {

  override val applicationName = "joern"

  def main(args: Array[String]): Unit = {
    run(parseConfig(args))
  }

  /** Code that is executed when starting the shell
    */
  override def predefLines =
    Predefined.forInteractiveShell

  override def greeting = JoernConsole.banner()

  override def promptStr: String = "joern"

  override def onExitCode: String = "workspace.projects.foreach(_.close)"

}
