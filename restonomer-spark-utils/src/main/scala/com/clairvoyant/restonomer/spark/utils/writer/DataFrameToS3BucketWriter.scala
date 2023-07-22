package com.clairvoyant.restonomer.spark.utils.writer

import org.apache.spark.sql.{DataFrame, SparkSession}

class DataFrameToS3BucketWriter(
    bucketName: String,
    fileFormat: String,
    filePath: String,
    saveMode: String
) extends DataFrameWriter {

  override def write(dataFrame: DataFrame)(implicit sparkSession: SparkSession): Unit = {
    dataFrame.write
      .mode(saveMode)
      .format(fileFormat)
      .save(s"s3a://$bucketName/$filePath")
  }

}
