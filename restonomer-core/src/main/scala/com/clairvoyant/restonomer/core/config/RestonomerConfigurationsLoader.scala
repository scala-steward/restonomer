package com.clairvoyant.restonomer.core.config

import zio.config.typesafe._
import zio.{ConfigProvider, _}

import java.io.File
import scala.annotation.tailrec
import scala.io.Source
import scala.util.{Failure, Success, Using}

object RestonomerConfigurationsLoader {

  def loadConfigFromFile[C](configFilePath: String, config: Config[C])(
      implicit configVariablesSubstitutor: Option[ConfigVariablesSubstitutor]
  ): C =
    Using(Source.fromFile(new File(configFilePath))) { configFileSource =>
      Unsafe.unsafe(implicit u => {
        zio.Runtime.default.unsafe
          .run(
            ConfigProvider
              .fromHoconString(
                configVariablesSubstitutor
                  .map(_.substituteConfigVariables(configFileSource.mkString))
                  .getOrElse(configFileSource.mkString)
              )
              .load(config)
          )
          .getOrThrowFiberFailure()
      })
    } match {
      case Success(config) =>
        config
      case Failure(exception) =>
        throw exception
    }

  def loadConfigsFromDirectory[C](configDirectoryPath: String, config: Config[C])(
      implicit configVariablesSubstitutor: Option[ConfigVariablesSubstitutor]
  ): List[C] = {
    @tailrec
    def loadConfigsFromDirectoryHelper(remainingConfigFiles: List[File], configs: List[C]): List[C] =
      if (remainingConfigFiles.isEmpty) configs
      else {
        val configFile = remainingConfigFiles.head

        if (configFile.isDirectory)
          loadConfigsFromDirectoryHelper(configFile.listFiles().toList ++ remainingConfigFiles.tail, configs)
        else
          loadConfigsFromDirectoryHelper(
            remainingConfigFiles.tail,
            loadConfigFromFile(configFile.getPath, config) :: configs
          )
      }

    loadConfigsFromDirectoryHelper(new File(configDirectoryPath).listFiles().toList, List())
  }

}
