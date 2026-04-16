package com.kazeyomi.ui.screens.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.kazeyomi.domain.model.ChapterPage
import com.kazeyomi.domain.model.ReadingDirection
import com.kazeyomi.domain.model.ReadingMode
import com.kazeyomi.ui.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    mangaId: Int,
    initialChapterId: Int,
    onBackClick: () -> Unit,
    viewModel: ReaderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showControls by remember { mutableStateOf(false) }

    LaunchedEffect(mangaId, initialChapterId) {
        viewModel.loadChapter(mangaId, initialChapterId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { showControls = !showControls }
                )
            }
    ) {
        when {
            uiState.isLoading -> LoadingIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
            uiState.error != null -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Text(uiState.error!!, color = Color.White)
                    Button(onClick = { viewModel.loadChapter(mangaId, initialChapterId) }) {
                        Text("Retry")
                    }
                }
            }
            uiState.pages.isNotEmpty() -> {
                when (uiState.settings.mode) {
                    ReadingMode.VERTICAL, ReadingMode.WEBTOON -> {
                        VerticalReader(
                            pages = uiState.pages,
                            currentPage = uiState.currentPage,
                            onPageChanged = { viewModel.setCurrentPage(it) },
                            showControls = showControls,
                            settings = uiState.settings,
                            onPreviousChapter = { viewModel.loadPreviousChapter() },
                            onNextChapter = { viewModel.loadNextChapter() }
                        )
                    }
                    ReadingMode.HORIZONTAL_SINGLE, ReadingMode.HORIZONTAL_CONTINUOUS -> {
                        HorizontalReader(
                            pages = uiState.pages,
                            currentPage = uiState.currentPage,
                            onPageChanged = { viewModel.setCurrentPage(it) },
                            showControls = showControls,
                            settings = uiState.settings
                        )
                    }
                }
            }
        }

        if (showControls) {
            ReaderControls(
                currentPage = uiState.currentPage,
                totalPages = uiState.pages.size,
                chapterTitle = uiState.chapterTitle,
                onBackClick = onBackClick,
                onPreviousChapter = { viewModel.loadPreviousChapter() },
                onNextChapter = { viewModel.loadNextChapter() },
                hasPreviousChapter = uiState.hasPreviousChapter,
                hasNextChapter = uiState.hasNextChapter,
                onSettingsClick = { },
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun VerticalReader(
    pages: List<ChapterPage>,
    currentPage: Int,
    onPageChanged: (Int) -> Unit,
    showControls: Boolean,
    settings: com.kazeyomi.domain.model.ReaderSettings,
    onPreviousChapter: () -> Unit,
    onNextChapter: () -> Unit
) {
    val listState = rememberLazyListState(currentPage)

    LaunchedEffect(currentPage) {
        listState.animateScrollToItem(currentPage)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = if (showControls) 80.dp else 0.dp,
            bottom = if (showControls) 80.dp else 0.dp
        )
    ) {
        items(pages, key = { it.index }) { page ->
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(page.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Page ${page.index}",
                modifier = Modifier.fillMaxWidth(),
                contentScale = if (settings.mode == ReadingMode.WEBTOON) ContentScale.FillWidth else ContentScale.FillWidth,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            )
        }
    }

    if (showControls) {
        Slider(
            value = currentPage.toFloat(),
            onValueChange = { onPageChanged(it.toInt()) },
            valueRange = 0f..(pages.size - 1).coerceAtLeast(0).toFloat(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 32.dp)
        )
    }
}

@Composable
fun HorizontalReader(
    pages: List<ChapterPage>,
    currentPage: Int,
    onPageChanged: (Int) -> Unit,
    showControls: Boolean,
    settings: com.kazeyomi.domain.model.ReaderSettings
) {
    val pagerState = rememberPagerState(initialPage = currentPage) { pages.size }
    val isRtl = settings.direction == ReadingDirection.RIGHT_TO_LEFT

    LaunchedEffect(pagerState.currentPage) {
        onPageChanged(pagerState.currentPage)
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        reverseLayout = isRtl
    ) { pageIndex ->
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pages[pageIndex].imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Page ${pageIndex}",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        )
    }
}

@Composable
fun ReaderControls(
    currentPage: Int,
    totalPages: Int,
    chapterTitle: String,
    onBackClick: () -> Unit,
    onPreviousChapter: () -> Unit,
    onNextChapter: () -> Unit,
    hasPreviousChapter: Boolean,
    hasNextChapter: Boolean,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TopAppBar(
            title = { Text(chapterTitle, color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            actions = {
                IconButton(onClick = onSettingsClick) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Black.copy(alpha = 0.7f)
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPreviousChapter,
                enabled = hasPreviousChapter
            ) {
                Icon(
                    Icons.Default.SkipPrevious,
                    contentDescription = "Previous Chapter",
                    tint = if (hasPreviousChapter) Color.White else Color.Gray
                )
            }

            Text(
                text = "${currentPage + 1} / $totalPages",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )

            IconButton(
                onClick = onNextChapter,
                enabled = hasNextChapter
            ) {
                Icon(
                    Icons.Default.SkipNext,
                    contentDescription = "Next Chapter",
                    tint = if (hasNextChapter) Color.White else Color.Gray
                )
            }
        }
    }
}
