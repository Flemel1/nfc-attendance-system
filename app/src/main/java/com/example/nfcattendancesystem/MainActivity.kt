package com.example.nfcattendancesystem

import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nfcattendancesystem.data.UserLogin
import com.example.nfcattendancesystem.ui.theme.NFCAttendanceSystemTheme
import com.example.nfcattendancesystem.viewmodel.BackendViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val backendViewModel: BackendViewModel by viewModels<BackendViewModel>()
        setContent {
            NFCAttendanceSystemTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginView(viewModel = backendViewModel,Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun LoginView(viewModel: BackendViewModel,modifier: Modifier = Modifier) {
    val loginState by viewModel.userLoginResponse.collectAsStateWithLifecycle()
    val mContext = LocalContext.current
    var username: String by remember {
        mutableStateOf("")
    }

    var password: String by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        val loginImagePainter = painterResource(id = R.drawable.login_image_view)

        Image(
            modifier = Modifier.height(300.dp).fillMaxWidth(),
            painter = loginImagePainter,
            contentScale = ContentScale.Fit,
            contentDescription = "Login Image")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { username = it },
            placeholder = { Text("username", color = Color.Gray)},
            leadingIcon = {
                Icon(imageVector = Icons.Filled.AccountCircle,
                contentDescription = null, tint = Color.Green)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { password = it },
            placeholder = { Text("password", color = Color.Gray)},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Lock,
                    contentDescription = null, tint = Color.Green)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonColors(
                containerColor = Color.Green,
                disabledContentColor = Color.Gray,
                disabledContainerColor = Color.Gray,
                contentColor = Color.White),
            onClick = {
                viewModel.login(UserLogin(username = username, password = password))

                loginState?.let {
                    if (it.status_code == 200) {
                        Toast.makeText(mContext, "Berhasil Login", Toast.LENGTH_SHORT).show()
                        val sharedPref = mContext.getSharedPreferences("login_data",
                            Context.MODE_PRIVATE)
                        with (sharedPref.edit()) {
                            putString("user_id", it.id.toString())
                            apply()
                        }
                        mContext.startActivity(Intent(mContext, HomeActivity::class.java))
                    } else {
                        Toast.makeText(mContext, "Gagal Login", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
            Text(text = "Masuk")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun LoginViewPreview() {
//    NFCAttendanceSystemTheme {
//        LoginView()
//    }
//}