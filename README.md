# PremiumApp - Aplicación Android Nativa

Aplicación Android nativa desarrollada con **Kotlin**, **Jetpack Compose** y **Material Design 3**, con una interfaz ultra premium, moderna, elegante y extremadamente fluida.

## Arquitectura

El proyecto sigue la arquitectura **MVVM** (Model-View-ViewModel) organizada en los siguientes paquetes:

```
com.premium.app/
├── ui/
│   ├── screens/          → Pantallas de la aplicación
│   └── theme/            → Tema Material Design 3 (colores, tipografía, formas)
├── viewmodels/           → ViewModels con lógica de negocio
├── repository/           → Repositorios para acceso a datos
├── models/               → Data classes (User, Post, Comment, Message, Business, Ad)
├── navigation/           → Navegación y barra inferior
├── firebase/             → Módulos Hilt para Firebase
└── utils/                → Utilidades (validación, moderación, compresión, spam)
```

## Funcionalidades

### Autenticación
- Inicio de sesión con correo/contraseña
- Inicio de sesión con Google
- Registro con validación en tiempo real
- Recuperación de contraseña
- Verificación de correo electrónico

### Feed (Inicio)
- Feed de videos con carga paginada
- Me gusta, comentarios, compartir, guardar
- Seguir creadores
- Eliminación solo por propietario

### Inteligencia Artificial
- Chat con IA
- Historial de conversaciones
- Respuestas a preguntas, programación, enseñanza

### Subir Contenido
- Subida de videos y fotografías
- Editor con IA (recorte, filtros, texto, música)
- Compresión inteligente
- Moderación automática de contenido

### Perfil
- Foto, nombre, usuario, biografía
- Seguidores, seguidos, publicaciones
- Biblioteca de guardados
- Restricciones de cambio (nombre: 5 días, usuario: 25 días)

### Negocios
- Solo para cuentas comerciales
- Anuncios, promociones, productos
- Información de contacto y horarios
- Estadísticas básicas

## Configuración

### 1. Firebase

1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Crea un nuevo proyecto o usa uno existente
3. Agrega una app Android con el package name: `com.premium.app`
4. Descarga el archivo `google-services.json`
5. Colócalo en la carpeta `app/`
6. Habilita los siguientes servicios en Firebase Console:
   - **Authentication** (Email/Password y Google)
   - **Cloud Firestore**
   - **Storage**
   - **Cloud Messaging**

### 2. Google Sign-In

1. En Firebase Console → Authentication → Sign-in method
2. Habilita Google como proveedor
3. Configura el SHA-1 de tu app en la consola de Firebase

### 3. API de IA (para el chat)

Configura tu API key de IA en el archivo `AIRepository.kt` o mediante variables de entorno.

## Requisitos

- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17
- SDK mínimo: API 24 (Android 7.0)
- SDK objetivo: API 34 (Android 14)

## Dependencias principales

- Jetpack Compose + Material Design 3
- Navigation Compose
- Firebase (Auth, Firestore, Storage, Messaging)
- Hilt (Inyección de dependencias)
- Coil (Carga de imágenes)
- ExoPlayer/Media3 (Reproducción de video)
- DataStore (Preferencias locales)

## Cómo ejecutar

1. Abre el proyecto en Android Studio
2. Agrega tu `google-services.json` en `app/`
3. Sincroniza Gradle
4. Ejecuta en un emulador o dispositivo físico

## Modo claro y oscuro

La aplicación soporta automáticamente modo claro y oscuro siguiendo las preferencias del sistema.

## Seguridad

- Solo el propietario puede eliminar sus publicaciones
- Protección contra spam (rate limiting)
- Moderación automática de contenido
- Validación de datos en cliente y servidor
- Verificación obligatoria de correo electrónico
- Sistema de reportes de publicaciones
