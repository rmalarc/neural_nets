package perceptron
import com.github.jcrfsuite.CrfTrainer
import com.github.jcrfsuite.CrfTagger

/**
  * Created by malarconba001 on 8/30/2016.
  */
object Perceptron {
  //  val data = Seq(Seq(0, 0, 0), Seq(1,0, 1), Seq(0,1, 0), Seq(0,1, 1), Seq(1,1, 0), Seq(1,1, 1), Seq(1,0, 0), Seq(0,0, 1))
  //  val response = Seq(0, 1, 0, 1,1,0,0,0)
  val data = Seq(Seq(0, 0, -1), Seq(0, 1, -1), Seq(1, 0, -1), Seq(1, 1, -1))
  val response = Seq(0, 1, 1, 1)
//  val weights = Seq(Math.random * 2 - 1, Math.random * 2 - 1, Math.random * 2 - 1)
  val weights = Seq(-0.05, -0.02, 0.02)
  val lr = 0.25


  val model: Seq[Double] = {
    (0 to 10).toList.foldLeft(weights) { (W, v) =>
      val newW = (data zip response).foldLeft(W) { (w0, r) =>
        val y = recall(r._1, w0)
        val delta = y - r._2
        val w1 = (r._1 zip w0).map { d =>
          d._2 - d._1 * delta * lr
        }
        w1
      }
      println((W, newW))
      val t = CrfTrainer.train("x","Y")
      val ta = new CrfTagger("x")
      Seq(0.01)
    }
  }

  def recall(d: Seq[Int], w: Seq[Double] = model) = {
    val products = (d zip w).map(d => d._1 * d._2)
    (products.sum > 0).compare(true) + 1
  }
}
