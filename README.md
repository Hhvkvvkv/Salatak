# صلاتك - تطبيق مواقيت الصلاة

**الإصدار:** 4.1.1 (build 42)  
**الحزمة:** `com.salatak.app`  
**الحد الأدنى للإصدار:** Android 7.0 (API 24)  
**الهدف:** Android 14 (API 34)

## هيكل المشروع

```
صلاتك/
├── app/
│   └── src/main/
│       ├── java/com/salatak/app/   ← الكود المصدري (Java مفكوك)
│       ├── res/                      ← الموارد (layouts, drawables, etc.)
│       ├── assets/                   ← الملفات الصوتية والبيانات
│       └── AndroidManifest.xml       ← ملف التطبيق
├── build.gradle.kts                  ← ملف بناء Gradle
├── settings.gradle.kts
└── gradle/wrapper/gradle-wrapper.properties
```

## الاعتماديات (Dependencies)

| المكتبة | الاستخدام |
|---|---|
| `androidx.appcompat:appcompat:1.6.1` | دعم الإصدارات القديمة |
| `androidx.activity:activity:1.8.2` | إدارة الأنشطة |
| `androidx.fragment:fragment:1.6.2` | إدارة الشاشات (Fragments) |
| `androidx.media3:media3-exoplayer:1.2.1` | تشغيل القرآن والراديو |
| `com.google.android.material:material:1.11.0` | واجهات Material Design |
| `com.android.volley:volley:1.2.1` | طلبات الشبكة |
| `com.batoulapps.adhan:adhan:2.0.1` | حساب مواقيت الصلاة |

## كيفية البناء

### الخيار 1: Gradle (يتطلب Android SDK)
```bash
# تثبيت Android SDK CLI أولاً (أنظر الأسفل)
cd /root/Documents/Codex/صلاتك
./gradlew assembleDebug
# APK في: app/build/outputs/apk/debug/
```

### الخيار 2: apktool (تعديل سريع - smali)
```bash
# 1. فك التجميع (تم)
apktool d /path/to/صلاتك.apk -o /tmp/apk-output

# 2. عدّل على smali/XML في /tmp/apk-output

# 3. إعادة البناء
apktool b /tmp/apk-output -o /tmp/صلاتك_معدل.apk

# 4. التوقيع (مطلوب)
jarsigner -keystore my.keystore -storepass password \
  /tmp/صلاتك_معدل.apk alias_name
```

## 🛠️ تثبيت Android SDK (للبيئة الحالية)
لتتمكن من البناء عبر Gradle:
```bash
# تحميل cmdline-tools
wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip
unzip commandlinetools-linux-*.zip -d /opt/android-sdk/cmdline-tools
mv /opt/android-sdk/cmdline-tools/cmdline-tools /opt/android-sdk/cmdline-tools/latest

# تثبيت SDK
export ANDROID_HOME=/opt/android-sdk
/opt/android-sdk/cmdline-tools/latest/bin/sdkmanager "platforms;android-34" "build-tools;34.0.0"
```

## ⚠️ ملاحظة مهمة عن الكود
- الكود Java المستخرج من jadx مخصص **للقراءة والفهم**
- 88 من 103 ملف تحتوي على `synthetic` classes (لـ lambdas) لا تترجم مباشرة
- للمشاريع الإنتاجية: يفضل العمل على smali مباشرة ثم إعادة البناء بـ apktool
- بعض الملفات لها أسماء مشفرة (a.java, b.java, إلخ)

## الملفات الرئيسية

| الملف | الوظيفة |
|---|---|
| `MainActivity.java` | الشاشة الرئيسية والتنقل بين الأقسام |
| `PrayerTimesFragment.java` | عرض مواقيت الصلاة |
| `QiblaFragment.java` | اتجاه القبلة |
| `AzkarActivity.java` | الأذكار |
| `TasbihActivity.java` | المسبحة الإلكترونية |
| `SettingsActivity.java` | الإعدادات |
| `RadioActivity.java` | الراديو الإسلامي |
| `QuranPlayerActivity.java` | مشغل القرآن |
| `PrayerTimesCalculator.java` | حساب مواقيت الصلاة (المجلد helpers) |

---

## 📱 كيفية التثبيت (مهم جداً)

### ⚠️ المشكلة الشائعة: "التطبيق ليس مثبتاً"
هذا يحدث لأن التطبيق الأصلي موقّع بمفتاح مختلف عن نسختنا المبنية.

### ✅ الحل:
1. **احذف التطبيق الأصلي** من جوالك أولاً:
   - الإعدادات ← التطبيقات ← صلاتك ← إلغاء التثبيت
2. **ثبّت النسخة الجديدة** من رابط التحميل
3. لن تحتاج لحذفه مرة أخرى في التحديثات القادمة (لأن كل النسخ المبنية بنفس المفتاح)

### 🔐 عن التوقيع
- النسخة المبنية تستخدم **debug key** (مفتاح تطوير)
- هذا طبيعي ومقبول للتطبيقات الشخصية/المعدّلة
- جميع التحديثات القادمة ستستخدم نفس المفتاح

### 📥 رابط التحميل المباشر
```
https://github.com/Hhvkvvkv/Salatak/releases/latest
```
