package com.kazeyomi.ui.screens.manga
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.kazeyomi.domain.model.Chapter
import com.kazeyomi.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailScreen(mangaId: Int, onBackClick: () -> Unit, onChapterClick: (Int) -> Unit, viewModel: MangaDetailViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(mangaId) { viewModel.loadManga(mangaId) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(state.manga?.title ?: "Details", maxLines = 1, overflow = TextOverflow.Ellipsis) }, navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Back") } }, actions = {
            state.manga?.let { IconButton(onClick = { viewModel.toggleFavorite() }) { Icon(if (it.inLibrary) Icons.Default.Favorite else Icons.Default.FavoriteBorder, "Favorite", tint = if (it.inLibrary) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface) } }
        }) }
    ) { padding ->
        when {
            state.isLoading && state.manga == null -> LoadingIndicator(Modifier.padding(padding))
            state.error != null && state.manga == null -> ErrorMessage(state.error!!, { viewModel.loadManga(mangaId) }, Modifier.padding(padding))
            state.manga != null -> {
                val m = state.manga!!
                LazyColumn(Modifier.fillMaxSize().padding(padding)) {
                    item {
                        Column(Modifier.padding(16.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                SubcomposeAsyncImage(model = ImageRequest.Builder(LocalContext.current).data(m.thumbnailUrl).crossfade(true).build(), contentDescription = m.title, modifier = Modifier.width(120.dp).height(180.dp), contentScale = ContentScale.Crop, loading = { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() } })
                                Column(Modifier.weight(1f)) {
                                    Text(m.title, style = MaterialTheme.typography.headlineSmall)
                                    m.author?.let { Text("Author: $it", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                                    m.artist?.let { Text("Artist: $it", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                                    Spacer(Modifier.height(8.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        AssistChip(onClick = {}, label = { Text(m.status.name) })
                                        AssistChip(onClick = {}, label = { Text("${m.chapterCount} ch") }, leadingIcon = { Icon(Icons.Default.MenuBook, null, Modifier.size(18.dp)) })
                                    }
                                    m.genre?.let { genres ->
                                        Spacer(Modifier.height(8.dp))
                                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            genres.take(4).forEach { g -> AssistChip(onClick = {}, label = { Text(g) }) }
                                        }
                                    }
                                }
                            }
                            if (!m.description.isNullOrEmpty()) { Spacer(Modifier.height(12.dp)); Text(m.description, style = MaterialTheme.typography.bodyMedium) }
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = { viewModel.fetchChapters() }, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.Refresh, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("Fetch New Chapters") }
                        }
                        Divider()
                        SectionHeader("Chapters")
                    }
                    items(state.chapters, key = { it.id }) { chapter ->
                        ListItem(
                            headlineContent = { Text(chapter.title.ifEmpty { chapter.name }, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                            supportingContent = { Text(if (chapter.read) "Read  •  ${chapter.uploadDate}" else "Unread") },
                            leadingContent = { if (!chapter.read) { Icon(Icons.Default.Circle, null, Modifier.size(12.dp), tint = MaterialTheme.colorScheme.primary) } },
                            trailingContent = { Row { if (chapter.bookmarked) Icon(Icons.Default.Bookmark, "Bookmarked", tint = MaterialTheme.colorScheme.primary); IconButton(onClick = { onChapterClick(chapter.id) }) { Icon(Icons.Default.PlayArrow, "Read") } } },
                            modifier = Modifier.clickable { onChapterClick(chapter.id) }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}
