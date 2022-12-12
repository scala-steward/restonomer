package com.clairvoyant.restonomer.spark.utils.transformer

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, from_json, lit, to_json}
import org.apache.spark.sql.types.DataType

object DataFrameTransformerImplicits {

  implicit class DataFrameWrapper(df: DataFrame) {

    def addColumn(
        columnName: String,
        columnValue: String,
        columnDataType: Option[String]
    ): DataFrame =
      columnDataType
        .map(dataType => df.withColumn(columnName, lit(columnValue).cast(dataType)))
        .getOrElse(df.withColumn(columnName, lit(columnValue)))

    def drop(columnNames: Set[String]): DataFrame = df.drop(columnNames.toList: _*)

    def explode(columnName: String): DataFrame =
      df.withColumn(columnName, org.apache.spark.sql.functions.explode(col(columnName)))

    def castNestedColumn(
        columnName: String,
        ddl: String
    ): DataFrame = df.withColumn(columnName, from_json(to_json(col(columnName)), DataType.fromDDL(ddl)))

  }

}
