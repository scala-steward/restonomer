package com.clairvoyant.restonomer.core.app.config

import com.clairvoyant.restonomer.core.common.util.FileUtil.fileExists
import com.clairvoyant.restonomer.core.exceptions.RestonomerContextException
import pureconfig._

import java.io.File
import scala.reflect.ClassTag

object RestonomerContextConfigUtil {

  def readConfigs[C: ClassTag](configDirectoryPath: String)(implicit reader: ConfigReader[C]): Option[List[C]] = {
    if (fileExists(configDirectoryPath))
      Option(
        new File(configDirectoryPath)
          .listFiles()
          .map(loadConfig[C](_))
          .toList
      )
    else
      None
  }

  def loadConfig[C](configFile: File)(implicit reader: ConfigReader[C]): C = {
    ConfigSource.file(configFile).load[C] match {
      case Right(config) =>
        config
      case Left(error) =>
        throw new RestonomerContextException(error.prettyPrint())
    }
  }

}
