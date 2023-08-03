package ru.akimslava.bookshelf.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.akimslava.bookshelf.R
import ru.akimslava.bookshelf.ui.model.BookshelfViewModel
import ru.akimslava.bookshelf.ui.screens.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfApp() {
    val viewModel: BookshelfViewModel = viewModel(
        factory = BookshelfViewModel.Factory,
    )
    Scaffold(topBar = { BookshelfAppBar() }) {
        HomeScreen(
            viewModel = viewModel,
            modifier = Modifier.padding(it),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookshelfAppBar() {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(R.string.bookshelf)) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    )
}