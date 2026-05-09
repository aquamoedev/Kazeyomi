package com.kazeyomi.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import com.kazeyomi.domain.model.Manga

@Composable
fun MangaGridItem(manga: Manga, onClick: () -> Unit) {
    Card(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Column {
            Box {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(manga.thumbnailUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = manga.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    },
                    error = {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("?", style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                )
                if (manga.unreadCount > 0) {
                    Badge(
                        modifier = Modifier.align(Alignment.TopEnd).padding(4.dp),
                        containerColor = MaterialTheme.colorScheme.error
                    ) {
                        Text("${manga.unreadCount}")
                    }
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(manga.title, style = MaterialTheme.typography.bodyMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                if (manga.author != null) {
                    Text(manga.author, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
fun MangaGrid(mangas: List<Manga>, onMangaClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(mangas, key = { it.id }) { manga ->
            MangaGridItem(manga = manga, onClick = { onMangaClick(manga.id) })
        }
    }
}
