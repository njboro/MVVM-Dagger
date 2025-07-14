package com.example.app_mvvm_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.app_mvvm_compose.model.Desh
import com.example.app_mvvm_compose.ui.theme.MVVMDaggerTheme
import com.example.app_mvvm_compose.ui.theme.SubTitle_TextColor
import com.example.app_mvvm_compose.ui.theme.Title_TextColor
import com.example.app_mvvm_compose.utils.getDummyList
import com.example.app_mvvm_compose.viewmodel.AppViewModel
import com.example.app_mvvm_compose.viewmodel.MainViewModel
import com.example.app_mvvm_compose.viewmodel.UIState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MVVMDaggerTheme {
                /*Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        mainViewModel
                    )
                }*/
                SwipeToRefreshComposable()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MVVMDaggerTheme {
            //Greeting("Android")
            val countryList = getDummyList()
            //ListItemComposable("India", "New Delhi", "")
            MyListsComposable(countryList)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, mainViewModel: MainViewModel) {
    //ListItemComposable("India", "New Delhi", "")
    MyListsComposable(getDummyList())
}


@Composable
fun MyListsComposable(listCountry: List<Desh>) {

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
            )
        }
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
fun SwipeToRefreshComposable(viewModel: AppViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsState()

    val isRefreshing = uiState is UIState.Loading

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            viewModel.loadCountries()
        }
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
fun ListItemComposable(countryName: String, capital: String?, imageUrl: String?) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {

            /*Image(
                painter = painterResource(R.drawable.indian_flag),
                contentDescription = "Image of Flag",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(
                        width = 100.dp,
                        height = 80.dp
                    )
                    .padding(5.dp)
            )*/

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
