package com.salatak.app;

import android.util.Base64;
import android.util.Log;
import androidx.media3.extractor.text.ttml.TtmlNode;
import com.android.volley.toolbox.HttpHeaderParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class GitHubApi {
    private static final String API_BASE = "https://api.github.com/repos/mabdulhakim248-crypto/salatak-backend";
    private static final byte[] EK = {90, 60, 113, 79, -126, 43, 109, -111};
    private static final byte[] ET = {61, 84, 1, 16, -29, 121, 11, -88, 46, 81, 40, 54, -48, 93, 31, -61, 107, 91, 18, 40, -22, 70, 39, -34, 52, 76, 5, 2, -14, 93, 43, -40, 57, 118, 64, 10, -54, 120, 90, -38};
    private static final String REPO = "mabdulhakim248-crypto/salatak-backend";
    private static final String TAG = "GitHubApi";

    public static JSONArray getChatMessages(String str) {
        try {
            return new JSONArray(readFile("chats/" + str + ".json"));
        } catch (Exception unused) {
            return new JSONArray();
        }
    }

    public static String getFileSha(String str) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(android.support.v4.media.l.t("https://api.github.com/repos/mabdulhakim248-crypto/salatak-backend/contents/", str)).openConnection();
        httpURLConnection.setRequestProperty("Authorization", "token " + resolveToken());
        httpURLConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");
        if (httpURLConnection.getResponseCode() != 200) {
            httpURLConnection.disconnect();
            return null;
        }
        String readStream = readStream(httpURLConnection.getInputStream());
        httpURLConnection.disconnect();
        try {
            return new JSONObject(readStream).getString("sha");
        } catch (JSONException unused) {
            return null;
        }
    }

    public static JSONArray getNews() {
        try {
            return new JSONArray(readFile("news.json"));
        } catch (Exception e2) {
            Log.e(TAG, "Error reading news", e2);
            return new JSONArray();
        }
    }

    public static JSONArray getUpdates() {
        try {
            return new JSONArray(readFile("updates.json"));
        } catch (Exception e2) {
            Log.e(TAG, "Error reading updates", e2);
            return new JSONArray();
        }
    }

    public static String readFile(String str) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(android.support.v4.media.l.t("https://api.github.com/repos/mabdulhakim248-crypto/salatak-backend/contents/", str)).openConnection();
        httpURLConnection.setRequestProperty("Authorization", "token " + resolveToken());
        httpURLConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");
        httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode != 200) {
            httpURLConnection.disconnect();
            throw new IOException(android.support.v4.media.l.c(responseCode, "GitHub API error: "));
        }
        String readStream = readStream(httpURLConnection.getInputStream());
        httpURLConnection.disconnect();
        try {
            return new String(Base64.decode(new JSONObject(readStream).getString("content").replaceAll("\\s", ""), 0), StandardCharsets.UTF_8);
        } catch (JSONException e2) {
            throw new IOException("JSON parse error", e2);
        }
    }

    private static String readStream(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                bufferedReader.close();
                return sb.toString();
            }
            sb.append(readLine);
        }
    }

    public static JSONObject registerUser(String str, String str2, String str3) {
        JSONArray jSONArray;
        try {
            jSONArray = new JSONArray(readFile("users.json"));
        } catch (Exception unused) {
            jSONArray = new JSONArray();
        }
        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i2);
            if (jSONObject.getString("email").equals(str2)) {
                if (jSONObject.getString("password").equals(str3)) {
                    return jSONObject;
                }
                throw new Exception("كلمة المرور غير صحيحة");
            }
        }
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put(TtmlNode.ATTR_ID, "user_" + System.currentTimeMillis());
        jSONObject2.put("username", str);
        jSONObject2.put("email", str2);
        jSONObject2.put("password", str3);
        jSONArray.put(jSONObject2);
        writeFile("users.json", jSONArray.toString(2), "Register user: " + str);
        return jSONObject2;
    }

    private static String resolveToken() {
        byte[] bArr = new byte[ET.length];
        int i2 = 0;
        while (true) {
            byte[] bArr2 = ET;
            if (i2 >= bArr2.length) {
                return new String(bArr, StandardCharsets.US_ASCII);
            }
            byte b = bArr2[i2];
            byte[] bArr3 = EK;
            bArr[i2] = (byte) (b ^ bArr3[i2 % bArr3.length]);
            i2++;
        }
    }

    public static void sendChatMessage(String str, JSONObject jSONObject) {
        JSONArray chatMessages = getChatMessages(str);
        chatMessages.put(jSONObject);
        writeFile("chats/" + str + ".json", chatMessages.toString(2), "Chat message from " + str);
    }

    public static String uploadFileToRepo(String str, byte[] bArr, String str2) {
        String fileSha = getFileSha(str);
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(android.support.v4.media.l.t("https://api.github.com/repos/mabdulhakim248-crypto/salatak-backend/contents/", str)).openConnection();
        httpURLConnection.setRequestMethod("PUT");
        httpURLConnection.setRequestProperty("Authorization", "token " + resolveToken());
        httpURLConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");
        httpURLConnection.setRequestProperty(HttpHeaderParser.HEADER_CONTENT_TYPE, "application/json");
        httpURLConnection.setDoOutput(true);
        String encodeToString = Base64.encodeToString(bArr, 2);
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("message", str2);
            jSONObject.put("content", encodeToString);
            if (fileSha != null) {
                jSONObject.put("sha", fileSha);
            }
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(jSONObject.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.close();
            int responseCode = httpURLConnection.getResponseCode();
            String readStream = (responseCode == 200 || responseCode == 201) ? readStream(httpURLConnection.getInputStream()) : "";
            httpURLConnection.disconnect();
            if (responseCode != 200 && responseCode != 201) {
                throw new IOException(android.support.v4.media.l.c(responseCode, "Upload error: "));
            }
            try {
                return new JSONObject(readStream).getJSONObject("content").getString("download_url");
            } catch (JSONException e2) {
                throw new IOException("JSON parse error", e2);
            }
        } catch (JSONException e3) {
            throw new IOException("JSON error", e3);
        }
    }

    public static void writeFile(String str, String str2, String str3) {
        String fileSha = getFileSha(str);
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(android.support.v4.media.l.t("https://api.github.com/repos/mabdulhakim248-crypto/salatak-backend/contents/", str)).openConnection();
        httpURLConnection.setRequestMethod("PUT");
        httpURLConnection.setRequestProperty("Authorization", "token " + resolveToken());
        httpURLConnection.setRequestProperty("Accept", "application/vnd.github.v3+json");
        httpURLConnection.setRequestProperty(HttpHeaderParser.HEADER_CONTENT_TYPE, "application/json");
        httpURLConnection.setDoOutput(true);
        Charset charset = StandardCharsets.UTF_8;
        String encodeToString = Base64.encodeToString(str2.getBytes(charset), 2);
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("message", str3);
            jSONObject.put("content", encodeToString);
            if (fileSha != null) {
                jSONObject.put("sha", fileSha);
            }
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(jSONObject.toString().getBytes(charset));
            outputStream.close();
            int responseCode = httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();
            if (responseCode != 200 && responseCode != 201) {
                throw new IOException(android.support.v4.media.l.c(responseCode, "GitHub write error: "));
            }
        } catch (JSONException e2) {
            throw new IOException("JSON error", e2);
        }
    }
}
