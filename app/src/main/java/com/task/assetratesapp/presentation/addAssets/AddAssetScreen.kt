package com.task.assetratesapp.presentation.addAssets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.task.assetratesapp.presentation.assetList.AssetViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssetScreen(
    viewModel: AssetViewModel,
    onAssetSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val currencies by viewModel.supportedCurrencies.collectAsState()

    val filtered = currencies
        .filterKeys { it.contains(searchQuery, ignoreCase = true) }
        .toSortedMap()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select an Asset") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(8.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Assets") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            LazyColumn(Modifier.fillMaxSize()) {
                items(filtered.entries.toList()) { (code, name) ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAssetSelected(code) }
                            .padding(16.dp)
                    ) {
                        Text(code, style = MaterialTheme.typography.bodyLarge)
                        Text(name, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}