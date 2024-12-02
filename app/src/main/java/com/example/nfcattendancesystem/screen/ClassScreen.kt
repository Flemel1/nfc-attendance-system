package com.example.nfcattendancesystem.screen

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nfcattendancesystem.HomeActivity
import com.example.nfcattendancesystem.NfcActivity
import com.example.nfcattendancesystem.data.Presence
import com.example.nfcattendancesystem.viewmodel.BackendViewModel

@Composable
fun ClassScreen(viewModel: BackendViewModel, modifier: Modifier = Modifier) {
    val presences by viewModel.presences.collectAsStateWithLifecycle()
    val selectedClass by viewModel.selectedClass.collectAsStateWithLifecycle()
    val mContext = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchPresenceByClass()
    }

    Column(
        modifier = modifier
            .padding(all = 10.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        selectedClass?.class_name?.let {
            Text(
                text = it,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Ruang: ${selectedClass?.class_room}")
            Text("Hari: ${selectedClass?.class_day}")
        }

        Text(
            text = "Jam Mulai: ${selectedClass?.class_start}",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End)

//        Text(
//            text = "Pengajar: Budiman")

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                modifier = Modifier.width(180.dp),
                colors = ButtonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White,
                    disabledContentColor = Color.Black,
                    disabledContainerColor = Color.Gray),
                onClick = {
                    val intent = Intent(mContext, NfcActivity::class.java)
                    intent.putExtra("class_name", selectedClass?.class_name)
                    intent.putExtra("class_day", selectedClass?.class_day)
                    intent.putExtra("class_start", selectedClass?.class_start)
                    intent.putExtra("class_room", selectedClass?.class_room)
                    intent.putExtra("class_id", selectedClass?.id)
                    mContext.startActivity(intent)
                }) {
                Text(text = "Presensi")
            }

//            Button(
//                modifier = Modifier.width(180.dp),
//                colors = ButtonColors(
//                    containerColor = Color.Green,
//                    contentColor = Color.White,
//                    disabledContentColor = Color.Black,
//                    disabledContainerColor = Color.Gray),
//                onClick = { null }) {
//                Text(text = "Chat")
//            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Daftar Presensi",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        with(presences) {
            return if (isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn {
                    items(items = presences) { presence ->
                        ClassCard(presence)
                    }
                }
            }
        }
    }
}

@Composable
fun ClassCard(data: Presence) {
    Column(modifier = Modifier
        .padding(top = 10.dp)
        .fillMaxWidth()
        .background(color = Color.Green)
        .padding(all = 20.dp)
    ) {
        Text(
            "Tanggal: ${data.presence_date}",
            color = Color.White)
        Text(
            "Jam: ${data.presence_time}",
            color = Color.White)
    }
}