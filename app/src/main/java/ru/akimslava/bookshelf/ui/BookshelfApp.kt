package ru.akimslava.bookshelf.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.akimslava.bookshelf.R
import ru.akimslava.bookshelf.ui.model.BookshelfViewModel
import ru.akimslava.bookshelf.ui.screens.BookScreen
import ru.akimslava.bookshelf.ui.screens.HomeScreen

enum class BookshelfScreen {
    HomeScreen,
    BookScreen,
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun BookshelfApp(
    viewModel: BookshelfViewModel = viewModel(
        factory = BookshelfViewModel.Factory,
    ),
    navController: NavHostController = rememberNavController(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(topBar = {
        BookshelfAppBar(
            title = viewModel.title.value,
            onBackPressed = {
                viewModel.setDefaultTitle()
                navController.navigateUp()
            },
            canNavigateBack = navController.previousBackStackEntry?.destination != null,
        )
    }) {
        NavHost(
            navController = navController,
            startDestination = BookshelfScreen.HomeScreen.name,
            modifier = Modifier.padding(it),
        ) {
            composable(route = BookshelfScreen.HomeScreen.name) {
                HomeScreen(
                    viewModel = viewModel,
                    onBookClick = { book ->
                        viewModel.setCurrentBook(book)
                        val title = viewModel.currentBook
                            .value
                            .volumeInfo
                            ?.title ?: "~No title~"
                        viewModel.setTitle(title)
                        navController.navigate(
                            route = BookshelfScreen.BookScreen.name
                        )
                    },
                    onSearchClick = {
                        keyboardController?.hide()
                        viewModel.searchBooks()
                    },
                )
            }
            composable(route = BookshelfScreen.BookScreen.name) {
                BookScreen(
                    book = viewModel.currentBook.value,
                    onBackPressed = {
                        viewModel.setDefaultTitle()
                        navController.navigateUp()
                    },
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookshelfAppBar(
    title: String,
    onBackPressed: () -> Unit,
    canNavigateBack: Boolean,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.back),
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    )
}