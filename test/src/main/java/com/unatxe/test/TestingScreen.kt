package com.unatxe.test

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TestingScreen() {
    val arraytest = remember { mutableStateListOf<Pair<Int,String>>() }
    LazyList(arraytest){
        arraytest.add(Pair(arraytest.size,"New Element"))
    }
}

@Composable
fun LazyList(arraytest: SnapshotStateList<Pair<Int,String>>, onAdd: ()-> Unit) {

    Column() {
        Log.d("Compose", "Column")
        val listState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier.height(500.dp),
            state = listState
        ) {
            Log.d("COmpose", "LazyColumn")
            items(
                items = arraytest,
                key = { it.first },
                itemContent = {
                    val text = remember { mutableStateOf(it) }
                    Text(text.value.second)
                }
            )


        }

        Button(modifier = Modifier.height(50.dp),
            onClick = { onAdd() }) {

            Text("Add")
        }
    }
}

@Composable
fun ItemColumn(text: MutableState<String>){
    Text(text.value)
}
