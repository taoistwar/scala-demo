package app.review.vo

case class Node(name:String, old: String, news: String) {
  override def toString() = name
}
