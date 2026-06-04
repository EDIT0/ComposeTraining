package com.my.book.library.core.common.component

import androidx.annotation.ColorRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.my.book.library.core.common.dpToSp
import com.my.book.library.core.common.noRippleClickable
import com.my.book.library.core.resource.NotoSansKR
import com.my.book.library.core.resource.R
import androidx.compose.material3.Text

@Composable
fun CommonSearchBar(
    modifier: Modifier = Modifier,
    isFakeSearchBar: Boolean = false,
    @ColorRes backgroundColorRes: Int = R.color.color_F2F4F6,
    cornerRadius: Dp = 8.dp,
    hint: String = "",
    @ColorRes hintColorRes: Int = R.color.color_6B7684,
    @ColorRes textColorRes: Int = R.color.color_191F28,
    textSize: Dp = 14.dp,
    value: TextFieldValue,
    focusRequester: FocusRequester,
    showKeyboardOnStart: Boolean = false,
    onValueChange: (TextFieldValue) -> Unit,
    onSearchClick: () -> Unit,
    onCancelClick: () -> Unit,
    onFakeBarClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(showKeyboardOnStart) {
        if (showKeyboardOnStart && !isFakeSearchBar) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    val backgroundColor = colorResource(backgroundColorRes)
    val hintColor = colorResource(hintColorRes)
    val textColor = colorResource(textColorRes)

    Row(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .then(
                if (isFakeSearchBar) Modifier.noRippleClickable { onFakeBarClick() }
                else Modifier
            )
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_search_grey_18x18),
            contentDescription = null,
            modifier = Modifier.noRippleClickable { onSearchClick() }
        )

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.text.isEmpty()) {
                Text(
                    text = hint,
                    style = TextStyle(
                        color = hintColor,
                        fontSize = dpToSp(textSize),
                        fontFamily = NotoSansKR,
                        fontWeight = FontWeight.Normal
                    )
                )
            }

            if (!isFakeSearchBar) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        color = textColor,
                        fontSize = dpToSp(textSize),
                        fontFamily = NotoSansKR,
                        fontWeight = FontWeight.Normal
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onSearchClick()
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    )
                )
            }
        }

        if (value.text.isNotEmpty()) {
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(R.drawable.ic_cancel_grey_20x20),
                contentDescription = null,
                modifier = Modifier.noRippleClickable {
                    onCancelClick()
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonSearchBarPreview() {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }
    CommonSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        hint = "검색어를 입력하세요",
        value = textFieldValue,
        focusRequester = focusRequester,
        onValueChange = { textFieldValue = it },
        onSearchClick = {},
        onCancelClick = { textFieldValue = TextFieldValue("") },
        onFakeBarClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CommonSearchBarFakePreview() {
    val focusRequester = remember { FocusRequester() }
    CommonSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        hint = "검색어를 입력하세요",
        isFakeSearchBar = true,
        value = TextFieldValue(""),
        focusRequester = focusRequester,
        onValueChange = {},
        onSearchClick = {},
        onCancelClick = {},
        onFakeBarClick = {}
    )
}
