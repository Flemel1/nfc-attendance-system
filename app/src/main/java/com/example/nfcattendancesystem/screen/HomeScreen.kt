package com.example.nfcattendancesystem.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.nfcattendancesystem.data.CollegeClass
import com.example.nfcattendancesystem.screen.route.AppScreen
import com.example.nfcattendancesystem.viewmodel.BackendViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: BackendViewModel, modifier: Modifier = Modifier) {
    val backendDataState by viewModel.classCollege.collectAsStateWithLifecycle()

    fun moveToClassScreen(classCollege: CollegeClass) {
        viewModel.selectClass(classCollege)
        navController.navigate(AppScreen.Class.name)
    }

    LaunchedEffect(Unit) {
        viewModel.fetchAllClass()
    }

    with(backendDataState) {
        if (isEmpty()) {
            return Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val list = this
            return LazyColumn(modifier = modifier.fillMaxWidth()) {
                items(items = list) { collegeClass ->
                    HomeCard(collegeClass = collegeClass, onClick = { moveToClassScreen(collegeClass) })
                }
            }
        }
    }
}

@Composable
fun HomeCard(collegeClass: CollegeClass, onClick: (CollegeClass) -> Unit) {
    Column(modifier = Modifier
        .padding(all = 10.dp)
        .fillMaxWidth()
        .background(color = Color.Green)
        .padding(all = 20.dp)
        .clickable {
            onClick(collegeClass)
        }
    ) {
        Text(
            collegeClass.class_name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White)
        Text(
            "Ruang: ${collegeClass.class_room}",
            color = Color.White)
        Text(
            "Hari: ${collegeClass.class_day}",
            color = Color.White)
        Text(
            "Jam Mulai: ${collegeClass.class_start}",
            color = Color.White)
    }
}