package com.example.app_mvvm_compose

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.app_mvvm_compose.model.Desh
import com.example.app_mvvm_compose.ui.theme.MVVMDaggerTheme
import com.example.app_mvvm_compose.ui.theme.SubTitle_TextColor
import com.example.app_mvvm_compose.ui.theme.Title_TextColor
import com.example.app_mvvm_compose.utils.DetailScreen
import com.example.app_mvvm_compose.utils.MyAppBar
import com.example.app_mvvm_compose.utils.getDummyList
import com.example.app_mvvm_compose.viewmodel.AppViewModel
import com.example.app_mvvm_compose.viewmodel.MainViewModel
import com.example.app_mvvm_compose.viewmodel.UIState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MVVMDaggerTheme {
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                Scaffold(
                    topBar = {
                        MyAppBar("List of Countries", scrollBehavior = scrollBehavior) {
                            finish()
                        }
                    },
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                ) {
                    innerPadding ->
                    SwipeToRefreshComposable(modifier = Modifier.padding(innerPadding))
                }

            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MVVMDaggerTheme {
            val countryList = getDummyList()
            //ListItemComposable("India", "New Delhi", "")
            //MyListsComposable(countryList)
            DetailScreen(getDummyList()[0])
        }
    }
}


@Composable
fun MyListsComposable(listCountry: List<Desh>) {

    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = "home"
    ) {
        composable("home") {
            LazyColumn(
                contentPadding = PaddingValues(5.dp),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                items(items = listCountry) { country ->
                    ListItemComposable(
                        countryName = country.countryName,
                        capital = country.capital,
                        imageUrl = country.flag
                    ) {
                        val country = Uri.encode(Gson().toJson(country)) // Encode to pass safely in route
                        navController.navigate("details/$country")
                    }
                }
            }
        }

        composable ("details/{country}"){
            backStackEntry ->
            val countryJson = backStackEntry.arguments?.getString("country")
            val country = Gson().fromJson(countryJson, Desh::class.java)

            DetailScreen(country)
        }
    }

    val currentContext = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    //Intercept Back press
    BackHandler(enabled = true) {
        showDialog = true
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {showDialog = false},
            title = { Text("Exit App")},
            text = { Text("Are you sure you want to exit?")},
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    //Actually exit the app
                    (currentContext as? Activity)?.finish()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text("No")
                }
            }
        )
    }


}

@Composable
fun CircularProgressComposable() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            strokeWidth = 4.dp
        )
    }
}

@Composable
fun ErrorMessageComposable(errorMessage : String, onClick : () -> Unit ) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = errorMessage, color = Color.Red)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onClick) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun SwipeToRefreshComposable(viewModel: AppViewModel = hiltViewModel(), modifier: Modifier) {

    val uiState by viewModel.uiState.collectAsState()

    val isRefreshing = uiState is UIState.Loading

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            viewModel.loadCountries()
        },
        modifier = modifier
    ) {
        when(uiState) {
            is UIState.Loading -> {
                CircularProgressComposable()
            }
            is UIState.Success<*> -> {
                val listCountries = (uiState as UIState.Success<List<Desh>>).data
                MyListsComposable(listCountries)
            }
            is UIState.Error -> {
                val errorMessage = (uiState as UIState.Error).message
                ErrorMessageComposable(errorMessage) {
                    viewModel.loadCountries()
                }
            }
        }
    }

}

@Composable
fun ListItemComposable(countryName: String, capital: String?, imageUrl: String?, onClick: () -> Unit) {

    Card(
        modifier = Modifier.fillMaxWidth().clickable {
            onClick.invoke()
        },
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            MyCoilImage(imageUrl)
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = countryName,
                    color = Title_TextColor,
                    fontSize = 18.sp
                )
                capital?.let {
                    Text(
                        text = it,
                        color = SubTitle_TextColor,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }

}

@Composable
fun MyCoilImage(flagUrl: String?) {
    flagUrl?.let {
        AsyncImage(
            model = flagUrl,
            contentDescription = "Coil image composable",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(
                    width = 100.dp,
                    height = 80.dp
                )
                .padding(5.dp)
        )
    }
}
