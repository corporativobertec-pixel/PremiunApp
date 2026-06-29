package com.premium.app.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.premium.app.R
import com.premium.app.utils.Constants

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
)

@Composable
fun BottomNavBar(
    navController: NavController,
    bottomBarState: Boolean
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem(name = stringResource(R.string.home_tab), route = "home", icon = Icons.Default.Home),
        BottomNavItem(name = stringResource(R.string.ai_tab), route = "ai", icon = Icons.Default.Psychology),
        BottomNavItem(name = stringResource(R.string.profile_tab), route = "profile", icon = Icons.Default.Person),
        BottomNavItem(name = stringResource(R.string.business_tab), route = "business", icon = Icons.Default.BusinessCenter)
    )

    AnimatedVisibility(
        visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(Constants.ANIMATION_DURATION_MS)),
        exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(Constants.ANIMATION_DURATION_MS))
    ) {
        BottomAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.take(2).forEach { item ->
                    BottomNavItemView(item = item, currentRoute = currentRoute, navController = navController)
                }

                // Central Floating Action Button
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    FloatingActionButton(
                        onClick = { /* TODO: Handle upload action */ },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        shape = CircleShape,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.upload_tab))
                    }
                }

                items.takeLast(2).forEach { item ->
                    BottomNavItemView(item = item, currentRoute = currentRoute, navController = navController)
                }
            }
        }
    }
}

@Composable
fun BottomNavItemView(
    item: BottomNavItem,
    currentRoute: String?,
    navController: NavController
) {
    val selected = currentRoute == item.route
    val contentColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(64.dp)
            .weight(1f)
    ) {
        IconButton(onClick = { navController.navigate(item.route) {
            popUpTo(navController.graph.startDestinationId)
            launchSingleTop = true
        } }) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.name,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = item.name,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor
        )
    }
}
