package com.clairvoyant.restonomer.core.persistence

import com.clairvoyant.data.scalaxy.reader.text.JSONTextToDataFrameReader
import com.clairvoyant.restonomer.core.common.S3MockSpec
import com.clairvoyant.restonomer.core.common.S3MockSpec.*
import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.DataFrame
import com.clairvoyant.data.scalaxy.test.util.readers.DataFrameReader

class S3BucketPersistenceSpec extends DataFrameReader with S3MockSpec {

  val restonomerResponseDF = readJSONFromText(
    """
      |{
      |  "col_A": "val_A",
      |  "col_B": "val_B",
      |  "col_C": "val_C"
      |}
      |""".stripMargin
  )

  "persist()" should "save the dataframe to the files in the s3 bucket" in {
    val s3BucketPersistence = S3Bucket(
      bucketName = "test-bucket",
      fileFormat = "JSON",
      filePath = "test-output-dir"
    )

    s3Client.createBucket(s3BucketPersistence.bucketName)

    s3BucketPersistence.persist(restonomerResponseDF)

    readJSONFromFile(
      s"s3a://${s3BucketPersistence.bucketName}/${s3BucketPersistence.filePath}"
    ) should matchExpectedDataFrame(restonomerResponseDF)
  }

}
