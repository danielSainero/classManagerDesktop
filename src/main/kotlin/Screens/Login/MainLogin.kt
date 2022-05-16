package Screens.Login

import ScreenItems.bigPasswordInput
import ScreenItems.bigPasswordInputWithErrorMessage
import Screens.ScreenItems.Dialogs.loadingDialog
import Screens.ScreenItems.Others.floatToast
import Utils.CommonErrors
import Utils.LazyGridFor
import Utils.isValidEmail
import Utils.isValidPassword
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun MainLogin(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {

    //Help variables
    val composableScope = rememberCoroutineScope()
    var showToast = remember { mutableStateOf(false) }
    var textToast = remember { mutableStateOf("") }
    var loading = remember { mutableStateOf(false) }

    //Texts
    var emailText by remember{ mutableStateOf("test@gmail.com") }
    var passwordText by remember{ mutableStateOf("11111111") }
    var passwordError by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(showToast.value) {
        if (showToast.value) {
            delay(1800L)
            showToast.value = false
        }
    }

    if(loading.value) {
        loadingDialog(
            loading = loading,
            informativeText = "Iniciando sesión"
        )
    }



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .width(500.dp)
                    .height(700.dp)
                    .border(1.dp, Color.LightGray),
                content = {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(text = "Login")
                                },
                            )
                        },
                        floatingActionButton = {
                            if(showToast.value) {
                                floatToast(
                                    message = textToast.value,
                                    showToast = showToast
                                )
                            }
                        },
                        content = {

                            LazyColumn(
                                content = {

                                    item {
                                        Image(
                                            painter = painterResource("logoDani.png"),
                                            contentDescription = "Sample",
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    item {
                                        OutlinedTextField(
                                            value = emailText,
                                            onValueChange = {
                                                emailText = it
                                            },
                                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                                focusedBorderColor = Color.Gray,
                                                unfocusedBorderColor = Color.LightGray
                                            ),
                                            placeholder = { Text("Email") },
                                            singleLine = true,
                                            label = { Text(text = "Email") },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(PaddingValues(start = 40.dp, end = 40.dp))
                                                .onPreviewKeyEvent { keyEvent ->
                                                    when {
                                                        (keyEvent.key == Key.DirectionRight) -> {

                                                            true
                                                        }
                                                        (keyEvent.key == Key.DirectionLeft) -> {
                                                            //TextRange(0, emailText.length - 1)
                                                            true
                                                        }
                                                        (keyEvent.key  == Key.Delete && keyEvent.type == KeyEventType.KeyDown) -> {
                                                            emailText = emailText.substring(0, emailText.length-1)
                                                            true
                                                        }
                                                        (keyEvent.key == Key.Backspace && keyEvent.type == KeyEventType.KeyDown) -> {
                                                            emailText = emailText.substring(0, emailText.length-1)
                                                            true
                                                        }
                                                        else -> false
                                                    }
                                                },
                                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                                        )
                                    }

                                    item {

                                        bigPasswordInputWithErrorMessage(
                                            value = passwordText,
                                            onValueChangeValue = { passwordText = it },
                                            valueError = passwordError,
                                            onValueChangeError = { passwordError = it },
                                            errorMessage = CommonErrors.notValidPassword,
                                            validateError = { isValidPassword(it) },
                                            mandatory = false,
                                            keyboardType = KeyboardType.Text,
                                        )
                                    }
                                    item {
                                        Button(
                                            content = {
                                                Text(text = "Iniciar sesión")
                                            },
                                            onClick = {
                                                if(isValidPassword(passwordText) && isValidEmail(emailText)) {
                                                    loading.value = true
                                                    ViewModelLogin.getCurrentUser(
                                                        composableScope = composableScope,
                                                        idOfUser = "N5LgCdejDJfRcmePyLhhNv0Ds8n1",
                                                        onFinished = {
                                                            if(it) {
                                                                loading.value = false
                                                                onLoginClick()
                                                            }
                                                            else{
                                                                loading.value = false
                                                                textToast.value = "No se ha podido logear correctamente"
                                                                showToast.value = true
                                                            }
                                                        },
                                                    )
                                                }
                                                else {
                                                    textToast.value = CommonErrors.incompleteFields
                                                    showToast.value = true
                                                }


                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(PaddingValues(start = 40.dp, end = 40.dp))
                                        )
                                    }
                                    item {
                                        Text(
                                            text = "O bien",
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(PaddingValues(start = 40.dp, end = 40.dp))
                                        )
                                    }
                                    item {
                                        Button(
                                            content = {
                                                Text(text = "Iniciar sesión con Google")
                                            },
                                            onClick = {
                                                //Inicair sesi´çon conm google

                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(PaddingValues(start = 40.dp, end = 40.dp))
                                        )
                                    }
                                    item {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceAround,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(PaddingValues(start = 40.dp, end = 40.dp))
                                        ) {
                                            Text(
                                                text = "He olvidado la contraseña",
                                                color = Color.Blue,
                                                modifier = Modifier
                                                    .clickable {
                                                        onForgotPasswordClick()
                                                    }
                                            )
                                            Text(
                                                text = "Crear usuario",
                                                color = Color.Blue,
                                                modifier = Modifier
                                                    .clickable {
                                                        onRegisterClick()
                                                    }
                                            )
                                        }
                                    }
                                }
                            )
                        },
                    )
                }
            )
        }
    )

}
