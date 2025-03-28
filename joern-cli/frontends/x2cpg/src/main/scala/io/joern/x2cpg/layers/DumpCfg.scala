package io.joern.x2cpg.layers

import io.shiftleft.semanticcpg.language.*
import io.shiftleft.semanticcpg.layers.{LayerCreator, LayerCreatorContext, LayerCreatorOptions}
import io.shiftleft.semanticcpg.utils.FileUtil.*

import java.nio.file.{Files, Paths}

case class CfgDumpOptions(var outDir: String) extends LayerCreatorOptions {}

object DumpCfg {

  val overlayName = "dumpCfg"

  val description = "Dump control flow graph to out/"

  def defaultOpts: CfgDumpOptions = CfgDumpOptions("out")
}

class DumpCfg(options: CfgDumpOptions) extends LayerCreator {
  override val overlayName: String       = DumpCfg.overlayName
  override val description: String       = DumpCfg.description
  override val storeOverlayName: Boolean = false

  override def create(context: LayerCreatorContext): Unit = {
    val cpg = context.cpg
    cpg.method.zipWithIndex.foreach { case (method, i) =>
      val str        = method.dotCfg.head
      val outputPath = Paths.get(options.outDir) / s"$i-cfg.dot"
      Files.writeString(outputPath, str)
    }
  }
}
