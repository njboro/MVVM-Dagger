package com.example.app_mvvm_compose.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.app_mvvm_compose.model.Desh
import com.example.app_mvvm_compose.ui.theme.SubTitle_TextColor
import com.example.app_mvvm_compose.ui.theme.Title_TextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppBar(title: String, scrollBehavior: TopAppBarScrollBehavior, backClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = backClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

@Composable
fun DetailScreen(country: Desh) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ){
        AsyncImage(
            model = country.flag,
            contentDescription = "Coil image composable",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
        Text(
            text = country.countryName,
            color = Title_TextColor,
            fontSize = 28.sp,
            modifier = Modifier.padding(12.dp)
        )
        country.capital?.let {
            Text(
                text = it,
                color = SubTitle_TextColor,
                fontSize = 18.sp,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}