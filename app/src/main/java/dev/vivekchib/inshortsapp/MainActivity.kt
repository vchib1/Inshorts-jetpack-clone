package dev.vivekchib.inshortsapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.vivekchib.inshortsapp.core.theme.InshortsappTheme
import dev.vivekchib.inshortsapp.presentation.bookmark.BookmarkScreen
import dev.vivekchib.inshortsapp.presentation.bookmark.viewmodel.BookmarkViewModel
import dev.vivekchib.inshortsapp.presentation.home.HomeScreen
import dev.vivekchib.inshortsapp.presentation.home.viewmodel.HomeViewModel

@AndroidEntryPoint
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InshortsappTheme {
                val context = LocalContext.current

                val navController = rememberNavController()

                val homeViewModel = hiltViewModel<HomeViewModel>()
                val bookmarkViewModel = hiltViewModel<BookmarkViewModel>()

                val navItemsList = listOf(
                    "Home" to Icons.Default.Home,
                    "Bookmark" to R.drawable.ic_bookmark_enabled,
                )

                Scaffold(
                    Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {

                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination

                            navItemsList.forEach { (title, icon) ->
                                NavigationBarItem(
                                    selected = currentDestination?.hierarchy?.any {
                                        it.hasRoute(title, arguments = null)
                                    } == true,
                                    onClick = {
                                        navController.navigate(title) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    label = { Text(text = title) },
                                    icon = {
                                        if (icon is Int) {
                                            Icon(
                                                painter = painterResource(id = icon),
                                                contentDescription = null
                                            )
                                        } else if (icon is ImageVector) {
                                            Icon(icon, contentDescription = null)
                                        }

                                    },
                                )
                            }
                        }
                    },
                ) { it ->
                    NavHost(
                        navController = navController,
                        startDestination = navItemsList.first().first
                    ) {

                        // HOME
                        composable("Home") {
                            val state = homeViewModel.state.collectAsStateWithLifecycle()
                            HomeScreen(
                                state = state.value,
                                onEvent = homeViewModel::onEvent,
                                openUrl = { openUrlActivity(context, it) }
                            )
                        }

                        // BOOKMARK
                        composable("Bookmark") {
                            val state = bookmarkViewModel.state.collectAsStateWithLifecycle()
                            BookmarkScreen(
                                articles = state.value,
                                onDelete = { bookmarkedArticle ->
                                    bookmarkViewModel.removeBookmark(bookmarkedArticle)
                                },
                                openUrl = { openUrlActivity(context, it) }
                            )
                        }
                    }
                }

            }
        }
    }
}

fun openUrlActivity(context: Context, url: String) {
    try {
        var url = url
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://$url"
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(context, intent, null)
    } catch (_: Exception) {
        Toast.makeText(context, "Unable to open the URL", Toast.LENGTH_SHORT).show()
    }
}




