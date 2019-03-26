package cs4215.scene

import org.joml._

final class Pose(val position: Vector3f, val orientation: Quaternionf, val scale: Vector3f) {
  def this(pose: Pose) = this(new Vector3f(pose.position), new Quaternionf(pose.orientation), new Vector3f(pose.scale))
  def this() = this(new Vector3f(), new Quaternionf(), new Vector3f(1f))

  def set(pose: Pose): Pose = {
    position.set(pose.position)
    orientation.set(pose.orientation)
    scale.set(pose.scale)
    this
  }

  def toMatrix4f(out: Matrix4f): Matrix4f = out.translationRotateScale(position, orientation, scale)
}
