package io.shiftleft.semanticcpg.layers

import io.shiftleft.codepropertygraph.generated.Cpg
import io.shiftleft.semanticcpg.Overlays
import org.slf4j.{Logger, LoggerFactory}

abstract class LayerCreator {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  val overlayName: String
  val description: String
  val dependsOn: List[String] = List()

  /** If the LayerCreator modifies the CPG, then we store its name in the CPGs metadata and disallow rerunning the
    * creator, that is, applying the layer twice.
    */
  protected val storeOverlayName: Boolean = true

  def run(context: LayerCreatorContext): Unit = {
    val appliedOverlays = Overlays.appliedOverlays(context.cpg).toSet
    if (!dependsOn.toSet.subsetOf(appliedOverlays)) {
      logger.warn(
        s"${this.getClass.getName} depends on $dependsOn but CPG only has $appliedOverlays - skipping creation"
      )
    } else if (appliedOverlays.contains(overlayName)) {
      logger.warn(s"The overlay $overlayName already exists - skipping creation")
    } else {
      create(context)
      if (storeOverlayName) {
        Overlays.appendOverlayName(context.cpg, overlayName)
      }
    }
  }

  def create(context: LayerCreatorContext): Unit

}

class LayerCreatorContext(val cpg: Cpg, val outputDir: Option[String] = None) {}
class LayerCreatorOptions()
