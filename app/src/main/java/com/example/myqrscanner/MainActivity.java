package com.example.myqrscanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.myqrscanner.databinding.ActivityMainBinding;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Запуск анимации при старте приложения
        startAnimation();

        // Проверка и запрос разрешения на камеру
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }

        binding.buttonScan.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                binding.barcodeView.setVisibility(View.VISIBLE);
                binding.ivAnim.setVisibility(View.GONE);
                startCameraPreview();
            } else {
                Toast.makeText(this, "Необходимо разрешение на использование камеры", Toast.LENGTH_SHORT).show();
            }
        });

        binding.tvTelegram.setOnClickListener(v -> {
            String telegramUrl = "https://t.me/kompaniaRost";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(telegramUrl));
            startActivity(intent);
        });
    }

    private void startAnimation() {
        if (binding.ivAnim.getDrawable() instanceof Animatable) {
            ((Animatable) binding.ivAnim.getDrawable()).start();
        }
    }

    private void startCameraPreview() {
        // Установка форматов для декодирования: QR-коды и штрих-коды
        List<BarcodeFormat> formats = Arrays.asList(
                BarcodeFormat.QR_CODE,
                BarcodeFormat.CODE_39,
                BarcodeFormat.CODE_128,
                BarcodeFormat.EAN_13,
                BarcodeFormat.UPC_A
                // Добавьте другие форматы, если нужно
        );

        binding.barcodeView.setDecoderFactory(new DefaultDecoderFactory(formats));

        // Начинаем предварительный просмотр и декодирование
        binding.barcodeView.decodeSingle(result -> runOnUiThread(() -> {
            if (result != null) {
                // Переход к ResultActivity с результатом кода
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("QR_DATA", result.getText());
                startActivity(intent);

                // Останавливаем и скрываем BarcodeView после завершения
                stopCameraPreview();
            } else {
                Toast.makeText(MainActivity.this, "Код не был считан", Toast.LENGTH_SHORT).show();
            }
        }));

        // Начинаем просмотр с камеры
        binding.barcodeView.resume();
    }

    private void stopCameraPreview() {
        binding.barcodeView.pause();
        binding.barcodeView.setVisibility(View.GONE);
        binding.ivAnim.setVisibility(View.VISIBLE);

        // Запускаем анимацию снова после завершения сканирования
        startAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding.barcodeView != null) {
            binding.barcodeView.pause();
        }
    }

    // Обработка результата запроса разрешения
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Разрешение на использование камеры получено", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Разрешение на использование камеры отклонено", Toast.LENGTH_SHORT).show();
            }
        }
    }
}