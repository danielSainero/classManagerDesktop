package Screens.Class.Components.MainBody.Members

import Screens.Class.Components.MainBody.ContentState
import Screens.Class.ViewModelClass
import Screens.MainAppScreen.Items.bigSelectedDropDownMenu
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun addMember(
    onValueChangeExpanded: (Boolean) -> Unit
) {


    val composableScope = rememberCoroutineScope()
    var idOfUser by remember { mutableStateOf("") }
    val suggestion: MutableList<String> = mutableListOf("admin","profesor","padre","alumno")
    var textSelectedRol by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
            .width(320.dp),
        content = {
            Spacer(modifier = Modifier.padding(10.dp))
            TextField(
                value = idOfUser,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onPreviewKeyEvent { keyEvent ->
                        when {
                            (keyEvent.key == Key.DirectionRight) -> {
                                //  CursorSelectionBehaviour
                                true
                            }
                            (keyEvent.key == Key.DirectionLeft) -> {
                                TextRange(1, 0)
                                true
                            }
                            (keyEvent.key == Key.Delete && keyEvent.type == KeyEventType.KeyDown) -> {
                                if (idOfUser.isNotEmpty()) idOfUser = idOfUser.substring(0, idOfUser.length - 1)
                                true
                            }
                            (keyEvent.key == Key.Backspace && keyEvent.type == KeyEventType.KeyDown) -> {
                                if (idOfUser.isNotEmpty()) idOfUser = idOfUser.substring(0, idOfUser.length - 1)
                                true
                            }
                            else -> false
                        }
                    }
                    .padding(
                        PaddingValues(
                            start = 20.dp,
                            end = 20.dp
                        )
                    ),
                label = {
                    Text(text = "Id del usuario")
                },
                placeholder = {
                    Text(text = "Añadir la id de un usuario")
                },
                onValueChange = {
                    idOfUser = it
                },
                singleLine = true,
            )
            Spacer(modifier = Modifier.padding(6.dp))

            bigSelectedDropDownMenu (
                suggestions = suggestion,
                onValueChangeTextSelectedItem = { textSelectedRol = it }
            )

            Spacer(modifier = Modifier.padding(7.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        PaddingValues(
                            end = 20.dp
                        )
                    ),
                content = {
                    TextButton(
                        onClick = {
                            onValueChangeExpanded(false)
                        },
                        content = {
                            Text(text = "Cancelar")
                        }
                    )
                    TextButton(
                        onClick = {
                            ViewModelClass.addNewMember(
                                rol = textSelectedRol,
                                composableScope = composableScope,
                                idOfUser = idOfUser,
                                onFinished = {
                                    ViewModelClass.updateContentState(newValue = ContentState.MEMBERS)
                                }
                            )
                        },
                        content = {
                            Text(text = "Save")
                        }
                    )

                }
            )

        }
    )
}