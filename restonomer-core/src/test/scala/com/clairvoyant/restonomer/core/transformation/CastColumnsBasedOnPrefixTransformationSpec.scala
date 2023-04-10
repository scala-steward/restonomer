package com.clairvoyant.restonomer.core.transformation

import com.clairvoyant.restonomer.core.common.CoreSpec
import com.clairvoyant.restonomer.spark.utils.reader.JSONTextToDataFrameReader
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.{DecimalType, DoubleType, FloatType, LongType, StringType}

class CastColumnsBasedOnPrefixTransformationSpec extends CoreSpec {

  val restonomerResponseDF: DataFrame =
    new JSONTextToDataFrameReader(
      sparkSession = sparkSession
    ).read(text =
      Seq(
        """
          |{
          |  "name": "abc",
          |  "price_in_india": "240",
          |  "price_in_canada": "3",
          |  "percent_difference": "10.23"
          |}
          |""".stripMargin
      )
    )

  "transform()" should "cast columns with the given prefix to the given type" in {
    val restonomerTransformation = CastColumnsBasedOnPrefix(
      prefixList = List("price", "percent"),
      dataTypeToCast = "float"
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "name")
      .head
      .dataType shouldBe StringType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "price_in_india")
      .head
      .dataType shouldBe FloatType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "price_in_canada")
      .head
      .dataType shouldBe FloatType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "percent_difference")
      .head
      .dataType shouldBe FloatType
  }

  "transform()" should "skip the prefix having no matches" in {
    val restonomerTransformation = CastColumnsBasedOnPrefix(
      prefixList = List("amount", "value"),
      dataTypeToCast = "double"
    )

    val actualRestonomerResponseTransformedDF = restonomerTransformation.transform(restonomerResponseDF)

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "name")
      .head
      .dataType shouldBe StringType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "price_in_india")
      .head
      .dataType shouldBe StringType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "price_in_canada")
      .head
      .dataType shouldBe StringType

    actualRestonomerResponseTransformedDF.schema.fields
      .filter(_.name == "percent_difference")
      .head
      .dataType shouldBe StringType
  }

}
