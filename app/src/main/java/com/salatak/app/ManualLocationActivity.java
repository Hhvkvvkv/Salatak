package com.salatak.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class ManualLocationActivity extends AppCompatActivity {
    private Button btnRemoveLocation;
    private Button btnSearch;
    private CardView cardCurrentLocation;
    private EditText etCitySearch;
    private List<Address> foundAddresses = new ArrayList();
    private ListView listResults;
    private SharedPreferences prefs;
    private ProgressBar progressSearch;
    private TextView tvCurrentLocation;
    private TextView tvNoResults;
    private TextView tvResultsLabel;

    private String buildDisplayName(Address address) {
        StringBuilder sb = new StringBuilder();
        String locality = address.getLocality();
        String subLocality = address.getSubLocality();
        String adminArea = address.getAdminArea();
        String countryName = address.getCountryName();
        if (locality != null && !locality.isEmpty()) {
            sb.append(locality);
        } else if (subLocality != null && !subLocality.isEmpty()) {
            sb.append(subLocality);
        }
        if (adminArea != null && !adminArea.isEmpty()) {
            if (sb.length() > 0) {
                sb.append("، ");
            }
            sb.append(adminArea);
        }
        if (countryName != null && !countryName.isEmpty()) {
            if (sb.length() > 0) {
                sb.append("، ");
            }
            sb.append(countryName);
        }
        if (sb.length() == 0) {
            sb.append(String.format("%.4f, %.4f", Double.valueOf(address.getLatitude()), Double.valueOf(address.getLongitude())));
        }
        return sb.toString();
    }

    private void hideKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService("input_method");
            View currentFocus = getCurrentFocus();
            if (currentFocus == null || inputMethodManager == null) {
                return;
            }
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        performSearch();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onCreate$2(TextView textView, int i2, KeyEvent keyEvent) {
        if (i2 != 3) {
            return false;
        }
        performSearch();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$3(View view) {
        showRemoveConfirmDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSearch$4(List list) {
        setSearching(false);
        this.foundAddresses = list;
        showResults(list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$performSearch$5(String str) {
        List<Address> fromLocationName;
        ArrayList arrayList = new ArrayList();
        try {
            List<Address> fromLocationName2 = new Geocoder(this, Locale.getDefault()).getFromLocationName(str, 15);
            if (fromLocationName2 != null) {
                for (Address address : fromLocationName2) {
                    if (address.hasLatitude() && address.hasLongitude()) {
                        arrayList.add(address);
                    }
                }
            }
            if (arrayList.isEmpty() && (fromLocationName = new Geocoder(this, new Locale("en")).getFromLocationName(str, 15)) != null) {
                for (Address address2 : fromLocationName) {
                    if (address2.hasLatitude() && address2.hasLongitude()) {
                        arrayList.add(address2);
                    }
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        runOnUiThread(new e0(5, this, arrayList));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showActivateDialog$7(Address address, String str, DialogInterface dialogInterface, int i2) {
        SharedPreferences.Editor edit = this.prefs.edit();
        edit.putBoolean("manual_location_enabled", true);
        edit.putFloat("manual_location_lat", (float) address.getLatitude());
        edit.putFloat("manual_location_lon", (float) address.getLongitude());
        edit.putString("manual_location_name", str);
        edit.putFloat("latitude", (float) address.getLatitude());
        edit.putFloat("longitude", (float) address.getLongitude());
        edit.apply();
        Toast.makeText(this, "تم تفعيل الموقع اليدوي: " + str, 1).show();
        setResult(-1);
        updateCurrentLocationCard();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showRemoveConfirmDialog$8(DialogInterface dialogInterface, int i2) {
        SharedPreferences.Editor edit = this.prefs.edit();
        edit.putBoolean("manual_location_enabled", false);
        edit.remove("manual_location_lat");
        edit.remove("manual_location_lon");
        edit.remove("manual_location_name");
        edit.apply();
        Toast.makeText(this, "تم الرجوع إلى الموقع التلقائي", 0).show();
        updateCurrentLocationCard();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showResults$6(List list, AdapterView adapterView, View view, int i2, long j2) {
        if (i2 < this.foundAddresses.size()) {
            showActivateDialog(this.foundAddresses.get(i2), (String) list.get(i2));
        }
    }

    private void performSearch() {
        EditText editText = this.etCitySearch;
        if (editText == null) {
            return;
        }
        String trim = editText.getText().toString().trim();
        if (trim.isEmpty()) {
            Toast.makeText(this, "يرجى إدخال اسم المدينة", 0).show();
            return;
        }
        hideKeyboard();
        setSearching(true);
        new Thread(new e0(4, this, trim)).start();
    }

    private void setSearching(boolean z2) {
        ProgressBar progressBar = this.progressSearch;
        if (progressBar != null) {
            progressBar.setVisibility(z2 ? 0 : 8);
        }
        Button button = this.btnSearch;
        if (button != null) {
            button.setEnabled(!z2);
        }
        TextView textView = this.tvNoResults;
        if (textView != null && z2) {
            textView.setVisibility(8);
        }
        TextView textView2 = this.tvResultsLabel;
        if (textView2 == null || !z2) {
            return;
        }
        textView2.setVisibility(8);
    }

    private void showActivateDialog(Address address, String str) {
        this.prefs.getBoolean("manual_location_enabled", false);
        this.prefs.getString("manual_location_name", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(this, 4);
        builder.setTitle("تحديد الموقع يدوياً");
        builder.setMessage("هل تريد تفعيل وضع الموقع اليدوي لـ:\n\n" + str + "؟");
        builder.setPositiveButton("تفعيل", new m0(this, address, str, 0));
        builder.setNegativeButton("إلغاء", (DialogInterface.OnClickListener) null);
        builder.show();
    }

    private void showRemoveConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, 4);
        builder.setTitle("إزالة الموقع اليدوي");
        builder.setMessage("هل تريد إزالة تحديد الموقع اليدوي والرجوع إلى الموقع التلقائي؟");
        builder.setPositiveButton("إزالة", new q(1, this));
        builder.setNegativeButton("إلغاء", (DialogInterface.OnClickListener) null);
        builder.show();
    }

    private void showResults(List<Address> list) {
        if (this.listResults == null) {
            return;
        }
        if (list.isEmpty()) {
            this.tvNoResults.setVisibility(0);
            this.tvResultsLabel.setVisibility(8);
            this.listResults.setAdapter((ListAdapter) null);
            return;
        }
        this.tvNoResults.setVisibility(8);
        this.tvResultsLabel.setVisibility(0);
        final ArrayList arrayList = new ArrayList();
        Iterator<Address> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(buildDisplayName(it.next()));
        }
        this.listResults.setAdapter((ListAdapter) new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList) { // from class: com.salatak.app.ManualLocationActivity.1
            @Override // android.widget.ArrayAdapter, android.widget.Adapter
            public View getView(int i2, View view, ViewGroup viewGroup) {
                View view2 = super.getView(i2, view, viewGroup);
                TextView textView = (TextView) view2.findViewById(android.R.id.text1);
                textView.setTextColor(-1);
                textView.setTextSize(15.0f);
                textView.setPadding(24, 20, 24, 20);
                textView.setGravity(21);
                view2.setBackgroundColor(-15655637);
                return view2;
            }
        });
        this.listResults.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.salatak.app.p0
            @Override // android.widget.AdapterView.OnItemClickListener
            public final void onItemClick(AdapterView adapterView, View view, int i2, long j2) {
                ManualLocationActivity.this.lambda$showResults$6(arrayList, adapterView, view, i2, j2);
            }
        });
    }

    private void updateCurrentLocationCard() {
        if (!this.prefs.getBoolean("manual_location_enabled", false) || this.cardCurrentLocation == null) {
            CardView cardView = this.cardCurrentLocation;
            if (cardView != null) {
                cardView.setVisibility(8);
                return;
            }
            return;
        }
        String string = this.prefs.getString("manual_location_name", "");
        this.cardCurrentLocation.setVisibility(0);
        TextView textView = this.tvCurrentLocation;
        if (textView != null) {
            if (string.isEmpty()) {
                string = "موقع محدد يدوياً";
            }
            textView.setText(string);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_manual_location);
        getWindow().setStatusBarColor(-15918294);
        getWindow().setNavigationBarColor(-15918294);
        this.prefs = getSharedPreferences("SalatakPrefs", 0);
        ImageView imageView = (ImageView) findViewById(R.id.btnBack);
        this.etCitySearch = (EditText) findViewById(R.id.etCitySearch);
        this.btnSearch = (Button) findViewById(R.id.btnSearch);
        this.listResults = (ListView) findViewById(R.id.listResults);
        this.progressSearch = (ProgressBar) findViewById(R.id.progressSearch);
        this.tvNoResults = (TextView) findViewById(R.id.tvNoResults);
        this.tvResultsLabel = (TextView) findViewById(R.id.tvResultsLabel);
        this.cardCurrentLocation = (CardView) findViewById(R.id.cardCurrentLocation);
        this.tvCurrentLocation = (TextView) findViewById(R.id.tvCurrentLocation);
        this.btnRemoveLocation = (Button) findViewById(R.id.btnRemoveLocation);
        if (imageView != null) {
            final int i2 = 0;
            imageView.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.n0
                public final /* synthetic */ ManualLocationActivity b;

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
                        default:
                            this.b.lambda$onCreate$3(view);
                            break;
                    }
                }
            });
        }
        updateCurrentLocationCard();
        Button button = this.btnSearch;
        if (button != null) {
            final int i3 = 1;
            button.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.n0
                public final /* synthetic */ ManualLocationActivity b;

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
                        default:
                            this.b.lambda$onCreate$3(view);
                            break;
                    }
                }
            });
        }
        EditText editText = this.etCitySearch;
        if (editText != null) {
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.salatak.app.o0
                @Override // android.widget.TextView.OnEditorActionListener
                public final boolean onEditorAction(TextView textView, int i4, KeyEvent keyEvent) {
                    boolean lambda$onCreate$2;
                    lambda$onCreate$2 = ManualLocationActivity.this.lambda$onCreate$2(textView, i4, keyEvent);
                    return lambda$onCreate$2;
                }
            });
        }
        Button button2 = this.btnRemoveLocation;
        if (button2 != null) {
            final int i4 = 2;
            button2.setOnClickListener(new View.OnClickListener(this) { // from class: com.salatak.app.n0
                public final /* synthetic */ ManualLocationActivity b;

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
                        default:
                            this.b.lambda$onCreate$3(view);
                            break;
                    }
                }
            });
        }
    }
}
