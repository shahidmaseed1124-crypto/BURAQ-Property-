package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.CalculatorScreen
import com.example.ui.screens.CompareScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PostPropertyScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.PropertyDetailScreen
import com.example.ui.screens.VisitsScreen
import com.example.ui.theme.AmberGold
import com.example.ui.theme.ApnaGharTheme
import com.example.ui.theme.EmeraldTeal
import com.example.viewmodel.AppScreen
import com.example.viewmodel.PropertyViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ApnaGharTheme {
        ApnaGharApp()
      }
    }
  }
}

@Composable
fun ApnaGharApp(
  viewModel: PropertyViewModel = viewModel()
) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val selectedProperty by viewModel.selectedProperty.collectAsState()
  val snackbarHostState = remember { SnackbarHostState() }

  // Listen for snackbar notifications
  LaunchedEffect(Unit) {
    viewModel.snackbarMessage.collect { message ->
      snackbarHostState.showSnackbar(message)
    }
  }

  // Determine if bottom navigation should be visible
  val showBottomBar = currentScreen != AppScreen.DETAIL &&
    currentScreen != AppScreen.POST_PROPERTY &&
    currentScreen != AppScreen.COMPARE

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    bottomBar = {
      if (showBottomBar) {
        NavigationBar(
          containerColor = MaterialTheme.colorScheme.surface,
          tonalElevation = 6.dp
        ) {
          NavigationBarItem(
            selected = currentScreen == AppScreen.HOME,
            onClick = { viewModel.navigateTo(AppScreen.HOME) },
            icon = {
              Icon(
                imageVector = if (currentScreen == AppScreen.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                contentDescription = "Home"
              )
            },
            label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = EmeraldTeal,
              indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_home")
          )

          NavigationBarItem(
            selected = currentScreen == AppScreen.EXPLORE,
            onClick = { viewModel.navigateTo(AppScreen.EXPLORE) },
            icon = {
              Icon(
                imageVector = if (currentScreen == AppScreen.EXPLORE) Icons.Filled.Explore else Icons.Outlined.Explore,
                contentDescription = "Explore"
              )
            },
            label = { Text("Browse", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = EmeraldTeal,
              indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_explore")
          )

          NavigationBarItem(
            selected = false,
            onClick = { viewModel.navigateTo(AppScreen.POST_PROPERTY) },
            icon = {
              Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Post",
                tint = AmberGold,
                modifier = Modifier.size(28.dp)
              )
            },
            label = { Text("Post", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AmberGold) },
            modifier = Modifier.testTag("nav_post")
          )

          NavigationBarItem(
            selected = currentScreen == AppScreen.SAVED,
            onClick = { viewModel.navigateTo(AppScreen.SAVED) },
            icon = {
              Icon(
                imageVector = if (currentScreen == AppScreen.SAVED) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Saved"
              )
            },
            label = { Text("Saved", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = EmeraldTeal,
              indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_saved")
          )

          NavigationBarItem(
            selected = currentScreen == AppScreen.VISITS,
            onClick = { viewModel.navigateTo(AppScreen.VISITS) },
            icon = {
              Icon(
                imageVector = if (currentScreen == AppScreen.VISITS) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                contentDescription = "Tours"
              )
            },
            label = { Text("Tours", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = EmeraldTeal,
              indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_visits")
          )
        }
      }
    }
  ) { innerPadding ->
    AnimatedContent(
      targetState = currentScreen,
      transitionSpec = { fadeIn() togetherWith fadeOut() },
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      label = "screen_transition"
    ) { screen ->
      when (screen) {
        AppScreen.HOME -> HomeScreen(viewModel = viewModel)
        AppScreen.EXPLORE -> ExploreScreen(viewModel = viewModel)
        AppScreen.POST_PROPERTY -> PostPropertyScreen(viewModel = viewModel)
        AppScreen.SAVED -> FavoritesScreen(viewModel = viewModel)
        AppScreen.VISITS -> VisitsScreen(viewModel = viewModel)
        AppScreen.CALCULATOR -> CalculatorScreen(viewModel = viewModel)
        AppScreen.DETAIL -> {
          selectedProperty?.let { prop ->
            PropertyDetailScreen(property = prop, viewModel = viewModel)
          } ?: HomeScreen(viewModel = viewModel)
        }
        AppScreen.COMPARE -> CompareScreen(viewModel = viewModel)
      }
    }
  }
}
