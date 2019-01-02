package de.ph1b.audiobook.features.bookOverview.list

import androidx.annotation.FloatRange
import de.ph1b.audiobook.data.Book
import de.ph1b.audiobook.features.bookOverview.list.header.BookOverviewCategory

sealed class BookOverviewItem

data class BookOverviewHeaderModel(
  val category: BookOverviewCategory,
  val hasMore: Boolean
) : BookOverviewItem()

data class BookOverviewModel(
  val name: String,
  val author: String?,
  val transitionName: String,
  @FloatRange(from = 0.0, to = 1.0)
  val progress: Float,
  val book: Book,
  val remainingTimeInMs: Int,
  val isCurrentBook: Boolean
) : BookOverviewItem() {

  constructor(book: Book, isCurrentBook: Boolean) : this(
    name = book.name,
    author = book.author,
    transitionName = book.coverTransitionName,
    book = book,
    progress = book.progress(),
    remainingTimeInMs = book.remainingTimeInMs(),
    isCurrentBook = isCurrentBook
  )

  fun areContentsTheSame(other: BookOverviewModel): Boolean {
    val oldBook = book
    val newBook = other.book
    return oldBook.id == newBook.id &&
        oldBook.content.position == newBook.content.position &&
        name == other.name &&
        isCurrentBook == other.isCurrentBook
  }

  fun areItemsTheSame(other: BookOverviewModel): Boolean {
    return book.id == other.book.id
  }
}

private fun Book.progress(): Float {
  val globalPosition = content.position
  val totalDuration = content.duration
  return (globalPosition.toFloat() / totalDuration.toFloat())
    .coerceAtMost(1F)
}

private fun Book.remainingTimeInMs(): Int {
  return content.duration - content.position
}