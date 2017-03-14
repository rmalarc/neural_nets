name := "neural_nets"

version := "1.0"

scalaVersion := "2.11.8"


libraryDependencies += "com.github.vinhkhuc" % "jcrfsuite" % "0.6"

val opRabbitVersion = "1.3.0"

libraryDependencies ++= Seq(
  "com.spingo" %% "op-rabbit-core"        % opRabbitVersion,
  "com.spingo" %% "op-rabbit-play-json"   % opRabbitVersion,
  "com.spingo" %% "op-rabbit-json4s"      % opRabbitVersion,
  "com.spingo" %% "op-rabbit-akka-stream" % opRabbitVersion,
  "com.timgroup" % "java-statsd-client" % "3.0.1",
  "com.abbyy" % "fcengine" % "1.1.1",
  "com.typesafe.play" % "play-ws_2.11" % "2.4.0",
  "org.apache.hadoop" % "hadoop-client" % "2.6.0" excludeAll (ExclusionRule("org.slf4j","slf4j-log4j12"), ExclusionRule(organization = "javax.servlet")),
  "com.typesafe.play" %% "anorm" % "2.5.0",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.postgresql" % "postgresql" % "9.4.1208.jre7",
  "com.typesafe" % "config" % "1.3.0"
)