package com.example.myqrscanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myqrscanner.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Инициализация ViewBinding
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Получение данных из интента и отображение их
        String qrData = getIntent().getStringExtra("QR_DATA");
        binding.tvResult.setText(qrData);

        // Делаем текст кликабельным
        binding.tvResult.setOnClickListener(v -> {
            if (isValidUrl(qrData)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(qrData));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Считанный текст не является ссылкой", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Метод для проверки, является ли текст ссылкой
    private boolean isValidUrl(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://"));
    }
}
