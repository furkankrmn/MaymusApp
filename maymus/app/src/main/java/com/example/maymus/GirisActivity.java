package com.example.maymus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.InterstitialAd;
import com.huawei.hms.ads.banner.BannerView;

public class GirisActivity extends AppCompatActivity {

    EditText kullaniciAdiEditText;
    Button kayitOlButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        HwAds.init(this); //HMS ads servisini tetikledik.
        loadBannerAdd(); //yüklenirken banneri yükledik

        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdId("testb4znbuh3n2");//video test id ekledik.
        interstitialAd.setAdListener(adListener);
        AdParam adParam = new AdParam.Builder().build();
        interstitialAd.loadAd(adParam);
        tanimla();
    }
    public void loadBannerAdd() {
        //ad parmeter objesini oluşturduk
        AdParam adParam = new AdParam.Builder().build();
        //banner view oluşturduk.
        BannerView bannerView = new BannerView(this);
        // Reklam ıd miz
        bannerView.setAdId("testw6vs28auh3");
        bannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        // Add BannerView to the layout.
        //Reklamın gözükeceği layout eriştik.
        RelativeLayout rootView = findViewById(R.id.Bannerüst);
        rootView.addView(bannerView);

        bannerView.loadAd(adParam);
    } //Reklam kodları

    private final AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            Toast.makeText(GirisActivity.this, "Reklam Yüklendi", Toast.LENGTH_SHORT).show();
            // Display an interstitial ad.
            showInterstitial();
        }

        @Override
        public void onAdFailed(int errorCode) {
            Toast.makeText(GirisActivity.this, "Reklam yüklemesi hata koduyla başarısız oldu: " + errorCode,
                    Toast.LENGTH_SHORT).show();
            Log.d("TAG", "Reklam yüklemesi hata koduyla başarısız oldu: " + errorCode);
        }

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            Log.d("TAG", "Kapanan Reklam");
        }

        @Override
        public void onAdClicked() {
            Log.d("TAG", "Tıklanan Reklam");
            super.onAdClicked();
        }

        @Override
        public void onAdOpened() {
            Log.d("TAG", "Açılan Reklam");
            super.onAdOpened();
        }
    };//Reklam kodları

    private void showInterstitial() {

        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Toast.makeText(this, "Reklam Yüklenmedi", Toast.LENGTH_SHORT).show();
        }
    }
    public void tanimla()
    {
        kullaniciAdiEditText = (EditText)findViewById(R.id.kullaniciAdiEditText);
        kayitOlButton = (Button)findViewById(R.id.kayitOlButton);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        kayitOlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = kullaniciAdiEditText.getText().toString();
                kullaniciAdiEditText.setText("");
                ekle(username);
            }
        });
    }
    public void ekle(final String kadi)
    {
        reference.child("Kullanıcılar").child(kadi).child("kullaniciadi").setValue(kadi).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Başarı ile Giriş Yaptınız", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(GirisActivity.this,MainActivity.class);
                    intent.putExtra("kadi",kadi);
                    startActivity(intent);
                }
            }

        });
    }

}