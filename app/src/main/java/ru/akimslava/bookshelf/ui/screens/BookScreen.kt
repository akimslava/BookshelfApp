package ru.akimslava.bookshelf.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.akimslava.bookshelf.R
import ru.akimslava.bookshelf.models.BookData
import ru.akimslava.bookshelf.models.ImageLinksData
import ru.akimslava.bookshelf.models.VolumeInfoData

@Composable
fun BookScreen(
    book: BookData,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler(enabled = true, onBack = onBackPressed)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val imageUrl = book
            .volumeInfo
            ?.imageLinks
            ?.thumbnail
            ?.replaceFirst("http", "https")
        if (imageUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = modifier.size(300.dp),
                alignment = Alignment.Center,
                contentScale = ContentScale.FillHeight,
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_no_image),
                contentDescription = stringResource(id = R.string.no_image),
                modifier = modifier.size(300.dp),
                alignment = Alignment.Center,
                contentScale = ContentScale.FillHeight,
            )
        }
        Text(
            text = book.volumeInfo?.title ?: stringResource(R.string.no_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        val description = book.volumeInfo?.description
        if (description != null) {
            Text(
                text = description,
                modifier = modifier,
            )
        }
        val pageCount = book.volumeInfo?.pageCount
        if (pageCount != null) {
            Text(
                text = stringResource(R.string.page_count).format(pageCount),
                modifier = modifier,
            )
        }
        val publisher = book.volumeInfo?.publisher
        val publishedDate = book.volumeInfo?.publishedDate
        if (publisher != null) {
            var publisherText = publisher
            if (publishedDate != null) publisherText += ", $publishedDate"
            Text(
                text = publisherText,
                modifier = modifier.align(Alignment.End),
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun BookScreenPreview() {
    MaterialTheme {
        BookScreen(
            book = BookData(
                volumeInfo = VolumeInfoData(
                    title = "1984",
                    authors = listOf("Джордж Оруэлл"),
                    publisher = "Litres",
                    publishedDate = "2021-01-01",
                    description = "Что будет, если в правящих кругах распространятся идеи фашизма и диктатуры? Каким станет общественный уклад, если власть потребует неуклонного подчинения? К какой катастрофе приведет подобный режим?В государстве Океания у граждан нет прав, а значит, власть партии абсолютна. Повседневная жизнь героев проходит в условиях страха и постоянных потрясений. Здесь перечеркнуты свобода слова, свобода печати, свобода выбора, свобода любви и даже свобода мысли. Нарушить порядок невозможно, ведь Старший Брат смотрит за каждым.Ужасы преступлений государственной машины зафиксировал Джордж Оруэлл в своем главном творении – «1984». Этот роман-предупреждение об опасности тоталитаризма стал одной из самых известных антиутопий XX века, которая стоит в одном ряду с «Мы» Замятина, «О дивный новый мир» Хаксли и «451° по Фаренгейту» Брэдбери.",
                    pageCount = 313,
                    imageLinks = ImageLinksData(
                        thumbnail = "http://books.google.com/books/content?id=uaoREAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
                    )
                )
            ),
            onBackPressed = {},
        )
    }
}