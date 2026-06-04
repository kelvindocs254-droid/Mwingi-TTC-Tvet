package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.*
import com.example.ui.theme.MwingiTtcTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MwingiTtcTheme {
                MainScreen()
            }
        }
    }
}

@Serializable object HomeRoute
@Serializable object AboutRoute
@Serializable object CoursesRoute
@Serializable object AdmissionRoute
@Serializable object ContactRoute

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                val items = listOf(
                    NavigationItem("Home", HomeRoute, Icons.Default.Home),
                    NavigationItem("Courses", CoursesRoute, Icons.Default.School),
                    NavigationItem("Admission", AdmissionRoute, Icons.Default.AppRegistration),
                    NavigationItem("About", AboutRoute, Icons.Default.Info),
                    NavigationItem("Contact", ContactRoute, Icons.Default.ContactSupport)
                )

                items.forEach { item ->
                    val selected = currentDestination?.hasRoute(item.route::class) == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<HomeRoute> { HomeScreen(navController) }
            composable<AboutRoute> { AboutScreen() }
            composable<CoursesRoute> { CoursesScreen() }
            composable<AdmissionRoute> { AdmissionScreen() }
            composable<ContactRoute> { ContactScreen() }
        }
    }
}

data class NavigationItem(val title: String, val route: Any, val icon: ImageVector)

@Composable
fun HomeScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeroSection()
        }
        item {
            Text(
                text = "Featured Courses",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            CoursesRow(navController)
        }
        item {
            Text(
                text = "Latest Announcements",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        items(getMockAnnouncements(), key = { it }) { announcement ->
            AnnouncementCard(announcement)
        }
    }
}

@Composable
fun HeroSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.mwingi_hero),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.6f
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Mwingi Teachers Training College",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Education for Service",
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun CoursesRow(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CourseSnippetCard("DPTE", "Primary Teacher Education", navController)
        CourseSnippetCard("DECTE", "Early Childhood Dev.", navController)
        CourseSnippetCard("Upgrading", "Upgrade Programs", navController)
    }
}

@Composable
fun CourseSnippetCard(code: String, name: String, navController: NavController) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { navController.navigate(CoursesRoute) },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(Icons.Default.Book, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = code, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = name, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun AnnouncementCard(announcement: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = announcement, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "About Us", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        
        SectionCard("Our Mission") {
            Text("To provide high quality teacher education and training that produces competent, innovative and ethical teachers through research and development for national and global sustainability.")
        }
        
        SectionCard("Our Vision") {
            Text("A globally recognized center of excellence in teacher education and holistic development.")
        }
        
        SectionCard("Our History") {
            Text("Mwingi Teachers Training College is a public institution located in Kitui County, Mwingi Town. It was established to address the growing need for quality teacher training in the region and beyond, focusing on Primary and Early Childhood Education.")
        }
        
        SectionCard("Core Values") {
            Column {
                val values = listOf("Integrity", "Professionalism", "Innovation", "Inclusivity", "Customer Satisfaction")
                values.forEach { value ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(value)
                    }
                }
            }
        }
    }
}

@Composable
fun CoursesScreen() {
    val courses = listOf(
        Course("Diploma in Primary Teacher Education (DPTE)", "C (Plain) or equivalent", "3 Years"),
        Course("Diploma in Early Childhood Teacher Education (DECTE)", "C (Plain) or equivalent", "3 Years"),
        Course("Upgrade Diploma in Primary Teacher Education", "P1 Certificate", "1 Year"),
        Course("Upgrade Diploma in Early Childhood Development Education", "ECDE Certificate", "1 Year")
    )
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(text = "Programs Offered", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
        items(courses, key = { it.name }) { course ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = course.name, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Requirements: ${course.req}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Duration: ${course.duration}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun AdmissionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Admission & Application", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Admission is open for the next intake!", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Join Mwingi TTC for a transformative training experience.")
            }
        }
        
        SectionHeader("How to Apply")
        Text("1. Visit the college or our website to obtain the application form.")
        Text("2. Fill in the required details accurately.")
        Text("3. Attach certified copies of KCSE certificate, ID card, and leaving certificate.")
        Text("4. Pay the required application fee and submit the documents.")
        
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Handle form download */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Download, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Download Application Form")
        }
    }
}

@Composable
fun ContactScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Contact Details", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        
        ContactInfoItem(Icons.Default.LocationOn, "Location", "Mwingi Town, Kitui County, Kenya")
        ContactInfoItem(Icons.Default.Email, "Email", "info@mwingittc.ac.ke")
        ContactInfoItem(Icons.Default.Phone, "Phone", "+254 712 345 678")
        ContactInfoItem(Icons.Default.PostAdd, "Postal Address", "P.O. Box 47-90400, Mwingi")
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { /* Handle call */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Call, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Call Campus")
        }
    }
}

@Composable
fun ContactInfoItem(icon: ImageVector, title: String, detail: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
            Text(text = detail, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun SectionCard(title: String, content: @Composable () -> Unit) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

data class Course(val name: String, val req: String, val duration: String)

fun getMockAnnouncements() = listOf(
    "2024 DPTE/DECTE Admission letters are ready for collection.",
    "College closes for half-term on 15th June 2024.",
    "Graduation ceremony proposed date: October 20th 2024.",
    "New upgrade programs for P1 teachers now available."
)
