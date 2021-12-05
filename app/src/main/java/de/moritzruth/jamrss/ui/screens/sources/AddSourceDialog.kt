package de.moritzruth.jamrss.ui.screens.sources

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import de.moritzruth.jamrss.R
import de.moritzruth.jamrss.graph
import de.moritzruth.jamrss.ui.util.checkIsOnline
import de.moritzruth.jamrss.util.FetchAndParseResult
import de.moritzruth.jamrss.util.fetchAndParseFeed
import kotlinx.coroutines.launch

private enum class AddSourceDialogState {
    Default,
    Loading,
    InvalidUrl,
    InvalidFeed,
    Offline
}

@Composable
private fun Dialog_(
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    state: AddSourceDialogState,
    url: MutableState<String>,
    shouldMarkExistingAsRead: MutableState<Boolean>
) {
    Dialog(onDismissRequest, DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            modifier = Modifier
                .padding(20.dp)
                .width(320.dp),
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colors.background
        ) {
            Box(Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                Column {
                    Text(
                        stringResource(R.string.dialog_add_source),
                        Modifier.padding(top = 15.dp, bottom = 10.dp),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        Column {
                            OutlinedTextField(
                                url.value,
                                onValueChange = { url.value = it },
                                label = { Text("URL") },
                                singleLine = true,
                                isError = state == AddSourceDialogState.InvalidUrl,
                                textStyle = LocalTextStyle.current.copy(fontSize = 12.sp)
                            )

                            AnimatedVisibility(state == AddSourceDialogState.InvalidUrl ||
                                    state == AddSourceDialogState.InvalidFeed) {
                                Text(
                                    if (state == AddSourceDialogState.InvalidUrl) stringResource(R.string.message_invalid_url)
                                    else stringResource(R.string.message_invalid_feed),
                                    modifier = Modifier.padding(top = 10.dp),
                                    color = MaterialTheme.colors.error,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        val checkboxInteractionSource = remember { MutableInteractionSource() }

                        Row(
                            Modifier.clickable(
                                checkboxInteractionSource,
                                onClick = { shouldMarkExistingAsRead.value = !shouldMarkExistingAsRead.value },
                                indication = null
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(start = 8.dp, end = 15.dp),
                                checked = shouldMarkExistingAsRead.value,
                                onCheckedChange = { shouldMarkExistingAsRead.value = it },
                                interactionSource = checkboxInteractionSource
                            )

                            Text(stringResource(R.string.button_mark_existing_as_read), fontSize = 15.sp)
                        }
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismissRequest) {
                            Text(stringResource(R.string.button_cancel))
                        }

                        Spacer(modifier = Modifier.padding(4.dp))
                        val saveEnabled = state != AddSourceDialogState.Loading && url.value.isNotBlank()

                        TextButton(
                            onClick = onSave,
                            enabled = saveEnabled
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                val progressIndicatorAlpha by animateFloatAsState(if (state === AddSourceDialogState.Loading) 1f else 0f)

                                CircularProgressIndicator(
                                    Modifier
                                        .size(18.dp)
                                        .alpha(progressIndicatorAlpha),
                                    strokeWidth = 2.dp
                                )

                                Text(stringResource(R.string.button_add))
                            }
                        }
                    }
                }
            }
        }
    }
}

private suspend fun add(url: String, shouldMarkExistingAsRead: Boolean, onCreated: () -> Unit): AddSourceDialogState {
    val trimmedUrl = url.trim()

    return when (val result = fetchAndParseFeed(trimmedUrl)) {
        FetchAndParseResult.InvalidUrl -> AddSourceDialogState.InvalidUrl
        FetchAndParseResult.BadResponse -> AddSourceDialogState.InvalidUrl
        FetchAndParseResult.InvalidFeed -> AddSourceDialogState.InvalidFeed

        is FetchAndParseResult.RequestFailed ->
            if (checkIsOnline()) AddSourceDialogState.InvalidUrl
            else AddSourceDialogState.Offline

        is FetchAndParseResult.Success -> {
            graph.feed.addSource(result.feed.title, trimmedUrl, shouldMarkExistingAsRead)
            onCreated()
            AddSourceDialogState.Loading
        }
    }
}

@Composable
fun AddSourceDialog(onDismissRequest: () -> Unit, onCreated: () -> Unit) {
    val url = remember { mutableStateOf("") }
    val shouldMarkExistingAsRead = remember { mutableStateOf(false) }
    var state by remember { mutableStateOf(AddSourceDialogState.Default) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val item = clipboard.primaryClip?.let { if (it.itemCount > 0) it.getItemAt(0) else null }

        item?.text?.let {
            if (
                (it.startsWith("https://") || it.startsWith("http://")) &&
                (it.contains("rss", true) || it.contains("atom", true))
            ) {
                url.value = it.toString()
            }
        }
    }

    Dialog_(
        onDismissRequest,
        {
            state = AddSourceDialogState.Loading
            scope.launch {
                state = add(url.value, shouldMarkExistingAsRead.value, onCreated)
            }
        },
        state,
        url,
        shouldMarkExistingAsRead
    )

    if (state == AddSourceDialogState.Offline) {
        AlertDialog(
            onDismissRequest = { state = AddSourceDialogState.Default },
            confirmButton = {
                TextButton(onClick = { state = AddSourceDialogState.Default }) {
                    Text(stringResource(R.string.button_okay))
                }
            },
            title = { Text(stringResource(R.string.message_offline)) },
            text = { Text(stringResource(R.string.message_source_add_needs_online)) }
        )
    }
}