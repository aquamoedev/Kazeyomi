package com.kazeyomi.ui.screens.reader
import com.kazeyomi.ui.components.LoadingIndicator
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import com.kazeyomi.domain.model.ReadingMode

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ReaderScreen(mangaId: Int, initialChapterId: Int, onBackClick: () -> Unit, viewModel: ReaderViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    var showControls by remember { mutableStateOf(true) }
    LaunchedEffect(mangaId, initialChapterId) { viewModel.loadChapter(mangaId, initialChapterId) }

    Box(Modifier.fillMaxSize().background(Color.Black).pointerInput(Unit) { detectTapGestures { showControls = !showControls } }) {
        when {
            state.isLoading -> LoadingIndicator(Modifier.align(Alignment.Center))
            state.error != null -> Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) { Text(state.error!!, color = Color.White); Button(onClick = { viewModel.loadChapter(mangaId, initialChapterId) }) { Text("Retry") } }
            state.pages.isNotEmpty() -> {
                when (state.settings.mode) {
                    ReadingMode.VERTICAL, ReadingMode.WEBTOON -> VerticalReader(state.pages, state.currentPage, { viewModel.setPage(it) }, showControls)
                    ReadingMode.HORIZONTAL -> HorizontalReader(state.pages, state.currentPage, { viewModel.setPage(it) }, showControls)
                }
            }
        }
        if (showControls) ReaderControls(state.currentPage, state.pages.size, state.chapterTitle, onBackClick, { viewModel.prevChapter() }, { viewModel.nextChapter() }, state.hasPrevChapter, state.hasNextChapter, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun VerticalReader(pages: List<com.kazeyomi.domain.model.ChapterPage>, curPage: Int, onPageChanged: (Int) -> Unit, showControls: Boolean) {
    val listState = rememberLazyListState(curPage)
    LaunchedEffect(curPage) { listState.animateScrollToItem(curPage) }
    Box(Modifier.fillMaxSize()) {
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(top = if (showControls) 80.dp else 0.dp, bottom = if (showControls) 80.dp else 0.dp)) {
            items(pages, key = { it.index }) { page ->
                SubcomposeAsyncImage(model = ImageRequest.Builder(LocalContext.current).data(page.imageUrl).crossfade(true).build(), contentDescription = "Page ${page.index}", modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth, loading = { Box(Modifier.fillMaxWidth().height(400.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Color.White) } })
            }
        }
        if (showControls) Slider(value = curPage.toFloat(), onValueChange = { onPageChanged(it.toInt()) }, valueRange = 0f..(pages.size - 1).coerceAtLeast(0).toFloat(), modifier = Modifier.align(Alignment.BottomCenter).padding(horizontal = 16.dp, vertical = 32.dp))
    }
}

@Composable
fun HorizontalReader(pages: List<com.kazeyomi.domain.model.ChapterPage>, curPage: Int, onPageChanged: (Int) -> Unit, showControls: Boolean) {
    val pagerState = rememberPagerState(initialPage = curPage) { pages.size }
    LaunchedEffect(pagerState.currentPage) { onPageChanged(pagerState.currentPage) }
    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { idx ->
        SubcomposeAsyncImage(model = ImageRequest.Builder(LocalContext.current).data(pages[idx].imageUrl).crossfade(true).build(), contentDescription = "Page $idx", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit, loading = { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Color.White) } })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderControls(curPage: Int, total: Int, title: String, onBack: () -> Unit, onPrev: () -> Unit, onNext: () -> Unit, hasPrev: Boolean, hasNext: Boolean, modifier: Modifier) {
    Column(modifier.fillMaxWidth()) {
        TopAppBar(title = { Text(title, color = Color.White) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back", tint = Color.White) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black.copy(alpha = 0.7f)))
        Row(Modifier.fillMaxWidth().background(Color.Black.copy(alpha = 0.7f)).padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onPrev, enabled = hasPrev) { Icon(Icons.Default.SkipPrevious, "Prev", tint = if (hasPrev) Color.White else Color.Gray) }
            Text("${curPage + 1} / $total", color = Color.White, style = MaterialTheme.typography.bodyLarge)
            IconButton(onClick = onNext, enabled = hasNext) { Icon(Icons.Default.SkipNext, "Next", tint = if (hasNext) Color.White else Color.Gray) }
        }
    }
}
