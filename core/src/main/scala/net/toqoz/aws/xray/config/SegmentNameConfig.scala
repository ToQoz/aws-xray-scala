package net.toqoz.aws.xray.config

object SegmentNameConfig extends ConfigReader {
  override val OVERRIDE_ENVIRONMENT_VARIABLE_KEY = "AWS_XRAY_TRACING_NAME"
  override val OVERRIDE_SYSTEM_PROPERTY_KEY = "com.amazonaws.xray.strategy.tracingName"

  def name(fallback: String): String = readValue.getOrElse(fallback)
}
