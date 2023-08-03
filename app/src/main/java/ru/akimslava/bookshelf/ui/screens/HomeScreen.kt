package ru.akimslava.bookshelf.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.akimslava.bookshelf.R
import ru.akimslava.bookshelf.models.BookData
import ru.akimslava.bookshelf.ui.model.BookshelfUiState
import ru.akimslava.bookshelf.ui.model.BookshelfViewModel

@Composable
fun HomeScreen(
    viewModel: BookshelfViewModel,
    onBookClick: (BookData) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState = viewModel.uiState
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SearchRow(
            viewModel = viewModel,
            onSearchClick = onSearchClick,
        )
        when (uiState) {
            BookshelfUiState.Home -> HomeScreenPart()
            is BookshelfUiState.Success -> SuccessScreen(
                uiState = uiState,
                onBookClick = onBookClick,
            )
            BookshelfUiState.Loading -> LoadingScreen()
            BookshelfUiState.Error -> ErrorScreen(
                retryAction = viewModel::searchBooks
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun SearchRow(
    viewModel: BookshelfViewModel,
    onSearchClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = viewModel.searchQuery.value,
            onValueChange = viewModel::setSearchQuery,
            label = { Text(text = stringResource(R.string.enter_a_book_title)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    viewModel.searchBooks()
                }
            ),
            singleLine = true,
            maxLines = 1,
        )
        Spacer(modifier = Modifier.size(20.dp))
        IconButton(onClick = onSearchClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = stringResource(R.string.search_button),
                tint = MaterialTheme.colorScheme.surfaceTint,
            )
        }
    }
}

@Composable
private fun HomeScreenPart() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = null,
            modifier = Modifier.size(200.dp),
        )
        Text(text = stringResource(R.string.home_screen_plug))
    }
}

@Composable
private fun SuccessScreen(
    uiState: BookshelfUiState,
    onBookClick: (BookData) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 8.dp,
                end = 8.dp,
                bottom = 8.dp,
            ),
    ) {
        val resultsCount =
            (uiState as BookshelfUiState.Success).requestData.totalItems
        Text(
            text = stringResource(id = R.string.found_results)
                .format(resultsCount),
            color = Color.LightGray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    bottom = 8.dp,
                ),
        )
        val bookList = uiState.requestData.items ?: listOf()
        BookCover(
            bookList = bookList,
            onBookClick = onBookClick,
        )
    }
}

@Composable
private fun BookCover(
    bookList: List<BookData>,
    onBookClick: (BookData) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(bookList) { book ->
            val bookThumbnail = book
                .volumeInfo
                ?.imageLinks
                ?.thumbnail
                ?.replaceFirst("http", "https")
            if (bookThumbnail != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(bookThumbnail)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.padding(4.dp)
                        .clickable { onBookClick(book) },
                    placeholder = painterResource(id = R.drawable.ic_downloading),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_no_image),
                    contentDescription = stringResource(R.string.no_image),
                    modifier = Modifier.clickable { onBookClick(book) },
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.search_in_progress),
        )
    }
}

@Composable
private fun ErrorScreen(retryAction: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_no_internet),
            contentDescription = stringResource(R.string.no_internet_connection),
            modifier = Modifier.size(200.dp),
        )
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}