package com.salatak.app;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/* loaded from: classes2.dex */
public class AdhanSoundsData {
    public static final String REPO_BASE_URL = "https://raw.githubusercontent.com/mabdulhakim248-crypto/islamweb-adhan-audio/main/adhan/";
    private static List<AdhanSound> allSounds;

    public static class AdhanSound {
        public String country;
        public String id;
        public String name;
        public String type;

        public AdhanSound(String str, String str2, String str3, String str4) {
            this.id = str;
            this.name = str2;
            this.type = str3;
            this.country = str4;
        }

        public String getUrl() {
            return android.support.v4.media.l.i(new StringBuilder(AdhanSoundsData.REPO_BASE_URL), this.id, ".mp3");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0056, code lost:
    
        if (r7.isEmpty() != false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x005e, code lost:
    
        if (r7.equals(r2.country) != false) goto L51;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.List<com.salatak.app.AdhanSoundsData.AdhanSound> filter(java.lang.String r5, java.lang.String r6, java.lang.String r7) {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.util.List r1 = getAdhanSounds()
            java.util.Iterator r1 = r1.iterator()
        Ld:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L76
            java.lang.Object r2 = r1.next()
            com.salatak.app.AdhanSoundsData$AdhanSound r2 = (com.salatak.app.AdhanSoundsData.AdhanSound) r2
            java.lang.String r3 = r2.id
            java.lang.String r4 = "none"
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L3f
            if (r5 == 0) goto L2b
            boolean r3 = r5.isEmpty()
            if (r3 == 0) goto Ld
        L2b:
            if (r6 == 0) goto L33
            boolean r3 = r6.isEmpty()
            if (r3 == 0) goto Ld
        L33:
            if (r7 == 0) goto L3b
            boolean r3 = r7.isEmpty()
            if (r3 == 0) goto Ld
        L3b:
            r0.add(r2)
            goto Ld
        L3f:
            if (r6 == 0) goto L50
            boolean r3 = r6.isEmpty()
            if (r3 != 0) goto L50
            java.lang.String r3 = r2.type
            boolean r3 = r6.equals(r3)
            if (r3 != 0) goto L50
            goto Ld
        L50:
            if (r7 == 0) goto L61
            boolean r3 = r7.isEmpty()
            if (r3 != 0) goto L61
            java.lang.String r3 = r2.country
            boolean r3 = r7.equals(r3)
            if (r3 != 0) goto L61
            goto Ld
        L61:
            if (r5 == 0) goto L72
            boolean r3 = r5.isEmpty()
            if (r3 != 0) goto L72
            java.lang.String r3 = r2.name
            boolean r3 = r3.contains(r5)
            if (r3 != 0) goto L72
            goto Ld
        L72:
            r0.add(r2)
            goto Ld
        L76:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.salatak.app.AdhanSoundsData.filter(java.lang.String, java.lang.String, java.lang.String):java.util.List");
    }

    public static List<AdhanSound> getAdhanSounds() {
        if (allSounds == null) {
            ArrayList arrayList = new ArrayList();
            allSounds = arrayList;
            arrayList.add(new AdhanSound("none", "🔕 بدون مؤذن (تنبيه فقط)", "general", ""));
            android.support.v4.media.l.s(allSounds, "1001", "أذان - الكويت", "general", "الكويت");
            android.support.v4.media.l.s(allSounds, "1002", "أذان الفجر - الكويت 1", "fajr", "الكويت");
            android.support.v4.media.l.s(allSounds, "1003", "أذان الفجر - الكويت 2", "fajr", "الكويت");
            android.support.v4.media.l.s(allSounds, "1004", "أذان - بروناي - دار السلام 1", "general", "بروناي");
            android.support.v4.media.l.s(allSounds, "1005", "أذان - بروناي - دار السلام 2", "general", "بروناي");
            android.support.v4.media.l.s(allSounds, "1006", "أذان - تركيا 1", "general", "تركيا");
            android.support.v4.media.l.s(allSounds, "1007", "أذان - تركيا 2", "general", "تركيا");
            android.support.v4.media.l.s(allSounds, "1008", "أذان الفجر - مكة المكرمة", "fajr", "السعودية");
            android.support.v4.media.l.s(allSounds, "1009", "أذان - مكة المكرمة", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "1010", "أذان الملا - مكة المكرمة", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "1011", "أذان - دبي - الإمارات", "general", "الإمارات");
            android.support.v4.media.l.s(allSounds, "1012", "أذان - عجمان - الإمارات", "general", "الإمارات");
            android.support.v4.media.l.s(allSounds, "1013", "أذان - المدينة المنورة", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "1014", "أذان الفجر - المدينة المنورة", "fajr", "السعودية");
            android.support.v4.media.l.s(allSounds, "1015", "أذان الفجر - القدس", "fajr", "فلسطين");
            android.support.v4.media.l.s(allSounds, "1016", "أذان - القدس", "general", "فلسطين");
            android.support.v4.media.l.s(allSounds, "1017", "أذان - حافظ حسين إرك", "general", "");
            android.support.v4.media.l.s(allSounds, "1019", "أذان الفجر - الجزائر", "fajr", "الجزائر");
            android.support.v4.media.l.s(allSounds, "1020", "أذان الفجر - بروني - دار السلام", "fajr", "بروناي");
            android.support.v4.media.l.s(allSounds, "1021", "أذان - سوريا", "general", "سوريا");
            android.support.v4.media.l.s(allSounds, "1022", "أذان الفجر - ماليزيا", "fajr", "ماليزيا");
            android.support.v4.media.l.s(allSounds, "1023", "أذان الفجر - القاهرة", "fajr", "مصر");
            android.support.v4.media.l.s(allSounds, "1024", "أذان القاهرة - أبو العنين شعيشع", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "1025", "أذان القاهرة - أحمد نعينع", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "1026", "أذان القاهرة - عبدالباسط عبدالصمد", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "1027", "أذان القاهرة - محمد رفعت", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "1029", "أذان القاهرة - محمود علي البنا", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "1030", "أذان القاهرة - محمود خليل الحصري", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "1031", "أذان - جزر المالديف", "general", "");
            android.support.v4.media.l.s(allSounds, "1032", "أذان - ماليزيا", "general", "ماليزيا");
            android.support.v4.media.l.s(allSounds, "1033", "أذان - مسقط - عمان", "general", "");
            android.support.v4.media.l.s(allSounds, "1034", "أذان - السيد متولي العال - لبنان", "general", "");
            android.support.v4.media.l.s(allSounds, "1035", "أذان - عبدالرزاق صالح - لبنان", "general", "");
            android.support.v4.media.l.s(allSounds, "1036", "أذان الفجر - أم القيوين", "fajr", "الإمارات");
            android.support.v4.media.l.s(allSounds, "1037", "أذان - بصوت القارئ علي البراق", "general", "");
            android.support.v4.media.l.s(allSounds, "1038", "أذان - الهند", "general", "");
            android.support.v4.media.l.s(allSounds, "1039", "أذان - إندونيسيا", "general", "");
            android.support.v4.media.l.s(allSounds, "1040", "أذان - باكستان", "general", "");
            android.support.v4.media.l.s(allSounds, "1330868", "أذان - بصوت أحمد فخرو - قطر", "general", "");
            android.support.v4.media.l.s(allSounds, "162145", "أذان - الشيخ البهلول أبوعرقوب 1", "general", "");
            android.support.v4.media.l.s(allSounds, "162146", "أذان - الشيخ البهلول أبوعرقوب 2", "general", "");
            android.support.v4.media.l.s(allSounds, "162695", "أذان 4 - عبدالباسط عبدالصمد", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "162696", "أذان 3 - عبدالباسط عبدالصمد", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "163562", "أذان من فلسطين - صهيب هاني خطبا", "general", "فلسطين");
            android.support.v4.media.l.s(allSounds, "166475", "أذان الفجر - أحمد نواف المجلاد", "fajr", "");
            android.support.v4.media.l.s(allSounds, "168406", "أذان 1 - مشاري العفاسي", "general", "الكويت");
            android.support.v4.media.l.s(allSounds, "168408", "أذان 3 - مشاري العفاسي", "general", "الكويت");
            android.support.v4.media.l.s(allSounds, "168409", "أذان 4 - مشاري العفاسي", "general", "الكويت");
            android.support.v4.media.l.s(allSounds, "168410", "أذان الفجر 1 - مشاري العفاسي", "fajr", "الكويت");
            android.support.v4.media.l.s(allSounds, "168411", "أذان الفجر 2 - مشاري العفاسي", "fajr", "الكويت");
            android.support.v4.media.l.s(allSounds, "253116", "أذان - محمد صبحي", "general", "");
            android.support.v4.media.l.s(allSounds, "255029", "أذان - كامل يوسف البهتيمي", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "280215", "أذان - ناصر القطامي", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "280216", "أذان - عبدالولي الأركاني", "general", "");
            android.support.v4.media.l.s(allSounds, "280219", "أذان - ماهر المعيقلي", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "284019", "أذان - حسين آل الشيخ", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "308427", "أذان الفجر - ربيع عبدالرحيم عيسى", "fajr", "");
            android.support.v4.media.l.s(allSounds, "308842", "أذان - خالد الرشود 1", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "317311", "أذان - عبدالمجيد السريحي", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "317698", "أذان - ياسر الدوسري", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "317776", "أذان الفجر العراق - ياسين العساف", "fajr", "العراق");
            android.support.v4.media.l.s(allSounds, "318900", "أذان الفجر - عثمان المسيمي", "fajr", "");
            android.support.v4.media.l.s(allSounds, "318960", "أذان 1 - عثمان المسيمي", "general", "");
            android.support.v4.media.l.s(allSounds, "319070", "أذان 2 - عثمان المسيمي", "general", "");
            android.support.v4.media.l.s(allSounds, "319317", "أذان - عبدالله الزهراني", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "319686", "أذان - سلمان العتيبي 1", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "320110", "أذان الفجر - محمد ناجى", "fajr", "");
            android.support.v4.media.l.s(allSounds, "320111", "أذان - محمد ناجى", "general", "");
            android.support.v4.media.l.s(allSounds, "320766", "أذان - هشام عباس الجزائري", "general", "الجزائر");
            android.support.v4.media.l.s(allSounds, "321007", "أذان الفجر - عبد المنعم عبد المبدئ", "fajr", "");
            android.support.v4.media.l.s(allSounds, "321515", "أذان - عبدالرحمن شداد", "general", "");
            android.support.v4.media.l.s(allSounds, "321782", "أذان - محمد عبدالحكيم", "general", "");
            android.support.v4.media.l.s(allSounds, "4001", "أذان - عبدالباسط عبدالصمد 1", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "400105", "أذان - عبدالله بن زيد الشهراني", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "400249", "أذان 1 - أحمد الطرابلسي", "general", "");
            android.support.v4.media.l.s(allSounds, "400251", "أذان الفجر 1 - أحمد الطرابلسي", "fajr", "");
            android.support.v4.media.l.s(allSounds, "400252", "أذان الفجر 2 - أحمد الطرابلسي", "fajr", "");
            android.support.v4.media.l.s(allSounds, "400261", "أذان - محمد عبيد نجل الشيخ عبيد الطرابلسي", "general", "");
            android.support.v4.media.l.s(allSounds, "400474", "أذان الفجر - إبراهيم جبر أبو رحيق", "fajr", "");
            android.support.v4.media.l.s(allSounds, "4003", "أذان - أبو العنين شعيشع 2", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "4005", "أذان - عبدالباسط عبدالصمد 2", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "400685", "أذان - حسن شهاب", "general", "");
            android.support.v4.media.l.s(allSounds, "4006", "أذان - محمد رفعت 1", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "4008", "أذان - محمد رفعت 2", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "4010", "أذان - ناصر القطامي 2", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "4011", "أذان - مشاري العفاسي 5", "general", "الكويت");
            android.support.v4.media.l.s(allSounds, "4012", "أذان - توفيق الصائغ", "general", "");
            android.support.v4.media.l.s(allSounds, "4013", "أذان - علي الملا - الحرم المكي", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "4014", "أذان الفجر - عبدالباسط عبدالصمد", "fajr", "مصر");
            android.support.v4.media.l.s(allSounds, "4015", "أذان - مصطفى إسماعيل", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "4017", "أذان - الشيخ زكي داغستاني", "general", "");
            android.support.v4.media.l.s(allSounds, "4018", "أذان - ملا عثمان الموصلي", "general", "العراق");
            android.support.v4.media.l.s(allSounds, "4019", "أذان - فاروق حضراوي 1", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "4020", "أذان - فاروق حضراوي 2", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "4022", "أذان - رشاد سلامة", "general", "");
            android.support.v4.media.l.s(allSounds, "4023", "أذان - الحرم النبوي 1", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "4024", "أذان - عبد المجيد سريحي 2", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "4026", "أذان - هاني الرفاعي", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "4027", "أذان - سعود الشريم", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "4029", "أذان - إدريس أبكر", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "4030", "أذان - محمود خليل الحصري 2", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "4031", "أذان - عبدالرحمن السديس", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "4032", "أذان محمد صديق المنشاوي", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "4034", "أذان - يوسف إسلام (كات ستيفنز)", "general", "");
            android.support.v4.media.l.s(allSounds, "403801", "أذان خاشع - عبداللطيف الغامدي", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "404627", "أذان الفجر 1 - عبدالباسط عبدالصمد", "fajr", "مصر");
            android.support.v4.media.l.s(allSounds, "404628", "أذان الفجر 2 - عبدالباسط عبدالصمد", "fajr", "مصر");
            android.support.v4.media.l.s(allSounds, "405192", "أذان - أحمد سعيد مندور", "general", "");
            android.support.v4.media.l.s(allSounds, "405650", "أذان - أحمد محمد الأحمد", "general", "");
            android.support.v4.media.l.s(allSounds, "406048", "أذان - عبد الملك معتوق", "general", "");
            android.support.v4.media.l.s(allSounds, "406562", "أذان - ماجد سعيد حمزة", "general", "");
            android.support.v4.media.l.s(allSounds, "407170", "أذان - محمد خالد محارب", "general", "");
            android.support.v4.media.l.s(allSounds, "407697", "أذان - أبو شهد بن مطلق القحطاني", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "408049", "أذان - أبو شهد القحطاني 2", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "410685", "أذان - عبدالله كامل", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "411217", "أذان الفجر - عزالدين عمارنه", "fajr", "");
            android.support.v4.media.l.s(allSounds, "411274", "أذان الفجر نهاوند - أحمد العاني", "fajr", "العراق");
            android.support.v4.media.l.s(allSounds, "412092", "أذان - سعد الغامدي", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "412651", "أذان - عبدالرحمن الحذيفي", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "416974", "أذان - ياسر سلامة", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "418323", "أذان - عبدالمحسن القاسم", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "419970", "أذان - خالد الجليل", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "421877", "أذان - ماهر حمد المعيقلي 2", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "424164", "أذان - عبدالله عواد الجهني", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "425360", "أذان - بندر بليلة 1", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "425434", "أذان - بندر بليلة 2", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "425495", "أذان - نبيل العوضي", "general", "الكويت");
            android.support.v4.media.l.s(allSounds, "426698", "أذان - أحمد العجمي", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "427317", "أذان - محمد فتحى حنتور", "general", "");
            android.support.v4.media.l.s(allSounds, "428132", "أذان - توفيق الصايغ 2", "general", "");
            android.support.v4.media.l.s(allSounds, "428133", "أذان الفجر - السيد مسلم", "fajr", "");
            android.support.v4.media.l.s(allSounds, "429987", "أذان - حسام الدين عبادي", "general", "");
            android.support.v4.media.l.s(allSounds, "432210", "أذان - صالح الدرويش", "general", "");
            android.support.v4.media.l.s(allSounds, "438348", "أذان - محمد طه الجنيد", "general", "اليمن");
            android.support.v4.media.l.s(allSounds, "450199", "أذان - عبدالعزيز الأحمد", "general", "السعودية");
            android.support.v4.media.l.s(allSounds, "450508", "الشيخ محمد صديق المنشاوى - أذان رمضان", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "452695", "أذان - محمد سيد بدر", "general", "");
            android.support.v4.media.l.s(allSounds, "452804", "أذان الفجر 2 - إبراهيم جبر أبو رحيق", "fajr", "");
            android.support.v4.media.l.s(allSounds, "453573", "أذان - خالد علي المرزوق", "general", "");
            android.support.v4.media.l.s(allSounds, "456572", "أذان - عبد الرحمن الفقيه", "general", "");
            android.support.v4.media.l.s(allSounds, "458028", "أذان - إبراهيم جبر أبو رحيق", "general", "");
            android.support.v4.media.l.s(allSounds, "458244", "آذان 2 - وليد مهساس الجزائري", "general", "الجزائر");
            android.support.v4.media.l.s(allSounds, "488227", "أذان الفجر - مؤمن طارق سليم", "fajr", "");
            android.support.v4.media.l.s(allSounds, "489488", "آذان الفجر - حمد الدغريري - الحرم المكي", "fajr", "السعودية");
            android.support.v4.media.l.s(allSounds, "491844", "اذان الفجر - محمد حمودي - الجزائر", "fajr", "الجزائر");
            android.support.v4.media.l.s(allSounds, "491847", "اذان - محمد حمودي - الجزائر", "general", "الجزائر");
            android.support.v4.media.l.s(allSounds, "492076", "أذان - بكر جمال الفار - الأردن", "general", "الأردن");
            android.support.v4.media.l.s(allSounds, "494547", "أذان - حسن خلفان - قطر", "general", "");
            android.support.v4.media.l.s(allSounds, "530082", "الاذان - حيدر الجنابي العراقي", "general", "العراق");
            android.support.v4.media.l.s(allSounds, "614185", "الأذان - مصطفى السيد السويسي", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "614261", "الأذان - محمد أحمد البكري", "general", "");
            android.support.v4.media.l.s(allSounds, "615106", "الأذان - كامل اللالا - الأردن", "general", "الأردن");
            android.support.v4.media.l.s(allSounds, "615107", "أذان الفجر - كامل اللالا - الأردن", "fajr", "الأردن");
            android.support.v4.media.l.s(allSounds, "615402", "أذان الفجر - حسن مصطفى", "fajr", "");
            android.support.v4.media.l.s(allSounds, "617869", "أذان 2 - أبو عمر المعموري", "general", "");
            android.support.v4.media.l.s(allSounds, "617888", "أذان - محمد أحمد بدر", "general", "");
            android.support.v4.media.l.s(allSounds, "618002", "أذان - إبراهيم بن نايف الجبوري", "general", "");
            android.support.v4.media.l.s(allSounds, "619563", "الأذان - مصطفى هجرس", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "619607", "أذان حجازي - كمال المروش المغربي", "general", "المغرب");
            android.support.v4.media.l.s(allSounds, "619659", "الأذان - عبدالله عبدالمجيد المصري", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "622135", "الاذان - أبو همام الموصلي", "general", "العراق");
            android.support.v4.media.l.s(allSounds, "622596", "أذان الفجر - كامل اللالا", "fajr", "");
            android.support.v4.media.l.s(allSounds, "627537", "أذان - محمدي نذير - الجزائر", "general", "الجزائر");
            android.support.v4.media.l.s(allSounds, "627724", "أذان - محمد عبدالحفيظ الفقي", "general", "");
            android.support.v4.media.l.s(allSounds, "630908", "الأذان - عامر القيسي", "general", "العراق");
            android.support.v4.media.l.s(allSounds, "631475", "أذان - محمد صلاح نافع", "general", "");
            android.support.v4.media.l.s(allSounds, "634102", "الأذان - سعد الزبيدي", "general", "");
            android.support.v4.media.l.s(allSounds, "634429", "أذان - صالح عبدالله علي", "general", "");
            android.support.v4.media.l.s(allSounds, "634822", "الاذان - ضياء ناصر عبدالعال", "general", "");
            android.support.v4.media.l.s(allSounds, "635366", "اذان - سمير محمد سبيله", "general", "");
            android.support.v4.media.l.s(allSounds, "635533", "الاذان - صفوت قلموش", "general", "");
            android.support.v4.media.l.s(allSounds, "638880", "الاذان - محمود إبراهيم الصباغ", "general", "");
            android.support.v4.media.l.s(allSounds, "639271", "اذان - مصطفى سعد الفقى - مصر", "general", "مصر");
            android.support.v4.media.l.s(allSounds, "639355", "الاذان - عبدالرحمن أبوالعنين", "general", "");
            android.support.v4.media.l.s(allSounds, "640427", "الأذان - عبدالرحمن عوض", "general", "");
            android.support.v4.media.l.s(allSounds, "644998", "الأذان - ياسر سرحان الديب", "general", "");
            android.support.v4.media.l.s(allSounds, "648261", "آذان نهاوند - إبراهيم بن نايف الجبوري", "general", "");
            android.support.v4.media.l.s(allSounds, "649310", "آذان الفجر بنغم الكرد", "fajr", "");
            android.support.v4.media.l.s(allSounds, "650026", "أذان - أحمد بن علي مرتضى", "general", "");
            android.support.v4.media.l.s(allSounds, "650344", "أذان الفجر - أحمد بن علي مرتضى", "fajr", "");
            android.support.v4.media.l.s(allSounds, "665678", "آذان خاشع - صلاح الفي", "general", "");
            android.support.v4.media.l.s(allSounds, "665730", "الأذان - عامر محمود القيسي", "general", "");
        }
        return allSounds;
    }

    public static List<String> getCountries() {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (AdhanSound adhanSound : getAdhanSounds()) {
            String str = adhanSound.country;
            if (str != null && !str.isEmpty()) {
                linkedHashSet.add(adhanSound.country);
            }
        }
        return new ArrayList(linkedHashSet);
    }

    public static String getNameById(String str) {
        for (AdhanSound adhanSound : getAdhanSounds()) {
            if (adhanSound.id.equals(str)) {
                return adhanSound.name;
            }
        }
        return "غير محدد";
    }

    public static String getUrlById(String str) {
        return (str == null || str.isEmpty() || "none".equals(str)) ? "" : android.support.v4.media.l.h(REPO_BASE_URL, str, ".mp3");
    }
}
