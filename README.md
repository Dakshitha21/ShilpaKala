# 🪵 Shilpa-Kala — Digital Portfolio Assistant

> *Empowering Karnataka's wood artisans with GenAI-inspired visual branding*

---

## 📌 Problem Statement

Wood carvers and "Gombe" makers in Channapatna and Kinnala have world-class craft skills but struggle with **product photography**. Their WhatsApp photos look dull, making high-quality handmade work appear cheap to city buyers.

## 💡 Solution

**Shilpa-Kala** is a mobile app that transforms any artisan's phone photo into a **professional luxury catalog image** — complete with heritage labels, artisan branding, and pricing — ready to share on WhatsApp or Instagram.

---

## ✨ Features

| Feature | Description |
|---|---|
| 📷 **Guided Camera** | Custom overlay with golden frame guide — artisan places product in frame |
| 🎨 **GenAI Branding Engine** | Bitmap Canvas overlay adds luxury panel, heritage label, artisan info |
| 🏷️ **Heritage Label** | "HANDMADE IN KARNATAKA" badge on every photo |
| 💰 **Price Tag** | Product name, wood type, and price embedded elegantly |
| 💾 **Gallery Save** | Saves to "ShilpaKala" album in device gallery |
| 📱 **WhatsApp Share** | One-tap share to WhatsApp or any app |

---

## 🏗️ Architecture

```
app/
├── ui/
│   ├── CameraActivity.kt       ← Camera preview + capture
│   ├── BrandingActivity.kt     ← Artisan detail input form
│   └── ResultActivity.kt       ← Preview, save, share
├── camera/
│   └── CameraOverlayView.kt    ← Custom Canvas overlay guide
├── utils/
│   ├── ImageUtils.kt           ← Bitmap branding engine (THE CORE)
│   └── ShareUtils.kt           ← Gallery save + share intents
└── MainActivity.kt             ← Splash screen
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | **Kotlin** |
| UI | **XML Layouts + ViewBinding** |
| Camera | **CameraX** (PreviewView + ImageCapture) |
| Image Processing | **Bitmap + Canvas** (GenAI-inspired branding) |
| Storage | **MediaStore API** |
| Share | **Android Intent + FileProvider** |

---

## 🚀 How to Build & Run

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- Android SDK 34
- A physical Android device or emulator with API 24+

### Steps
1. **Clone/Open** the project in Android Studio
2. **Sync Gradle** (File → Sync Project with Gradle Files)
3. **Run** on device (camera won't work on emulator for full test)
4. **Grant** Camera permission when prompted

### Demo Flow
1. Open app → Splash screen appears
2. Camera opens with golden guide overlay
3. Place wooden product inside the frame
4. Tap white capture button
5. Enter: Artisan Name, Product Name, Wood Type, Price
6. Tap **"Generate Professional Photo"**
7. See the branded luxury catalog image
8. Save to Gallery or Share on WhatsApp!

---

## 🖼️ Branding Overlay Details (ImageUtils.kt)

The **GenAI-inspired branding engine** uses Android's `Canvas` + `Bitmap` API to:

1. **Load** the captured photo
2. **Apply** a gradient dark panel (bottom 32% of image)
3. **Render** `"✦ HANDMADE IN KARNATAKA ✦"` in serif italic gold
4. **Draw** product name in large cream serif font
5. **Add** artisan name in italic gold below
6. **Place** wood type (left) and price (right) with separator
7. **Stamp** a "GENUINE CRAFT" circular quality badge (top-right)
8. **Watermark** "SHILPA-KALA • DIGITAL ARTISAN PORTFOLIO" at bottom
9. **Save** as high-quality JPEG

Color palette: `#1A0E05` (dark brown) · `#C9A84C` (heritage gold) · `#FFF8E7` (cream)

---

## 🎯 Impact Goals

- **Brand India**: Enhances perceived value of Karnataka handicrafts globally
- **Digital Literacy**: Teaches artisans the power of visual branding
- **Economic Empowerment**: Enables artisans to justify premium pricing

---

## 🔮 Future Scope (GenAI Extensions)

- **AI Background Removal**: Remove background, add studio backdrop using ML Kit / SegmentAnything
- **Auto Product Description**: Generate product descriptions from image using Gemini API
- **AI Price Suggestion**: Market-based price recommendations
- **Multi-language**: Kannada UI for local artisans
- **Online Marketplace**: Direct listing to Etsy / Amazon Handmade

---

## 📊 What This Demonstrates

> *"The app uses GenAI-inspired branding automation concepts to enhance product presentation for artisans. Future scope includes AI-generated backgrounds and automatic product descriptions using LLMs."*

- ✅ Android + GenAI concept alignment
- ✅ Real camera integration (CameraX)
- ✅ Image processing with Canvas/Bitmap
- ✅ Clean MVVM-inspired architecture
- ✅ Social impact focus
- ✅ Complete user flow (capture → brand → share)

---

## 👨‍💻 Built for Internship Evaluation

**Project**: Shilpa-Kala — Android App Development using GenAI  
**Category**: Self-Employment / Social Impact  
**Stack**: Android Native (Kotlin) + CameraX + Bitmap Canvas  

---

*Made with ❤️ for Karnataka's artisan community*
