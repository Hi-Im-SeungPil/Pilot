package org.jeonfeel.pilotproject1.ui

import android.graphics.Paint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Shapes
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FCMBoxListItem(title: String, body: String) {
    Card(
        modifier = Modifier
            .padding(start = 20.dp, top = 10.dp,end = 20.dp, bottom = 10.dp)
            .fillMaxWidth()
            .height(70.dp)
            .border(1.dp, color = Color.Black, shape = RoundedCornerShape(10))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .padding()
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 10.dp, top = 7.dp)
                )
            Text(
                text = body,
                modifier = Modifier
                    .padding()
                    .fillMaxWidth()
                    .weight(2f)
                    .padding(start = 10.dp)
//                    .wrapContentHeight(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
fun FCMBoxList() {
    LazyColumn() {
        items(20) {
            FCMBoxListItem(title = "스타박스", body = "시워언한 아이스 커피!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FCMBoxListPreview() {
    FCMBoxList()
}