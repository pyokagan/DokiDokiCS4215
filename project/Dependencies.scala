import sbt._

object Dependencies {
  val osName = System.getProperty("os.name")
  val lwjglClassifier =
    if (osName == "Linux")
      "natives-linux"
    else if (osName == "Mac OS X")
      "natives-macos"
    else if (osName.startsWith("Windows"))
      "natives-windows"
    else
      throw new RuntimeException(s"Invalid osName: $osName")
  val lwjglVersion = "3.2.1"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val lwjgl = "org.lwjgl" % "lwjgl" % lwjglVersion
  lazy val lwjglNative = "org.lwjgl" % "lwjgl" % lwjglVersion classifier lwjglClassifier
  lazy val lwjglGlfw = "org.lwjgl" % "lwjgl-glfw" % lwjglVersion
  lazy val lwjglGlfwNative = "org.lwjgl" % "lwjgl-glfw" % lwjglVersion classifier lwjglClassifier
  lazy val lwjglOpenal = "org.lwjgl" % "lwjgl-openal" % lwjglVersion
  lazy val lwjglOpenalNative = "org.lwjgl" % "lwjgl-openal" % lwjglVersion classifier lwjglClassifier
  lazy val lwjglOpengl = "org.lwjgl" % "lwjgl-opengl" % lwjglVersion
  lazy val lwjglOpenglNative = "org.lwjgl" % "lwjgl-opengl" % lwjglVersion classifier lwjglClassifier
  lazy val lwjglStb = "org.lwjgl" % "lwjgl-stb" % lwjglVersion
  lazy val lwjglStbNative = "org.lwjgl" % "lwjgl-stb" % lwjglVersion classifier lwjglClassifier
  lazy val joml = "org.joml" % "joml" % "1.9.13"
}
