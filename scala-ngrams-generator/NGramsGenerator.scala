package fr.gauth

import scala.collection.JavaConversions._
import scala.collection.mutable.{Queue => MutableQueue}
import java.util.StringTokenizer

object NGramsGenerator
{
  // each ngram is represented as a List of String
  def generate(text : String, minSize : Int, maxSize : Int) =
  {
    assert(maxSize >= minSize && minSize > 0 && maxSize > 0)

    // iterate for each token on the available ngrams
    for (ngramList <- this.generate_sub(text, minSize, maxSize);
         ngram <- ngramList)
      yield ngram.toList
  }

  // generate a list of ngrams
  def generate_sub(text : String, minSize : Int, maxSize : Int) =
  {
    val nGramsBuffer = new NGramsBuffer(minSize, maxSize)
    for (t <- this.getTokenizerIterator(text))
     yield
     {
       nGramsBuffer.addToken(t.asInstanceOf[String])
       nGramsBuffer.getNGrams
     }
  }

  // Can be overloaded to use an other tokenizer
  def getTokenizerIterator(text : String) =
    new StringTokenizer(text)

  // A utility class that stores a list of fixed size queues, each queue is a current ngram
  class NGramsBuffer(val minSize : Int, val maxSize : Int)
  {
    val queues = minSize.to(maxSize).foldLeft(List[SlidingFixedSizeQueue[String]]()) {
      (list, n) =>
        new SlidingFixedSizeQueue[String](n) :: list
    }

    def addToken(token : String) = queues.foreach(q => q.enqueue(token))

    // return only buffers that are full, otherwise not enough tokens have been
    // see yet to fill in the NGram
    def getNGrams() = queues.filter(q => q.size == q.maxSize)
  }

  // Utility class to store the last maxSize encountered tokens
  class SlidingFixedSizeQueue[A](val maxSize : Int) extends scala.collection.mutable.Queue[A]
  {
    override def enqueue(elems : A*) =
    {
      elems.foreach(super.enqueue(_))

      while (this.size > this.maxSize)
        this.dequeue()
    }
  }
}
