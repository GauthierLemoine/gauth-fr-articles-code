package fr.gauth

object NGramsDemo {
  def main(args: Array[String]): Unit =
  {
    NGramsGenerator.generate("here is my list of words", 1, 3)
      .foreach(ngram => println("Got NGram: " + ngram.mkString(" ")))
  }
}