package cd.esforca.entreprise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 1. Gérer l'animation du Splash Screen (doit être avant super.onCreate)
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // 2. Initialiser le bouton de démarrage
        Button btnStart = findViewById(R.id.btnStart);

        // 3. Navigation vers la gestion des départements (Logique du prof)
        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, ManageDepartmentsActivity.class);
            startActivity(intent);
            finish();
        });
    }
}