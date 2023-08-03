package ru.akimslava.bookshelf.ui.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.akimslava.bookshelf.BookshelfApplication
import ru.akimslava.bookshelf.TAG
import ru.akimslava.bookshelf.data.BooksRepository
import ru.akimslava.bookshelf.models.SearchRequestData
import java.io.IOException

sealed interface BookshelfUiState {
    object Home : BookshelfUiState
    data class Success(val requestData: SearchRequestData): BookshelfUiState
    object Loading : BookshelfUiState
    object Error : BookshelfUiState
}

class BookshelfViewModel(
    private val booksRepository: BooksRepository,
): ViewModel() {
    var uiState: BookshelfUiState by mutableStateOf(BookshelfUiState.Home)
        private set

    var searchQuery = mutableStateOf("")
        private set

    fun setSearchQuery(newSearchQuery: String) {
        searchQuery.value = newSearchQuery
    }

    fun searchBooks() {
        viewModelScope.launch {
            uiState = BookshelfUiState.Loading
            uiState = try {
                BookshelfUiState.Success(
                    booksRepository.searchBooks(searchQuery.value)
                )
            } catch (e: IOException) {
                Log.i(TAG, "IO: ${e.message}")
                BookshelfUiState.Error
            } catch (e: HttpException) {
                Log.i(TAG, "Http: ${e.message}")
                BookshelfUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                            as BookshelfApplication
                )
                val bookshelfRepository = application.container.booksRepository
                BookshelfViewModel(bookshelfRepository)
            }
        }
    }
}