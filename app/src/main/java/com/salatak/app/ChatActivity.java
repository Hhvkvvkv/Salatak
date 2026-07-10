package com.salatak.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.media3.common.MimeTypes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class ChatActivity extends AppCompatActivity {
    private static final int PERMISSION_AUDIO = 1002;
    private static final int PICK_IMAGE = 1001;
    private ChatAdapter adapter;
    private String audioFilePath;
    private MediaPlayer audioPlayer;
    private ImageView btnAttach;
    private ImageView btnMic;
    private ImageView btnSend;
    private EditText etMessage;
    private Uri pendingImageUri;
    private MediaRecorder recorder;
    private RecyclerView rvMessages;
    private String userId;
    private String username;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private List<JSONObject> messages = new ArrayList();
    private boolean isRecording = false;

    public class ChatAdapter extends RecyclerView.Adapter<ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout audioContainer;
            ImageView btnPlayAudio;
            LinearLayout bubbleContainer;
            ImageView ivImage;
            TextView tvAudioDuration;
            TextView tvMessage;
            TextView tvTime;

            public ViewHolder(@NonNull View view) {
                super(view);
                this.bubbleContainer = (LinearLayout) view.findViewById(R.id.bubbleContainer);
                this.tvMessage = (TextView) view.findViewById(R.id.tvMessage);
                this.tvTime = (TextView) view.findViewById(R.id.tvTime);
                this.ivImage = (ImageView) view.findViewById(R.id.ivImage);
                this.btnPlayAudio = (ImageView) view.findViewById(R.id.btnPlayAudio);
                this.tvAudioDuration = (TextView) view.findViewById(R.id.tvAudioDuration);
                this.audioContainer = (LinearLayout) view.findViewById(R.id.audioContainer);
            }
        }

        public ChatAdapter() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$0(String str, View view) {
            ChatActivity.this.playAudio(str);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return ChatActivity.this.messages.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i2) {
            try {
                JSONObject jSONObject = (JSONObject) ChatActivity.this.messages.get(i2);
                String optString = jSONObject.optString("from", "user");
                String optString2 = jSONObject.optString("text", "");
                String optString3 = jSONObject.optString("type", "text");
                String optString4 = jSONObject.optString("timestamp", "");
                boolean equals = "user".equals(optString);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewHolder.bubbleContainer.getLayoutParams();
                if (equals) {
                    layoutParams.gravity = GravityCompat.END;
                    viewHolder.bubbleContainer.setBackgroundResource(R.drawable.chat_bubble_user);
                } else {
                    layoutParams.gravity = GravityCompat.START;
                    viewHolder.bubbleContainer.setBackgroundResource(R.drawable.chat_bubble_admin);
                }
                viewHolder.bubbleContainer.setLayoutParams(layoutParams);
                viewHolder.tvMessage.setText(optString2);
                viewHolder.tvMessage.setVisibility(optString2.isEmpty() ? 8 : 0);
                if (optString4.length() > 10) {
                    viewHolder.tvTime.setText(optString4.substring(11));
                } else {
                    viewHolder.tvTime.setText(optString4);
                }
                if ("image".equals(optString3)) {
                    viewHolder.ivImage.setVisibility(0);
                    ChatActivity.this.loadImageAsync(jSONObject.optString("image_url", ""), viewHolder.ivImage);
                } else {
                    viewHolder.ivImage.setVisibility(8);
                }
                if (!MimeTypes.BASE_TYPE_AUDIO.equals(optString3)) {
                    viewHolder.audioContainer.setVisibility(8);
                } else {
                    viewHolder.audioContainer.setVisibility(0);
                    viewHolder.btnPlayAudio.setOnClickListener(new j0(0, this, jSONObject.optString("audio_url", "")));
                }
            } catch (Exception unused) {
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i2) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_message, viewGroup, false));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadImageAsync$13(String str, ImageView imageView) {
        try {
            InputStream openStream = new URL(str).openStream();
            Bitmap decodeStream = BitmapFactory.decodeStream(openStream);
            openStream.close();
            this.mainHandler.post(new e0(1, imageView, decodeStream));
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadMessages$4(List list) {
        this.messages.clear();
        this.messages.addAll(list);
        this.adapter.notifyDataSetChanged();
        if (this.messages.isEmpty()) {
            return;
        }
        this.rvMessages.scrollToPosition(this.messages.size() - 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadMessages$5() {
        JSONArray chatMessages = GitHubApi.getChatMessages(this.userId);
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < chatMessages.length(); i2++) {
            try {
                arrayList.add(chatMessages.getJSONObject(i2));
            } catch (Exception unused) {
            }
        }
        this.mainHandler.post(new e0(3, this, arrayList));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        sendTextMessage();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2(View view) {
        pickImage();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$3(View view) {
        toggleRecording();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$playAudio$14(MediaPlayer mediaPlayer) {
        mediaPlayer.release();
        this.audioPlayer = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendTextMessage$6() {
        this.btnSend.setEnabled(true);
        lambda$sendVoiceMessage$9();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendTextMessage$7(Exception exc) {
        this.btnSend.setEnabled(true);
        Toast.makeText(this, "خطأ: " + exc.getMessage(), 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendTextMessage$8(String str) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("from", "user");
            jSONObject.put("username", this.username);
            jSONObject.put("text", str);
            jSONObject.put("type", "text");
            jSONObject.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            if (this.pendingImageUri != null) {
                try {
                    InputStream openInputStream = getContentResolver().openInputStream(this.pendingImageUri);
                    Bitmap decodeStream = BitmapFactory.decodeStream(openInputStream);
                    if (openInputStream != null) {
                        openInputStream.close();
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    decodeStream.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    jSONObject.put("image_url", GitHubApi.uploadFileToRepo("media/" + this.userId + "/img_" + System.currentTimeMillis() + ".jpg", byteArray, "Image from " + this.username));
                    jSONObject.put("type", "image");
                } catch (Exception unused) {
                    jSONObject.put("text", str + "\n[فشل إرفاق الصورة]");
                }
                this.pendingImageUri = null;
            }
            GitHubApi.sendChatMessage(this.userId, jSONObject);
            this.mainHandler.post(new g0(this, 0));
        } catch (Exception e2) {
            this.mainHandler.post(new h0(this, e2, 0));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendVoiceMessage$10(Exception exc) {
        Toast.makeText(this, "خطأ: " + exc.getMessage(), 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendVoiceMessage$11() {
        try {
            File file = new File(this.audioFilePath);
            byte[] bArr = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bArr);
            fileInputStream.close();
            String uploadFileToRepo = GitHubApi.uploadFileToRepo("media/" + this.userId + "/voice_" + System.currentTimeMillis() + ".m4a", bArr, "Voice from " + this.username);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("from", "user");
            jSONObject.put("username", this.username);
            jSONObject.put("text", "رسالة صوتية");
            jSONObject.put("type", MimeTypes.BASE_TYPE_AUDIO);
            jSONObject.put("audio_url", uploadFileToRepo);
            jSONObject.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            GitHubApi.sendChatMessage(this.userId, jSONObject);
            file.delete();
            this.mainHandler.post(new g0(this, 2));
        } catch (Exception e2) {
            this.mainHandler.post(new h0(this, e2, 1));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadImageAsync(String str, ImageView imageView) {
        Executors.newSingleThreadExecutor().execute(new b0(this, str, imageView, 1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: loadMessages, reason: merged with bridge method [inline-methods] */
    public void lambda$sendVoiceMessage$9() {
        Executors.newSingleThreadExecutor().execute(new g0(this, 1));
    }

    private void pickImage() {
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        startActivityForResult(intent, 1001);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playAudio(String str) {
        try {
            MediaPlayer mediaPlayer = this.audioPlayer;
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            MediaPlayer mediaPlayer2 = new MediaPlayer();
            this.audioPlayer = mediaPlayer2;
            mediaPlayer2.setDataSource(str);
            this.audioPlayer.prepareAsync();
            this.audioPlayer.setOnPreparedListener(new x0(4));
            this.audioPlayer.setOnCompletionListener(new v0(3, this));
        } catch (Exception unused) {
            Toast.makeText(this, "خطأ في تشغيل الصوت", 0).show();
        }
    }

    private void sendTextMessage() {
        String trim = this.etMessage.getText().toString().trim();
        if (trim.isEmpty() && this.pendingImageUri == null) {
            return;
        }
        this.etMessage.setText("");
        this.btnSend.setEnabled(false);
        Executors.newSingleThreadExecutor().execute(new e0(2, this, trim));
    }

    private void sendVoiceMessage() {
        Toast.makeText(this, "جاري إرسال الرسالة الصوتية...", 0).show();
        Executors.newSingleThreadExecutor().execute(new g0(this, 3));
    }

    private void startRecording() {
        try {
            this.audioFilePath = getCacheDir().getAbsolutePath() + "/voice_" + System.currentTimeMillis() + ".m4a";
            MediaRecorder mediaRecorder = new MediaRecorder();
            this.recorder = mediaRecorder;
            mediaRecorder.setAudioSource(1);
            this.recorder.setOutputFormat(2);
            this.recorder.setAudioEncoder(3);
            this.recorder.setAudioEncodingBitRate(128000);
            this.recorder.setAudioSamplingRate(44100);
            this.recorder.setOutputFile(this.audioFilePath);
            this.recorder.prepare();
            this.recorder.start();
            this.isRecording = true;
            this.btnMic.setColorFilter(-44462);
            Toast.makeText(this, "جاري التسجيل... اضغط مرة أخرى للإيقاف", 0).show();
        } catch (Exception e2) {
            Toast.makeText(this, "خطأ في التسجيل: " + e2.getMessage(), 0).show();
        }
    }

    private void stopRecording() {
        try {
            this.recorder.stop();
            this.recorder.release();
            this.recorder = null;
            this.isRecording = false;
            this.btnMic.clearColorFilter();
            sendVoiceMessage();
        } catch (Exception unused) {
            this.isRecording = false;
            this.btnMic.clearColorFilter();
            Toast.makeText(this, "خطأ في إيقاف التسجيل", 0).show();
        }
    }

    private void toggleRecording() {
        if (this.isRecording) {
            stopRecording();
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.RECORD_AUDIO"}, 1002);
        } else {
            startRecording();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i2, int i3, @Nullable Intent intent) {
        super.onActivityResult(i2, i3, intent);
        if (i2 == 1001 && i3 == -1 && intent != null) {
            this.pendingImageUri = intent.getData();
            Toast.makeText(this, "تم اختيار الصورة - اضغط إرسال", 0).show();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.setStatusBarColor(-15918294);
        window.setNavigationBarColor(-15918294);
        setContentView(R.layout.activity_chat);
        SharedPreferences sharedPreferences = getSharedPreferences("SalatakPrefs", 0);
        this.userId = sharedPreferences.getString("report_user_id", "");
        this.username = sharedPreferences.getString("report_username", "");
        if (this.userId.isEmpty()) {
            finish();
            return;
        }
        this.rvMessages = (RecyclerView) findViewById(R.id.rvMessages);
        this.etMessage = (EditText) findViewById(R.id.etMessage);
        this.btnSend = (ImageView) findViewById(R.id.btnSend);
        this.btnMic = (ImageView) findViewById(R.id.btnMic);
        this.btnAttach = (ImageView) findViewById(R.id.btnAttach);
        final int i2 = 0;
        ((ImageView) findViewById(R.id.btnBack)).setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.i0
            public final /* synthetic */ ChatActivity b;

            {
                this.b = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (i2) {
                    case 0:
                        this.b.lambda$onCreate$0(view);
                        break;
                    case 1:
                        this.b.lambda$onCreate$1(view);
                        break;
                    case 2:
                        this.b.lambda$onCreate$2(view);
                        break;
                    default:
                        this.b.lambda$onCreate$3(view);
                        break;
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        this.rvMessages.setLayoutManager(linearLayoutManager);
        ChatAdapter chatAdapter = new ChatAdapter();
        this.adapter = chatAdapter;
        this.rvMessages.setAdapter(chatAdapter);
        final int i3 = 1;
        this.btnSend.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.i0
            public final /* synthetic */ ChatActivity b;

            {
                this.b = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (i3) {
                    case 0:
                        this.b.lambda$onCreate$0(view);
                        break;
                    case 1:
                        this.b.lambda$onCreate$1(view);
                        break;
                    case 2:
                        this.b.lambda$onCreate$2(view);
                        break;
                    default:
                        this.b.lambda$onCreate$3(view);
                        break;
                }
            }
        });
        final int i4 = 2;
        this.btnAttach.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.i0
            public final /* synthetic */ ChatActivity b;

            {
                this.b = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (i4) {
                    case 0:
                        this.b.lambda$onCreate$0(view);
                        break;
                    case 1:
                        this.b.lambda$onCreate$1(view);
                        break;
                    case 2:
                        this.b.lambda$onCreate$2(view);
                        break;
                    default:
                        this.b.lambda$onCreate$3(view);
                        break;
                }
            }
        });
        final int i5 = 3;
        this.btnMic.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.i0
            public final /* synthetic */ ChatActivity b;

            {
                this.b = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (i5) {
                    case 0:
                        this.b.lambda$onCreate$0(view);
                        break;
                    case 1:
                        this.b.lambda$onCreate$1(view);
                        break;
                    case 2:
                        this.b.lambda$onCreate$2(view);
                        break;
                    default:
                        this.b.lambda$onCreate$3(view);
                        break;
                }
            }
        });
        lambda$sendVoiceMessage$9();
        this.mainHandler.postDelayed(new Runnable() { // from class: com.salatak.app.ChatActivity.1
            @Override // java.lang.Runnable
            public void run() {
                if (ChatActivity.this.isFinishing()) {
                    return;
                }
                ChatActivity.this.lambda$sendVoiceMessage$9();
                ChatActivity.this.mainHandler.postDelayed(this, 10000L);
            }
        }, 10000L);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        MediaRecorder mediaRecorder = this.recorder;
        if (mediaRecorder != null) {
            try {
                mediaRecorder.release();
            } catch (Exception unused) {
            }
        }
        MediaPlayer mediaPlayer = this.audioPlayer;
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception unused2) {
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i2, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i2, strArr, iArr);
        if (i2 == 1002 && iArr.length > 0 && iArr[0] == 0) {
            startRecording();
        }
    }
}
