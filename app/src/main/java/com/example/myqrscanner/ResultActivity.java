package com.example.myqrscanner;

import android.os.Bundle;
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
    }
}