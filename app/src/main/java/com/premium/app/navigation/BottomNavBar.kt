package com.premium.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Inicio", Icons.Default.Home)
    object AI : BottomNavItem("ai", "IA", Icons.Default.Star)
    object Upload : BottomNavItem("upload", "Subir", Icons.Default.Add)
    object Profile : BottomNavItem("profile", "Perfil", Icons.Default.Person)
    object Business : BottomNavItem("business", "Negocios", Icons.Default.Star)
}

@Composable
fun BottomNavBar(navController: NavController, onFabClick: () -> Unit) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.AI,
        BottomNavItem.Upload,
        BottomNavItem.Profile,
        BottomNavItem.Business
    )

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp) // Ajusta la altura si es necesario
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        // containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp) // Opcional: para elevar el color
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selected,
                onClick = {
                    if (item == BottomNavItem.Upload) {
                        onFabClick()
                    } else {
                        navController.navigate(item.route) {
                            // Evita múltiples copias de la misma pantalla en la pila
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            // Evita múltiples copias del mismo elemento cuando se selecciona
                            launchSingleTop = true
                            // Restaura el estado cuando se vuelve a seleccionar el elemento
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavBar() {
    val navController = rememberNavController()
    Column(modifier = Modifier.padding(top = 500.dp)) { // Para que la barra se vea en la parte inferior en el preview
        BottomNavBar(navController = navController, onFabClick = { /* Do nothing for preview */ })
    }
}
