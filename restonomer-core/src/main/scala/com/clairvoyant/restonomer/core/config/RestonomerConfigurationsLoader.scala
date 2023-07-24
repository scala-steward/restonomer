package com.clairvoyant.restonomer.core.config

import zio.config.typesafe._
import zio.{ConfigProvider, _}

import java.io.File
import scala.annotation.tailrec
import scala.io.Source
import scala.util.{Failure, Success, Using}

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import scala.io.BufferedSource
import java.io.ByteArrayInputStream
import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobId


object RestonomerConfigurationsLoader {

  def loadConfigFromFile[C](configFilePath: String, config: Config[C])(
      implicit configVariablesSubstitutor: Option[ConfigVariablesSubstitutor]
  ): C =
    Using(readFile(configFilePath)) { configFileSource =>
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

  def readFile(absoluteFilePath: String): BufferedSource = {
    if (absoluteFilePath.startsWith("gs://")) {
      // Get bucket and filepath
      val bucketName: String = absoluteFilePath.stripPrefix("gs://").split("/", 2)(0)
      val filePath: String  = absoluteFilePath.stripPrefix("gs://").split("/", 2)(1)

      println(s"bucketName :=> $bucketName")
      println(s"filePath :=> $filePath")

      val storage = StorageOptions.getDefaultInstance.getService
      val blobId: BlobId = BlobId.of(bucketName, filePath)
      val blob = storage.get(blobId)
      val content = blob.getContent()
      Source.fromInputStream(new ByteArrayInputStream(content))
    } else {
        Source.fromFile(new File(absoluteFilePath))
    }
  }

}