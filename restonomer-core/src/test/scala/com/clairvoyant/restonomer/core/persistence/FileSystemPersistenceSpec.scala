package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import com.clairvoyant.restonomer.spark.utils.writer.DataFrameToFileSystemWriter
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.DataFrame

import java.io.File

class FileSystemPersistenceSpec extends CoreSpec {

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(text =
      Seq(
        """
          |{
          |  "col_A": "val_A",
          |  "col_B": "val_B",
          |  "col_C": "val_C",
          |  "col_D": "val_D"
          |}
          |""".stripMargin
      )
    )

  lazy val dataFrameToFileSystemWriterOutputDirPath = s"out_${System.currentTimeMillis()}"

  "persist() - with proper format and path" should
    "save the dataframe to the file in the desired format at the desired path" in {
      val fileSystemPersistence = FileSystem(
        fileFormat = "JSON",
        filePath = dataFrameToFileSystemWriterOutputDirPath
      )

      fileSystemPersistence.persist(
        restonomerResponseDF,
        new DataFrameToFileSystemWriter(
          fileFormat = fileSystemPersistence.fileFormat,
          filePath = fileSystemPersistence.filePath,
          saveMode = fileSystemPersistence.saveMode
        )
      )

      sparkSession.read.json(dataFrameToFileSystemWriterOutputDirPath) should matchExpectedDataFrame(
        restonomerResponseDF
      )
    }

  override def afterAll(): Unit = FileUtils.deleteDirectory(new File(dataFrameToFileSystemWriterOutputDirPath))

}
