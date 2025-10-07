package com.example.sample_jetpack_compose

import PersonDetailScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.navigation.compose.NavHost
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersonsTelekomApp()
        }
    }
}

@Composable
fun PersonsTelekomApp() {
    val navController = rememberNavController()

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {

                MagentaBanner(
                    text = "Persons@Telekom",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") {
                            MainScreen(navController)
                        }
                        composable("details/{name}/{email}/{phone}/{imageRes}") { backStackEntry ->
                            val name = backStackEntry.arguments?.getString("name") ?: ""
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            val phone = backStackEntry.arguments?.getString("phone") ?: ""
                            val imageRes = backStackEntry.arguments?.getString("imageRes")?.toInt() ?: 0

                            PersonDetailScreen(
                                person = Person(name, email, phone, imageRes),
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MagentaBanner(text: String, modifier: Modifier = Modifier) {
    val telekomMagenta = Color(0xFFE20074)

    Box(
        modifier = modifier
            .background(color = telekomMagenta),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// --- Main Screen mit Liste und Suche ---
@Composable
fun MainScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val persons = listOf(
        Person("Anna Müller", "email@telekom.de", "012345678", R.drawable.ic_person),
        Person("Max Mustermann", "email@telekom.de", "012345678", R.drawable.ic_person),
        Person("Beate Beispiel", "email@telekom.de", "012345678", R.drawable.ic_person),
        Person("Max Liebig", "email@telekom.de", "012345678", R.drawable.ic_person),
        Person("Annika Zehne", "email@telekom.de", "012345678", R.drawable.ic_person),
        Person("Tom Schneider", "email@telekom.de", "012345678", R.drawable.ic_person),
        Person("Lisa Becker", "email@telekom.de", "012345678", R.drawable.ic_person),
        Person("Antonia Eberhard", "email@telekom.de", "012345678", R.drawable.ic_person),
        Person("Julia Franz", "email@telekom.de", "012345678", R.drawable.ic_person),
        Person("Julian Peter", "email@telekom.de", "012345678", R.drawable.ic_person),
    )

    val filteredPersons = persons.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
            .padding(8.dp)
    ) {
        // Suchfeld
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .semantics { contentDescription = "searchInput" }
                .fillMaxWidth(),
            placeholder = { Text("🔍 Suche nach Name...") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Scrollbare Liste
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 60.dp)
                .semantics { contentDescription = "recyclerView" },
        ) {
            items(filteredPersons) { person ->
                PersonCard(person) {
                    navController.navigate("details/${person.name}/${person.email}/${person.mobile}/${person.imageResId}")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun PersonCard(person: Person, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(id = person.imageResId),
                contentDescription = person.name,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = person.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}