package com.example.rakaminmandirivinka

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.rakaminmandirivinka.model.Article
import com.example.rakaminmandirivinka.ui.theme.RakaminMandiriVinkaTheme
import com.example.rakaminmandirivinka.viewmodel.NewsViewModel
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RakaminMandiriVinkaTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "newsList") {
                        composable("newsList") { NewsScreen(navController, newsViewModel) }
                        composable("newsDetail/{article}") { backStackEntry ->
                            val articleJson = Uri.decode(backStackEntry.arguments?.getString("article"))
                            val article = Gson().fromJson(articleJson, Article::class.java)
                            NewsDetailScreen(navController, article)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun NewsScreen(navController: NavHostController, newsViewModel: NewsViewModel = viewModel()) {
    val articles = newsViewModel.articles.collectAsState().value

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Text(
                text = "Mandiri News",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 28.dp, bottom = 4.dp)
                    .fillMaxWidth()
            )
        }

        item {
            Text(
                text = "Top Headlines in the US",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )
        }
        item {
            Divider(
                color = MaterialTheme.colorScheme.primary,
                thickness = 2.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }
        items(articles) { article ->
            ArticleItem(article) {
                val articleJson = Uri.encode(Gson().toJson(article))
                navController.navigate("newsDetail/$articleJson")
            }
        }
    }
}



@Composable
fun ArticleItem(article: Article, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable { onClick() }) {
        article.urlToImage?.let { url ->
            Image(
                painter = rememberAsyncImagePainter(url),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = article.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = article.description ?: "No Description",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun NewsDetailScreen(navController: NavHostController, article: Article) {
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        item {
            article.urlToImage?.let { url ->
                Image(
                    painter = rememberAsyncImagePainter(url),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "By ${article.author ?: "Unknown"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Published at ${article.publishedAt}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.content ?: "No Content",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = Int.MAX_VALUE
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.popBackStack() },
            ) {
                Text(text = "Back")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RakaminMandiriVinkaTheme {
        val sampleArticle = Article(
            source = com.example.rakaminmandirivinka.model.Source(id = null, name = "Sample Source"),
            title = "Sample Title",
            description = "Sample Description",
            url = "https://example.com",
            urlToImage = null,
            publishedAt = "2023-06-30T12:00:00Z",
            content = "Sample Content",
            author = "Author"
        )
        ArticleItem(sampleArticle) {}
    }
}
