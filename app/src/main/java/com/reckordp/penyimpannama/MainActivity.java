package com.reckordp.penyimpannama;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    public String GITHUB_PROFILE = "https://github.com/Reckordp";
    private final String FILE_NAMA = "NAME_SAVE.reck";
    private final String TEKS_MENYAPA = "HALO, ";
    private final String AKHIRAN_SAPA = "!";

    private File pegangNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pegangNama = new File(getFilesDir(), FILE_NAMA);
        ulangiLayout();

        if (pegangNama.exists()) {
            tampilkanTeksMenyapa();
        } else {
            tampilkanEditor();
        }
    }

    private void ulangiLayout() {
        setContentView(R.layout.activity_main);
        Button mengulang = findViewById(R.id.mengulang_layout);
        mengulang.setOnClickListener(v -> {
            pegangNama.delete();
            tampilkanEditor();
            v.setVisibility(View.GONE);
        });
    }

    private void kosongkanFrame() {
        ViewGroup vg = findViewById(R.id.interaksi);
        vg.removeAllViews();

        runOnUiThread(() -> {
            vg.invalidate();
            vg.requestLayout();
        });
    }

    private void tampilkanTeksMenyapa() {
        kosongkanFrame();
        byte[] cbuf = new byte[2048];
        String cbufString = (new String()).concat(TEKS_MENYAPA);

        try {
            FileInputStream buff = new FileInputStream(pegangNama);
            if (buff.read(cbuf, 0, (int)FILE_NAMA.length()) > 0) {
                cbufString = cbufString.concat(new String(cbuf, StandardCharsets.UTF_8))
                        .concat(AKHIRAN_SAPA);
            }
            namaDitemukan(cbufString);
            buff.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button mengulang = findViewById(R.id.mengulang_layout);
        mengulang.setVisibility(View.VISIBLE);
    }

    private void tampilkanEditor() {
        kosongkanFrame();
        View vw = inflateBadan(R.layout.mengetik);
        ViewGroup.LayoutParams params = vw.getLayoutParams();
        params.width = getWindow().getAttributes().width;
        vw.setLayoutParams(params);

        EditText ketikan = (EditText)findViewById(R.id.ketikan_nama);
        ketikan.setOnEditorActionListener((v, actionId, event) -> {
            if ((actionId & EditorInfo.IME_MASK_ACTION) != 0) {
                String teksKetikan = v.getText().toString();
                try {
                    FileOutputStream fos = new FileOutputStream(pegangNama);
                    fos.write(teksKetikan.getBytes(StandardCharsets.UTF_8));
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ulangiLayout();
                tampilkanTeksMenyapa();
                return false;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AlertDialog.Builder pembuat = new AlertDialog.Builder(this);
        pembuat.setMessage(R.string.message);
        pembuat.setTitle(R.string.title_message);
        pembuat.setPositiveButton(android.R.string.ok, (dialog, which) -> {});
        pembuat.setNegativeButton(R.string.lebih_lanjut,
                (dialog, which) -> startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_PROFILE))
                ));
        pembuat.show();
    }

    protected void namaDitemukan(String nama) {
        ViewGroup vw = (ViewGroup)inflateBadan(android.R.layout.simple_list_item_1);
        TextView tview = (TextView)vw.getChildAt(0);
        tview.setText(nama);
    }

    protected View inflateBadan(@LayoutRes int resId) {
        return getLayoutInflater().inflate(resId, findViewById(R.id.interaksi));
    }
}