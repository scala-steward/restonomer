package com.clairvoyant.restonomer.core.common.enums

object RestonomerContextConfigTypes extends Enumeration {

  protected case class RestonomerContextConfigTypesDetails(configDirectoryName: String)
      extends super.Val(configDirectoryName)

  implicit def valueToRestonomerContextConfigTypesDetails(x: Value): RestonomerContextConfigTypesDetails =
    x.asInstanceOf[RestonomerContextConfigTypesDetails]

  val CHECKPOINT: RestonomerContextConfigTypesDetails = RestonomerContextConfigTypesDetails("checkpoints")
}
