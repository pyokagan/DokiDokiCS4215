package cs4215.scene

private final class BinPack(val w: Int, val h: Int) {
  final case class BinNode(x: Int, y: Int, w: Int)
  final case class Rect(x: Int, y: Int, w: Int, h: Int)
  private val nodes = scala.collection.mutable.ArrayBuffer.empty[BinNode] :+ BinNode(1, 1, w - 1)

  // Test if region (w, h) fits into nodes(i)
  private def fit(i: Int, w: Int, h: Int): Int = {
    if (nodes(i).x + w > this.w) -1
    else {
      def aux(i: Int, y: Int, w_left: Int): Int = {
        if (w_left <= 0) y
        else if (i >= nodes.length) -1
        else {
          val node = nodes(i)
          val y2 = math.max(y, node.y)
          if (y2 + h > this.h) -1
          else aux(i + 1, y2, w_left - node.w)
        }
      }
      aux(i, nodes(i).y, w)
    }
  }

  // Merge nodes where possible
  private def merge(): Unit = {
    def aux(i: Int): Unit = {
      if (i >= nodes.length - 1) ()
      else {
        val node = nodes(i)
        val nextNode = nodes(i + 1)
        if (node.y == nextNode.y) {
          nodes.update(i, BinNode(node.x, node.y, node.w + nextNode.w))
          nodes.remove(i + 1)
          aux(i)
        } else aux(i + 1)
      }
    }
    aux(0)
  }

  // Try to pack (w, h) and return the newly allocated region
  private def _pack(w: Int, h: Int): Rect = {
    var bestW = Int.MaxValue
    var bestH = Int.MaxValue
    var bestI = -1
    var region = Rect(0, 0, 0, 0)
    nodes.zipWithIndex.foreach { case (node, i) =>
      val y = fit(i, w, h)
      if (y >= 0 && ((y + h < bestH) || (y + h == bestH && node.w < bestW))) {
        bestW = node.w
        bestH = y + h
        bestI = i
        region = Rect(node.x, y, w, h)
      }
    }
    if (bestI == -1) region
    else {
      val node = BinNode(region.x, region.y + h, w)
      nodes.insert(bestI, node)
      def aux(i: Int): Unit = {
        if (i >= nodes.length) ()
        else {
          val node = nodes(i)
          val prevNode = nodes(i - 1)
          if (node.x < prevNode.x + prevNode.w) {
            val shrink = prevNode.x + prevNode.w - node.x
            if (node.w - shrink <= 0) {
              nodes.remove(i)
              aux(i)
            } else {
              nodes.update(i, BinNode(node.x + shrink, node.y, node.w - shrink))
            }
          } else ()
        }
      }
      aux(bestI + 1)
      merge()
      region
    }
  }

  def pack(w: Int, h: Int): (Int, Int, Int, Int) = {
    val out = _pack(w, h)
    if (out.w <= 0 && out.h <= 0)
      throw new RuntimeException("Could not pack")
    (out.x, out.y, out.w, out.h)
  }
}
