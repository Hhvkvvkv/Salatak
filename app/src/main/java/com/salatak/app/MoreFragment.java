package com.salatak.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/* loaded from: classes2.dex */
public class MoreFragment extends Fragment {
    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateView$0(View view) {
        try {
            startActivity(new Intent(getActivity(), (Class<?>) RadioActivity.class));
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(getActivity(), "خطأ في فتح الراديو", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateView$1(View view) {
        try {
            startActivity(new Intent(getActivity(), (Class<?>) QuranAudioActivity.class));
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(getActivity(), "خطأ في فتح القرآن الكريم", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateView$2(View view) {
        try {
            startActivity(new Intent(getActivity(), (Class<?>) AzkarActivity.class));
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(getActivity(), "خطأ في فتح الأذكار", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateView$3(View view) {
        try {
            startActivity(new Intent(getActivity(), (Class<?>) StoriesActivity.class));
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(getActivity(), "خطأ في فتح القصص", 0).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateView$4(View view) {
        try {
            startActivity(new Intent(getActivity(), (Class<?>) TasbihActivity.class));
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(getActivity(), "خطأ في فتح المسبحة", 0).show();
        }
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_more, viewGroup, false);
        try {
            LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.btnRadio);
            if (linearLayout != null) {
                final int i2 = 0;
                linearLayout.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.q0
                    public final /* synthetic */ MoreFragment b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i2) {
                            case 0:
                                this.b.lambda$onCreateView$0(view);
                                break;
                            case 1:
                                this.b.lambda$onCreateView$1(view);
                                break;
                            case 2:
                                this.b.lambda$onCreateView$2(view);
                                break;
                            case 3:
                                this.b.lambda$onCreateView$3(view);
                                break;
                            default:
                                this.b.lambda$onCreateView$4(view);
                                break;
                        }
                    }
                });
            }
            LinearLayout linearLayout2 = (LinearLayout) inflate.findViewById(R.id.btnQuranAudio);
            if (linearLayout2 != null) {
                final int i3 = 1;
                linearLayout2.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.q0
                    public final /* synthetic */ MoreFragment b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i3) {
                            case 0:
                                this.b.lambda$onCreateView$0(view);
                                break;
                            case 1:
                                this.b.lambda$onCreateView$1(view);
                                break;
                            case 2:
                                this.b.lambda$onCreateView$2(view);
                                break;
                            case 3:
                                this.b.lambda$onCreateView$3(view);
                                break;
                            default:
                                this.b.lambda$onCreateView$4(view);
                                break;
                        }
                    }
                });
            }
            LinearLayout linearLayout3 = (LinearLayout) inflate.findViewById(R.id.btnAzkar);
            if (linearLayout3 != null) {
                final int i4 = 2;
                linearLayout3.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.q0
                    public final /* synthetic */ MoreFragment b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i4) {
                            case 0:
                                this.b.lambda$onCreateView$0(view);
                                break;
                            case 1:
                                this.b.lambda$onCreateView$1(view);
                                break;
                            case 2:
                                this.b.lambda$onCreateView$2(view);
                                break;
                            case 3:
                                this.b.lambda$onCreateView$3(view);
                                break;
                            default:
                                this.b.lambda$onCreateView$4(view);
                                break;
                        }
                    }
                });
            }
            LinearLayout linearLayout4 = (LinearLayout) inflate.findViewById(R.id.btnStories);
            if (linearLayout4 != null) {
                final int i5 = 3;
                linearLayout4.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.q0
                    public final /* synthetic */ MoreFragment b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i5) {
                            case 0:
                                this.b.lambda$onCreateView$0(view);
                                break;
                            case 1:
                                this.b.lambda$onCreateView$1(view);
                                break;
                            case 2:
                                this.b.lambda$onCreateView$2(view);
                                break;
                            case 3:
                                this.b.lambda$onCreateView$3(view);
                                break;
                            default:
                                this.b.lambda$onCreateView$4(view);
                                break;
                        }
                    }
                });
            }
            LinearLayout linearLayout5 = (LinearLayout) inflate.findViewById(R.id.btnTasbih);
            if (linearLayout5 != null) {
                final int i6 = 4;
                linearLayout5.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.q0
                    public final /* synthetic */ MoreFragment b;

                    {
                        this.b = this;
                    }

                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        switch (i6) {
                            case 0:
                                this.b.lambda$onCreateView$0(view);
                                break;
                            case 1:
                                this.b.lambda$onCreateView$1(view);
                                break;
                            case 2:
                                this.b.lambda$onCreateView$2(view);
                                break;
                            case 3:
                                this.b.lambda$onCreateView$3(view);
                                break;
                            default:
                                this.b.lambda$onCreateView$4(view);
                                break;
                        }
                    }
                });
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return inflate;
    }
}
