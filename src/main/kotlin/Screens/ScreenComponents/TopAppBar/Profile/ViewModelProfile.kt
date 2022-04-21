package Screens.ScreenComponents.TopAppBar.Profile

import Screens.Register.ViewModelRegister
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.api.ApiServiceAuthentication
import data.api.ApiServiceUser
import data.local.NewUser
import data.remote.appUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class ViewModelProfile {
    companion object{
        private var errorMessage: String by mutableStateOf ("")
        var postUser = appUser("","","", arrayListOf(), arrayListOf(),"","")

        fun saveChange(
            newUser: appUser,
            composableScope: CoroutineScope,
            onFinish: () -> Unit,
        ) {
            composableScope.launch {
                val apiService = ApiServiceUser.getInstance()

                try {
                    val result = apiService.putUser(newUser)
                    if (result.isSuccessful) {
                        postUser = result.body()!!
                        onFinish()
                    }
                } catch (e: Exception) {
                    errorMessage = e.message.toString()
                }
            }
        }

        //Validaciones
        fun isValidName(text: String) = Pattern.compile("^[a-zA-Z ]+$", Pattern.CASE_INSENSITIVE).matcher(text).find()
        fun isValidDescription(text: String) = Pattern.compile("^[a-zA-Z0-9 ]+$", Pattern.CASE_INSENSITIVE).matcher(text).find()

    }
}