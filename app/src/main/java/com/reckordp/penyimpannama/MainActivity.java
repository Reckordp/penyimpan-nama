package com.reckordp.penyimpannama;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private final String FILE_NAMA = "NAME_SAVE.reck";
    private final String TEKS_MENYAPA = "HALO, ";
    private final String AKHIRAN_SAPA = "!";

    private File pegangNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        byte[] cbuf = new byte[2048];
        String cbufString = (new String()).concat(TEKS_MENYAPA);
        pegangNama = new File(getFilesDir(), FILE_NAMA);

        if (pegangNama.exists()) {
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
        } else {
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
                    return false;
                }
                return false;
            });
        }
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